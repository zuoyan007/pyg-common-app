package pyg.daheng.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @ClassName: DateUtils
 * @Author: ZhanSSH
 * @Date: 2021/1/20 11:36
 */
public class DateUtils {

    /**
     * 指定格式的日期字符串转时间戳
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Timestamp convertString2Timestamp(String dateStr,String formatStr){
        if(StringUtils.isEmpty(dateStr)){
            return null;
        }
        DateFormat df = new SimpleDateFormat(formatStr);
        try{
            return new Timestamp(df.parse(dateStr).getTime());
        }catch(ParseException e){
            String errorInfo = "cannot use formatStr:" + formatStr + "parse dateStr:" + dateStr;
            throw new RuntimeException(errorInfo,e);
        }
    }

    /**
     * 根据指定日期时间戳的下一天的对应时间
     * @param day
     * @return
     */
    public static Date getNextDay(Timestamp day){
        Calendar now = Calendar.getInstance();
        now.setTime(day);
        now.set(Calendar.DATE,now.get(Calendar.DATE) + 1);
        return new Date(now.getTime().getTime());
    }

    /**
     * 根据指定日期时间戳的下一天的对应时间
     * @param date
     * @return
     */
    public static Timestamp getTimestamp(Date date){
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        return new Timestamp(now.getTime().getTime());
    }
}
