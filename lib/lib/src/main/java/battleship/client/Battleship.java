package battleship.client;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;

public class Battleship {
    WebSocketClient client;

    public Battleship() throws Exception {
        WebSocketClient client = new Client(new URI("wss://simfony.tech/mitty/websocket"));
        client.connect();
    }

    public void sendText(String msg) {
        client.send(msg);
    }
}
