package battleship.client;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.java_websocket.client.WebSocketClient;
import com.google.gson.*;

public class Battleship {
    WebSocketClient client = new Client(new URI("wss://simfony.tech/mitty/websocket"), this);
    BattleshipInterface battleshipInterface;

    Gson gson = new Gson();
    JsonElement jsonElement;
    JsonObject gameState;
    String currDataRequest = "", matchId = "", readyData = "", moveData = "", errMsg = "", receivedMove = "";

    CountDownLatch createNewGameLatch = new CountDownLatch(1);
    CountDownLatch joinGameLatch = new CountDownLatch(1);
    CountDownLatch readyLatch = new CountDownLatch(1);
    CountDownLatch sendMoveLatch = new CountDownLatch(1);

    public Battleship(BattleshipInterface battleshipInterface) throws Exception {
        this.battleshipInterface = battleshipInterface;
        client.connectBlocking();
    }

    public void sendText(String msg) {
        client.send(msg);
    }

    public void recievedText(String msg) { // Called when text is recieved by client
        // System.out.println("recieved text");

        try {
            this.jsonElement = JsonParser.parseString(msg);
            this.gameState = jsonElement.getAsJsonObject();
        } catch (Exception e) {
            if (msg.equals("The other player has disconnected")) {
                gameDestroyed();
            } else if (this.currDataRequest.equals("join")) {
                this.matchId = msg;
                joinGameLatch.countDown();
            } else if (this.currDataRequest.equals("matchId")) {
                this.matchId = msg;
                createNewGameLatch.countDown();
            } else if (this.currDataRequest.equals("ready")) {
                this.readyData = msg;
                readyLatch.countDown();
            } else if (this.currDataRequest.equals("sendMove")) {
                this.moveData = msg;
                sendMoveLatch.countDown();
            }
        }
       
        if (this.currDataRequest.equals("join")) {
            this.matchId = this.gameState.get("matchId").toString();
            joinGameLatch.countDown();
        } else if (this.currDataRequest.equals("matchId")) {
            this.matchId = this.gameState.get("matchId").toString();
            createNewGameLatch.countDown();
        } else if (this.currDataRequest.equals("ready")) {
            this.readyData = this.gameState.get("matchId").toString(); // TODO: Change to actual game object
            readyLatch.countDown();
        // } else if (this.currDataRequest.equals("sendMove")) {
        //     this.moveData = this.gameState.get("matchId").toString(); // TODO: Change to actual game object
        //     sendMoveLatch.countDown();
        //     System.out.println(this.moveData);
        } else if (this.currDataRequest.equals("receiveMove")) {
            receivedMove(this.gameState.get("col").toString().charAt(0), Integer.parseInt(this.gameState.get("row").toString()));
        }
    }

    public String createNewGame() { // returns game code
        this.currDataRequest = "matchId";
        this.sendText("{ \"option\": \"new\" }");

        try {
            createNewGameLatch.await(); // blocking, only in method scope
        } catch (InterruptedException e) {}

        createNewGameLatch = new CountDownLatch(1); // reset
        return this.matchId;
    }
    
    public String joinGame(String matchId) { // returns game code
        this.currDataRequest = "join";
        // this.sendText("{ \"option\": \"join\", \"matchId\": \"" + matchId + "\" }");
        this.sendText(String.format("{ \"option\": \"join\", \"matchId\": \"%s\" }", matchId));

        try {
            joinGameLatch.await(); // blocking, only in method scope
        } catch (InterruptedException e) {}

        joinGameLatch = new CountDownLatch(1);
        return this.matchId;
    }

    public String setReady(String matchId) { // returns game code
        this.currDataRequest = "ready";
        this.sendText("{ \"option\": \"ready\" }");

        try {
            readyLatch.await(); // blocking, only in method scope
        } catch (InterruptedException e) {}

        readyLatch = new CountDownLatch(1);
        return this.readyData;
    }

    public String makeMove(char col, int row) {
        this.currDataRequest = "sendMove";
        Character.toLowerCase(col);
        if (col < 'a' || col > 'j') {
            return "Row out of bounds. Must be between 'a' and 'j', inclusive.";
        } else if ( row < 0 || row > 9) {
            return "Column out of bounds. Must be between 0 and 9, inclusive.";
        } else {
            this.sendText(String.format("{  \"option\": \"move\", \"col\": \"%c\", \"row\": %d }", col, row));
            
            try {
                sendMoveLatch.await(); // blocking, only in method scope
            } catch (InterruptedException e) {}

            sendMoveLatch = new CountDownLatch(1);
            this.currDataRequest = "receiveMove";
            return this.moveData;
        }
    }

    public void receivedMove(char col, int row) {
        this.battleshipInterface.recievedMove(col, row);
    }

    public void gameDestroyed() {
        this.battleshipInterface.gameDestroyed();
    }
}