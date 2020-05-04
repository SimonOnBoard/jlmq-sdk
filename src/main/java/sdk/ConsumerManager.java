package sdk;


import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConsumerManager {
    private final Map<String, Consumer> consumerMap = new HashMap<>();

    public Optional<Consumer> getCurrentConsumer(String queue) {

        return Optional.ofNullable(consumerMap.get(queue));
    }

    public boolean putCurrentSession(String queue, Consumer consumer) {
        if(consumerMap.get(queue) != null) return false;
        consumerMap.put(queue, consumer);
        return true;
    }

    public void remove(String queue){
        consumerMap.remove(queue);
    }

    public void closeAll() {
        consumerMap.entrySet().stream().forEach(item -> item.getValue().close());
    }
}
