package eureka.config;

import java.io.Serializable;

/**
 * @author zhangjuwa
 * @description kafka消息实体
 * @date 2018/9/26
 * @since jdk1.8
 */
public class KafkaMessage implements IMessage, Serializable {

    private static final long serialVersionUID = -2333002967579590853L;
    private Integer id;

    /**
     * 消息
     */
    private String message; //

    @Override
    public String toString() {
        return "KafkaMessage{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
