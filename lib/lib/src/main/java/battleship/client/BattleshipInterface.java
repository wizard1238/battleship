package battleship.client;

public interface BattleshipInterface {
    public void matchJoined(String matchId);
    public void recievedMove(char col, int row);
}
