package battleship.client;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;

public class Battleship {
    WebSocketClient client = new Client(new URI("wss://simfony.tech/mitty/websocket"), this);

    public Battleship() throws Exception {
        client.connectBlocking();
    }

    public void sendText(String msg) {
        client.send(msg);
    }

    public void recievedText(String msg) {
        //Is overriden
    }
}
