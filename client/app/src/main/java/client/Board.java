package client;

public class Board
{
    private static final int ROWS = 11;
    private static final int COLUMNS = 9;
    private static final char EMPTY = '-';
    public static final int VALID_PLACEMENT = 0;
    public static final int NOT_ON_BOARD = 1;
    public static final int OVERLAPS_SHIPS = 2;
    private static final char SHIP = 'O';
    public static final char HIT = 'H';
    public static final char MISS = 'M';
    public static final char SUNK = 'S';

    private final char[][] board = new char[ROWS][COLUMNS];
    private int hit = 0;
    public Board(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                board[i][j] = EMPTY;
            }
        }
    }
    public static void displayBoards(Board myBoard, Board opponentBoard){
        System.out.print("  ");
        for(int i = 0; i < COLUMNS; i++){
            System.out.print(i + " ");
        }
        System.out.print("       ");
        for(int i = 0; i < COLUMNS; i++){
            System.out.print(i + " ");
        }
        System.out.println();
        for(int i = 0; i < ROWS; i++){
            System.out.print((char)(i+65) + " ");
            for(int j = 0; j < COLUMNS; j++){
                System.out.print(myBoard.board[i][j] + " ");
            }
            System.out.print("     ");
            System.out.print((char)(i+65) + " ");
            for(int j = 0; j < COLUMNS; j++){
                System.out.print(opponentBoard.board[i][j] + " ");
            }
            System.out.println();
        }
    }
    public int validPlacement(int row, int column, int length, char direction){
        if(direction == 'N'){
            if(row - length + 1 < 0){
                return NOT_ON_BOARD;
            }
            for(int i = 0; i < length; i++){
                if(board[row-i][column] == SHIP){
                    return OVERLAPS_SHIPS;
                }
            }
        } else if(direction == 'S'){
            if(row + length - 1 > ROWS){
                return NOT_ON_BOARD;
            }
            for(int i = 0; i < length; i++){
                if(board[row+i][column] == SHIP){
                    return OVERLAPS_SHIPS;
                }
            }
        } else if(direction == 'W'){
            if(column - length + 1 < 0){
                return NOT_ON_BOARD;
            }
            for(int i = 0; i < length; i++){
                if(board[row][column-i] == SHIP){
                    return OVERLAPS_SHIPS;
                }
            }
        } else {
            if(column + length - 1 > COLUMNS){
                return NOT_ON_BOARD;
            }
            for(int i = 0; i < length; i++){
                if(board[row][column+i] == SHIP){
                    return OVERLAPS_SHIPS;
                }
            }
        }
        return VALID_PLACEMENT;
    }
    public void update(Ship ship){
        for(int i = 0; i < ship.getLength(); i++){
            if(ship.getDirection() == 'N'){
                board[ship.getRow()-i][ship.getColumn()] = SHIP;
            } else if(ship.getDirection() == 'S'){
                board[ship.getRow()+i][ship.getColumn()] = SHIP;
            } else if(ship.getDirection() == 'W'){
                board[ship.getRow()][ship.getColumn()-i] = SHIP;
            } else {
                board[ship.getRow()][ship.getColumn()+i] = SHIP;
            }
        }
    }
    public void updatePos(int row, int column, char result){
        board[row][column] = result;
    }
    public boolean updateUserBoard(int row, int column){
        if(board[row][column] == SHIP){
            board[row][column] = HIT;
            hit++;
            return true;
        } else {
            board[row][column] = MISS;
            return false;
        }
    }
    public boolean gameOver(){
        return hit >= 17;
    }

    public char getValue(int row, int col){
        return board[row][col];
      }
}