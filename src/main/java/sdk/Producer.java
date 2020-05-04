package sdk;

import sdk.ProducerMessages.ProducerUniformMessage;

public class Producer {
    private Connector connector;
    private String queue;
    public Producer(Connector connector, String queue) {
        this.connector = connector;
        this.queue = queue;
        this.send(ProducerUniformMessage.builder().option("startProducer").build());
    }

    public void send(Object payload){
        this.send(ProducerUniformMessage.builder().queue(queue).option("task").payload(payload).build());
    }

    private void send(ProducerUniformMessage message) {
        connector.send(message);
    }
}
