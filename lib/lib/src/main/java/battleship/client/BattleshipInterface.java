package battleship.client;

public interface BattleshipInterface {
    public void matchJoined(String matchId);
    public void recievedMove(int col, char row);
    public void gameDestroyed();
}
