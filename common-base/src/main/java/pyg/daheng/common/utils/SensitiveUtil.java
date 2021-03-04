package pyg.daheng.common.utils;

import cn.csg.lib.common.utils.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import pyg.daheng.common.annotation.SensitiveInfo;
import pyg.daheng.common.aspect.DesensitizationAspect;
import pyg.daheng.common.constants.enums.SensitiveType;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 脱敏工具类
 * @author ZhanSSH
 * @date 2021/3/3 15:26
 */
public class SensitiveUtil {
    private static Logger logger = LoggerFactory.getLogger(DesensitizationAspect.class);
    /**
     * 对象格式json文本脱敏
     * {"a":"123"}
     * [{"a":"123"},{"b":"456"}]
     * @param jsonString
     * @return
     */
    public static Object sensitiveObject(String jsonString) {
        try {
            if (jsonString.startsWith("[{")){
                JSONArray jsonArray = JSONArray.parseArray(jsonString);
                for (int i = 0; i < jsonArray.size(); i++) {
                    jsonArray.set(i, sensitiveJsonObject(jsonArray.getJSONObject(i)));
                }
                return jsonArray;
            } else if (jsonString.startsWith("[")){
                return sensitiveList("", jsonString);
            } else {
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                return sensitiveJsonObject(jsonObject);
            }
        } catch (Exception e){
            return jsonString;
        }
    }

    /**
     * 对象格式JSONObject脱敏
     * @param jsonObject
     * @return
     */
    public static JSONObject sensitiveJsonObject(JSONObject jsonObject) {
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            jsonObject.put(entry.getKey(),findChild(entry.getKey(),entry.getValue()));
        }
        return jsonObject;
    }

    /**
     * Object对象格式脱敏
     * @param objectOptional 脱敏对象
     * @return 脱敏后的对象
     */
    public static Object sensitiveObject(Optional<Object> objectOptional) {
        objectOptional.ifPresent(obj -> {
            //判断是否是简单类型，简单类型不做处理
            if (BeanUtils.isSimpleValueType(obj.getClass())) {
                return;
            }
            if (obj instanceof Map) {
                sensitiveMap((Map<Object, Object>) obj);
            } else if (obj instanceof Collection) {
                sensitiveCollection((Collection<Object>) obj);
            } else {
                sensitiveBean(obj);
            }
        });
        return objectOptional.orElse(new HashMap<>());
    }

    /**
     * 对象Object类型进行脱敏
     * @param bean 脱敏对象
     * @return 脱敏后的对象
     */
    private static Object sensitiveBean(final Object bean) {
        PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanGetters(bean.getClass());
        Arrays.stream(propertyDescriptors).forEach(propertyDescriptor -> sensitivePropertyDescriptor(bean, propertyDescriptor));
        return bean;
    }

    /**
     * 针对列表脱敏
     * @param collection 脱敏列表
     * @return 脱敏后的列表
     */
    private static Collection<Object> sensitiveCollection(Collection<Object> collection) {
        collection.forEach(item -> {
            if (!BeanUtils.isSimpleValueType(item.getClass())) {
                sensitiveObject(Optional.of(item));
            }
        });
        return collection;
    }

    /**
     * map对象格式脱敏
     * @param map 脱敏对象
     * @return 脱敏后的对象
     */
    private static Map<Object, Object> sensitiveMap(Map<Object, Object> map) {
        map.entrySet().forEach(entry -> {
            Object value = entry.getValue();
            //如果key不是String类型或者value为null，则不做处理
            if (!(entry.getKey() instanceof String) || null == value) {
                return;
            }
            if (value instanceof String) {
                Optional<SensitiveType> optionalSensitiveType = EnumsUtils.getEnumObject(SensitiveType.class, e -> e.getValue().equals((entry.getKey())));
                value = hideSensitive(optionalSensitiveType.orElse(SensitiveType.NONE), value);
            } else if (value instanceof Map) {
                sensitiveObject(Optional.ofNullable(value));
            }
            entry.setValue(value);
        });
        return map;
    }

    /**
     * 根据Field属性进行脱敏
     * @param field field属性
     * @param value 脱敏对象
     * @return 脱敏后结果
     */
    private static Object sensitiveField(Field field, String value) {
        if (field == null) {
            return value;
        }
        SensitiveInfo sensitiveInfo = field.getAnnotation(SensitiveInfo.class);
        //根据注解判断属性是否需要脱敏
        SensitiveType sensitiveType = null;
        if (null != sensitiveInfo) {
            if (sensitiveInfo.disable()) {
                return value;
            }
            sensitiveType = sensitiveInfo.sensitiveType();
        }
        //没有注解的情况下根据属性名去读取配置
        if (null == sensitiveType) {
            sensitiveType = EnumsUtils.getEnumObject(SensitiveType.class, e -> e.getValue().equals(field.getName())).orElse(SensitiveType.NONE);
        }
        return hideSensitive(sensitiveType, value);
    }

    /**
     * 根据PropertyDescriptor信息对对象的属性进行脱敏
     * @param bean 脱敏对象
     * @param propertyDescriptor 属性信息
     */
    private static void sensitivePropertyDescriptor(Object bean, PropertyDescriptor propertyDescriptor) {
        Method getMethod = propertyDescriptor.getReadMethod();
        Method setMethod = propertyDescriptor.getWriteMethod();
        //读取不到getMethod方法，则直接返回
        if (null == getMethod || null == setMethod) {
            logger.info("can not find getMethod or setMethod fieldName={}", propertyDescriptor.getName());
            return;
        }
        try {
            Object value = getMethod.invoke(bean);
            if (null == value) {
                return;
            }
            if (!BeanUtils.isSimpleValueType(value.getClass())) {
                sensitiveObject(Optional.of(value));
            } else if (value instanceof String) {
                value = sensitiveField(ReflectUtil.getField(bean.getClass(), propertyDescriptor.getName()), (String) value);
                setMethod.invoke(bean, value);
            }
        }  catch (Exception e) {
            logger.error("sensitivePropertyDescriptor fail:", e);
        }
    }

    /**
     * 列表格式json文本脱敏
     * ["123","456"]
     * @param jsonString
     * @return
     */
    public static Object sensitiveList(String key, String jsonString) {
        try {
            List<Object> list = JSON.parseObject(jsonString, List.class);
            for (int i = 0; i < list.size(); i++) {
                Object object = hideSensitive(key, list.get(i));
                list.set(i, object);
            }
            return list;
        } catch (Exception e){
            return jsonString;
        }
    }

    public static Object findChild(String key, Object value) {
        String valueString = value.toString();
        //判断是否有内层嵌套
        if (valueString.startsWith("[{") || valueString.startsWith("{")){
            return sensitiveObject(valueString);
        } else if (valueString.startsWith("[")){
            return sensitiveList(key, valueString);
        } else {
            return hideSensitive(key, value);
        }
    }

    /**
     * 判断类型进行实际脱敏操作
     * @param sensitiveTypeValue
     * @param value
     * @return
     */
    public static Object hideSensitive(String sensitiveTypeValue,Object value) {
        SensitiveType sensitiveType = EnumsUtils.getEnumObject(SensitiveType.class, e -> e.getValue().equals(sensitiveTypeValue)).orElse(SensitiveType.NONE);
        return hideSensitive(sensitiveType, value);
    }

    /**
     * 判断类型进行实际脱敏操作
     * @param sensitiveType
     * @param value
     * @return
     */
    public static Object hideSensitive(SensitiveType sensitiveType, Object value) {
        String valueString = value.toString();
        if (sensitiveType == null) {
            return value;
        }
        switch (sensitiveType) {
            case MOBILE:
                return mobile(valueString);
            case MOBILE_PHONE:
                return mobile(valueString);
            case EMAIL:
                return email(valueString);
            case METERING_POINT_NUMBER:
                return meteringPointNumber(valueString);
            case SETTLE_ACCT_NUMBER:
                return settleAcctNumber(valueString);
            case ALI_USER_NAME:
                return aliUserName(valueString);
            case USER_NAME:
                return name(valueString);
            case REAL_NAME:
                return name(valueString);
            case CERT_NUMBER:
                return idCardNum(valueString);
            case DEVICEIDENTIF:
                return meteringPointNumber(valueString);
            case BANKCARD:
                return bankCard(valueString);
            case CONTACTPERSON:
                return name(valueString);
            case CONTACTPHONE:
                return mobile(valueString);
            case FAULTPOWFAILADDR:
                return address(valueString);
            case ELEADDRESS:
                return address(valueString);
            case METERING_POINT_ADDRESS:
                return address(valueString);
            case SETTLE_ACCT_NAME:
                return name(valueString);
            case SETTLE_ACCT_ADDRESS:
                return address(valueString);
            case ELECUSTNAME:
                return name(valueString);
            case APPLICANTPHONE:
                return mobile(valueString);
            default:
                break;
        }
        return value;
    }

    /**
     * 支付宝账号脱敏显示。分为邮箱和手机号
     * @param valueString
     * @return
     */
    public static String aliUserName(String valueString){
        if(valueString.contains("@")){
            return email(valueString);
        }else{
            return mobile(valueString);
        }
    }

    /**
     * [户名] 7位以下按人名，7位以上按公司名
     */
    public static String name(String name) {
        if (!StringUtils.isBlank(name)) {
            int length = name.length();
            if(length > 7){
                //七字以上
                return companyName(name);
            } else {
                //七字以下
                return chineseName(name);
            }
        }
        return name;
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李*明>
     */
    public static String chineseName(String fullName) {
        if (!StringUtils.isBlank(fullName)) {
            int length = fullName.length();
            if(length > 2){
                //三字以上名字中间脱敏
                String overlay = "";
                //中间一比一用*替换
                for(int i = 0; i<length-2; i++ ){
                    overlay += "*";
                }
                return StringUtils.overlay(fullName,overlay,1,length-1);
            }else{
                //三字以下名字只显示最后一个字
                String lastName = StringUtils.right(fullName,1);
                return StringUtils.leftPad(lastName, StringUtils.length(fullName), "*");
            }
        }
        return fullName;
    }

    /**
     * [公司名] 显示前5个后2个汉字，其他隐藏为1个星号（最多6个）
     */
    public static String companyName(String companyName) {
        if (!StringUtils.isBlank(companyName)) {
            int length = companyName.length();
            if(length > 7){
                //7字以上名字中间脱敏
                int sensitiveLength = length - 7;
                if (sensitiveLength > 6){
                    sensitiveLength = 6;
                }
                return StringUtils.left(companyName, 5) + StringUtils.leftPad(StringUtils.right(companyName, 2), sensitiveLength + 2, "*");
            }
        }
        return companyName;
    }

    /**
     * [身份证号] 显示前2位和最后2位，其他隐藏。共计18位或者15位。<例子：37*************62>
     */
    public static String idCardNum(String id) {
        if (!StringUtils.isBlank(id)) {
            return StringUtils.left(id, 2).concat(StringUtils.removeStart(
                    StringUtils.leftPad(StringUtils.right(id, 2), StringUtils.length(id), "*"),
                    "****"));
        }
        return id;
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     */
    public static String fixedPhone(String num) {
        if (!StringUtils.isBlank(num)) {
            return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
        }
        return num;
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138****1234>
     */
    public static String mobile(String num) {
        if (!StringUtils.isBlank(num)) {
            return StringUtils.left(num, 3)+"****"+StringUtils.right(num, 4);
        }
        return num;
    }

    /**
     * [地址] 只显示地址后8位，其他用星号隐藏每位1个星号(最多4个)；<例子：****海淀区X小区X栋>
     *
     */
    public static String address(String address) {
        if (!StringUtils.isBlank(address)) {
            final int length = StringUtils.length(address);
            if (length > 8){
                int sensitiveLength = length - 8;
                if (sensitiveLength > 4){
                    sensitiveLength = 4;
                }
                return StringUtils.leftPad(StringUtils.right(address, 8), sensitiveLength + 8, "*");
            }
        }
        return address;
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     */
    public static String email(String email) {
        if (!StringUtils.isBlank(email)) {
            final int index = StringUtils.indexOf(email, "@");
            if (index <= 1) {
                return email;
            } else {
                return StringUtils.rightPad(StringUtils.left(email, 1), index, "*")
                                  .concat(StringUtils.mid(email, index, StringUtils.length(email)));
            }
        }
        return email;
    }

    /**
     * [银行卡号] 前两位，后四位，其他用星号隐藏每位1个星号<例子:62***************1234>
     */
    public static String bankCard(String cardNum) {
        if (!StringUtils.isBlank(cardNum)) {
            return StringUtils.left(cardNum, 2).concat(StringUtils.removeStart(
                    StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"),
                    "******"));
        }
        return cardNum;
    }

    /**
     * [用电号] 前两位，后四位，其他用星号隐藏每位1个星号<例子:03**********1234>
     */
    public static String eleCustNumber(String eleCustNumber) {
        if (!StringUtils.isBlank(eleCustNumber)) {
            return StringUtils.left(eleCustNumber, 2).concat(StringUtils.removeStart(
                    StringUtils.leftPad(StringUtils.right(eleCustNumber, 4), StringUtils.length(eleCustNumber), "*"),
                    "**"));
        }
        return eleCustNumber;
    }

    /**
     * [计量点] 10位数字以内（含10位）全部展示
     * 10位数字以上只展示前2位和后8位数字，中间数字用3个“*”号代替  例子:03******12341234>
     */
    public static String meteringPointNumber(String meteringPointNumber) {
        if (!StringUtils.isBlank(meteringPointNumber)) {
            int length = meteringPointNumber.length();
            if(length > 10){
                return StringUtils.left(meteringPointNumber, 2)+"***"+StringUtils.right(meteringPointNumber, 8);
            }else{
                return meteringPointNumber;
            }
        }
        return meteringPointNumber;
    }

    /**
     * [用户户号] 前两位，后四位，其他用星号隐藏每位1个星号<例子:03**********1234>
     */
    public static String settleAcctNumber(String settleAcctNumber) {
        if (!StringUtils.isBlank(settleAcctNumber)) {
            return StringUtils.left(settleAcctNumber, 2).concat(StringUtils.removeStart(
                    StringUtils.leftPad(StringUtils.right(settleAcctNumber, 4), StringUtils.length(settleAcctNumber), "*"),
                    "**"));
        }
        return settleAcctNumber;
    }
}
