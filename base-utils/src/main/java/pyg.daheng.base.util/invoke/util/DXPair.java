package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:53
 */
public class DXPair<T1, T2> {
    private T1 first = null;
    private T2 second = null;
    public DXPair(){}
    public DXPair(T1 a, T2 b){
        this.first = a;
        this.second = b;
    }
    public static <T1, T2> DXPair<T1, T2> create(T1 a, T2 b){
        return new DXPair<T1, T2>(a, b);
    }
    public T1 getFirst() {
        return first;
    }
    public void setFirst(T1 a) {
        this.first = a;
    }
    public T2 getSecond() {
        return second;
    }
    public void setSecond(T2 b) {
        this.second = b;
    }
    public int hashCode() {
        int hashFirst = (first != null ? first.hashCode() : 0);
        int hashSecond = (second != null ? second.hashCode() : 0);

        return (hashFirst >> 1) ^ hashSecond;
    }
}
