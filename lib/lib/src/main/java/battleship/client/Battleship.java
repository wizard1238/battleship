package battleship.client;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.java_websocket.client.WebSocketClient;
import com.google.gson.*;

public class Battleship {
    WebSocketClient client = new Client(new URI("wss://simfony.tech/mitty/battleship"), this);
    BattleshipInterface battleshipInterface;

    Gson gson = new Gson();
    JsonElement jsonElement;
    JsonObject gameState;
    String currDataRequest = "", matchId = "", readyData = "", moveData = "", errMsg = "", receivedMove = "", respond = "";

    CountDownLatch createNewGameLatch = new CountDownLatch(1);
    CountDownLatch joinGameLatch = new CountDownLatch(1);
    CountDownLatch readyLatch = new CountDownLatch(1);
    CountDownLatch sendMoveLatch = new CountDownLatch(1);
    CountDownLatch respondLatch = new CountDownLatch(1);

    public Battleship(BattleshipInterface battleshipInterface) throws Exception {
        this.battleshipInterface = battleshipInterface;
        client.connectBlocking();
    }

    public void sendText(String msg) {
        client.send(msg);
    }

    public void recievedText(String msg) throws BattleshipParsingException { // Called when text is recieved by client
        try {
            this.jsonElement = JsonParser.parseString(msg);
            this.gameState = jsonElement.getAsJsonObject();
        } catch (Exception e) {
            if (msg.equals("The other player has disconnected")) {
                gameDestroyed();
            } else if (this.currDataRequest.equals("join")) {
                joinGameLatch.countDown();
                throw new BattleshipParsingException(msg);
            } else if (this.currDataRequest.equals("matchId")) {
                createNewGameLatch.countDown();
                throw new BattleshipParsingException(msg);
            } else if (this.currDataRequest.equals("ready")) {
                readyLatch.countDown();
                throw new BattleshipParsingException(msg);
            } else if (this.currDataRequest.equals("sendMove")) {
                sendMoveLatch.countDown();
                throw new BattleshipParsingException(msg);
            } else if (this.currDataRequest.equals("respond")) {
                respond = msg;
                respondLatch.countDown();
            }
        }

        System.out.println("DEBUGLIB " + msg);
       
        if (this.currDataRequest.equals("join")) {
            this.matchId = this.gameState.get("matchId").toString();
            joinGameLatch.countDown();
        } else if (this.currDataRequest.equals("matchId")) {
            this.matchId = this.gameState.get("matchId").toString();
            createNewGameLatch.countDown();
        } else if (this.currDataRequest.equals("ready")) {
            this.readyData = this.gameState.get("matchId").toString(); // TODO: Change to actual game object
            readyLatch.countDown();
        } else if (this.currDataRequest.equals("sendMove")) {
            this.moveData = this.gameState.get("response").toString(); // TODO: Change to actual game object
            sendMoveLatch.countDown();
        } else if (this.currDataRequest.equals("receiveMove")) {
            receivedMove(Integer.parseInt(this.gameState.get("col").toString()), this.gameState.get("row").toString().charAt(0));
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

    public String makeMove(int col, char row) {
        this.currDataRequest = "sendMove";
        Character.toLowerCase(col);
        if (row < 'a' || row > 'k') {
            return "Row out of bounds. Must be between 'a' and 'k', inclusive.";
        } else if ( col < 0 || col > 8) {
            return "Column out of bounds. Must be between 0 and 8, inclusive.";
        } else {
            this.sendText(String.format("{  \"option\": \"move\", \"col\": %d, \"row\": \"%c\" }", col, row));
            
            try {
                sendMoveLatch.await(); // blocking, only in method scope
            } catch (InterruptedException e) {}

            sendMoveLatch = new CountDownLatch(1);
            this.currDataRequest = "receiveMove";
            return this.moveData;
        }
    }

    public String respondToMove(String status) {
        if (!status.equals("hit") || !status.equals("miss") || !status.equals("sink")) {
            return "Status must be hit, miss, or sink";
        }

        this.currDataRequest = "respond";
        this.sendText("{ \"option\": \"response\", \"response\": \"" +  status + "\" }");

        try {
            respondLatch.await(); // blocking, only in method scope
        } catch (InterruptedException e) {}

        return this.respond;
    }

    public void receivedMove(int col, char row) {
        this.battleshipInterface.recievedMove(col, row);
    }

    public void gameDestroyed() {
        this.battleshipInterface.gameDestroyed();
    }
}