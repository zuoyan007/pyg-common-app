package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:05
 */
public class DXThreadLocal {
    private static volatile ThreadLocal<DXTransItem> dxTransItemTl;
    private static volatile ThreadLocal<String> dxSpanIdTl;

    public DXThreadLocal() {
    }

    public static ThreadLocal<DXTransItem> getDxTransItemTlInstance() {
        if (dxTransItemTl == null) {
            Class var0 = DXThreadLocal.class;
            synchronized(DXThreadLocal.class) {
                if (dxTransItemTl == null) {
                    dxTransItemTl = new ThreadLocal();
                }
            }
        }

        return dxTransItemTl;
    }

    public static ThreadLocal<String> getDxSpanIdTl() {
        if (dxSpanIdTl == null) {
            Class var0 = DXThreadLocal.class;
            synchronized(DXThreadLocal.class) {
                if (dxSpanIdTl == null) {
                    dxSpanIdTl = new ThreadLocal();
                }
            }
        }

        return dxSpanIdTl;
    }

    public static void addAnno(String oper, String msg) {
        DXTransItem transitem = (DXTransItem)getDxTransItemTlInstance().get();
        transitem.addanno(oper, msg);
        getDxTransItemTlInstance().set(transitem);
    }
}
