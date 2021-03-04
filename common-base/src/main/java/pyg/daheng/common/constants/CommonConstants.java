package pyg.daheng.common.constants;

import lombok.Data;
import pyg.daheng.common.config.AppConfig;

/**
 * @Description:
 * @ClassName: CommonConstants
 * @Author: ZhanSSH
 * @Date: 2021/1/20 10:39
 */
@Data
public class CommonConstants {

    public enum UserInfoConstants implements BaseConstants {
        DELETED_STATUS("deleted.status","1","删除标识"),
        UN_DELETED_STATUS("un.deleted","0","不删除"),
        VIP_STATUS("vip.status","1","vip标识"),
        UN_VIP_STATUS("un.vip.status","0","非vip标识"),
        ;
        private final String name;
        private final String defaultValue;
        private final String description;
        UserInfoConstants(String name,String defaultValue,String description){
            this.name = name;
            this.defaultValue = defaultValue;
            this.description = description;
        }

        @Override
        public Object get() {
            String property = AppConfig.getProperty(name);
            return property == null ? defaultValue : property;
        }
    }
}
interface BaseConstants{
    Object get();
    default String getStr(){return get().toString();}
    default Boolean getBool(){return Boolean.parseBoolean(getStr());}
    default Integer getInteger(){return Integer.parseInt(getStr());}
}
