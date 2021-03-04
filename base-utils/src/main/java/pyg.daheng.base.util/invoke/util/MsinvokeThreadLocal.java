package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:01
 */
public class MsinvokeThreadLocal {
    private static volatile ThreadLocal<FilterHeader> filterHeader;

    private MsinvokeThreadLocal() {
    }

    public static ThreadLocal<FilterHeader> getFilterHeader() {
        if (filterHeader == null) {
            Class var0 = MsinvokeThreadLocal.class;
            synchronized(MsinvokeThreadLocal.class) {
                if (filterHeader == null) {
                    filterHeader = new ThreadLocal();
                    filterHeader.set(new FilterHeader());
                }
            }
        }

        if (filterHeader.get() == null) {
            filterHeader.set(new FilterHeader());
        }

        return filterHeader;
    }
}
