package sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class ClientEndPoint {
    private WebSocketContainer container;
    private Session userSession;
    private Connector connector;
    private boolean isClosed;
    private ConsumerManager consumerManager;
    private ObjectMapper objectMapper;

    public ClientEndPoint(Connector connector, ConsumerManager consumerManager, ObjectMapper objectMapper) {
        container = ContainerProvider.getWebSocketContainer();
        this.consumerManager = consumerManager;
        this.connector = connector;
        isClosed = true;
        this.objectMapper = objectMapper;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen:" + session.getId());
        isClosed = false;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        isClosed = true;
        System.out.println("onClose:" + session.getId());
        consumerManager.closeAll();
    }

    @OnMessage
    public void onMessage(String msg) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(msg);
        String command;
        if (jsonNode.isNull() || jsonNode.isMissingNode() || (command = jsonNode.get("command").asText()) == null) {
            throw new IllegalStateException("Bad data");
        }
        switch (command) {
            case "do":
                consumerManager.getCurrentConsumer(jsonNode.get("queue").asText())
                        .ifPresent(consumer -> {
                            consumer.handle(jsonNode);
                        });
                break;
            case "drop":
                consumerManager.remove(jsonNode.get("queue").asText());
                break;
        }
    }

    public void connect(String serverUri) {
        try {
            userSession = container.connectToServer(this, new URI(serverUri));
            userSession.setMaxTextMessageBufferSize(Integer.MAX_VALUE);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized void syncSendMessage(String msg) {
        try {
            userSession.getBasicRemote().sendText(msg);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void disconnect() {
        try {
            if (userSession != null && userSession.isOpen()) {
                userSession.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isClosed() {
        return isClosed;
    }
}