package pyg.daheng.common.annotation;

import pyg.daheng.common.constants.enums.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 * @author ZhanSSH
 * @date 2021/3/3 16:59
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LimitAnnotation {

    /**
     * 名称
     * @return String
     */
    String name() default "";

    /**
     * key
     * @return String
     */
    String key() default "";

    /**
     * 前缀
     * @return String
     */
    String prefix() default "";

    /**
     * 限流类型
     * @return LimitType
     */
    LimitType limitType() default LimitType.IP;

    /**
     * 时间段
     * @return int
     */
    int period() default 10;

    /**
     * 阈值
     * @return int
     */
    int count() default 5;
}
