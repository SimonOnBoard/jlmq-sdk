import sdk.*;
import sdk.ProducerMessages.SimpleTextMessage;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Connector connector = JLMQ.connector("ws://localhost:8099/queue");
        Producer producer = connector.producer("toPrint");
        Producer producer1 = connector.producer("toDo");
        Handler<SimpleTextMessage> messageHandler = message -> {
            System.out.println(message.getText());
        };

        Handler<SimpleTextMessage> messageHandler1 = message -> {
            System.out.println(message.getText());
        };
        Consumer consumer = connector.consumer("toDo");
        consumer.setHandler(messageHandler, SimpleTextMessage.class);
        Consumer consumer1 = connector.consumer("toPrint");
        consumer1.setHandler(messageHandler1, SimpleTextMessage.class);


        Thread.sleep(30000);
        connector.close();
    }
}
