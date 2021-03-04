package pyg.daheng.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @ClassName: NoSupperVip
 * @Author: ZhanSSH
 * @Date: 2021/1/18 11:22
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSupperVip {
}
