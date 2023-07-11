package mayfly.sys.biz.common.websocket;

import lombok.Data;
import mayfly.core.model.result.CodeMessage;
import mayfly.core.util.JsonUtils;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-13 2:18 下午
 */
@Data
public class WebSocketMessage<T> {

    private Integer type;

    private Integer status;

    private T data;

    public WebSocketMessage(Integer type, Integer status, T data) {
        this.type = type;
        this.status = status;
        this.data = data;
    }

    public static <T> String msg(MessageTypeEnum type, CodeMessage result, T data) {
        return JsonUtils.toJSONString(new WebSocketMessage<T>(type.getValue(), Integer.parseInt(result.getCode()), data));
    }
}
