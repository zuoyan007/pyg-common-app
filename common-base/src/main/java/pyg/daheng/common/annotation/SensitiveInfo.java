package pyg.daheng.common.annotation;

import pyg.daheng.common.constants.enums.SensitiveType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author ZhanSSH
 * @date 2021/2/25 17:20
 */
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface SensitiveInfo {

    /**
     * 脱敏类型
     * @return 返回
     */
    SensitiveType sensitiveType() default SensitiveType.NONE;

    /**
     * 关闭脱敏
     * @return 返回
     */
    boolean disable() default false;
}
