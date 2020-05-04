package sdk.ProducerMessages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sdk.Message;

import javax.websocket.MessageHandler;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleTextMessage implements Message {
    private String text;
}
