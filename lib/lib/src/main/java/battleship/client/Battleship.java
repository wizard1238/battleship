package battleship.client;

import java.io.*;
import java.util.*;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import com.google.gson.*;

public class Battleship {
    WebSocketClient client = new Client(new URI("wss://simfony.tech/mitty/websocket"), this);

    Gson gson = new Gson();
    JsonElement jsonElement;
    JsonObject gameState;

    public Battleship() throws Exception {
        client.connectBlocking();
    }

    public void sendText(String msg) {
        client.send(msg);
    }

    public void recievedText(String msg) { //Called when text is recieved by client
        // Is overriden
        this.jsonElement = JsonParser.parseString(msg);
        this.gameState = jsonElement.getAsJsonObject();
        System.out.println(this.gameState.get("matchId")); // debug

        // TODO: check for error messages
    }

    public void makeMove() {
        //send json
        client.send("{\"json\":true}");
    }

    public void passData(String data) {
        //overriden
    }

    public String createNewGame() { //returns game code
        //json[option] = new,
        client.send("");
        return "";
    }
    
    public void joinGame() {
        // {
        //     option: 'join',
        //     matchId: matchId,
        // }

        // Send this
    }
}
