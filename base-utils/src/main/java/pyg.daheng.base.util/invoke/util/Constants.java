package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:48
 */
public class Constants {
    public static enum AutoOffsetReset{
        latest("latest"),
        earliest("earliest"),
        none("none");
        private final String value;
        public String getValue(){return value;}
        public static AutoOffsetReset getVariable(String v){for(AutoOffsetReset var:values()){if(var.getValue().equals(v)) return var;}return null;}
        AutoOffsetReset(String v){this.value = v;}
    }
    public static enum KafkaClientType{
        producer("producer"),
        consumer("consumer");
        private final String value;
        public String getValue(){return value;}
        public static KafkaClientType getVariable(String v){for(KafkaClientType var:values()){if(var.getValue().equals(v)) return var;}return null;}
        KafkaClientType(String v){this.value = v;}
    }
}
