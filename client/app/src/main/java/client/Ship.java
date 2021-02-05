package client;

public class Ship
{
    private String name;
    private int number;
    private int length;
    private int row;
    private int column;
    private char direction;
    private static int totalShips = 0;
    public Ship(String name, int number, int length){
        this.name = name;
        this.number = number;
        this.length = length;
        totalShips++;
    }
    public int getNumber(){
        return number;
    }
    public int getLength(){
        return length;
    }
    public void set(int row, int column, char direction){
        this.row = row;
        this.column = column;
        this.direction = direction;
    }
    public int getRow(){
        return row;
    }
    public int getColumn(){
        return column;
    }
    public char getDirection(){
        return direction;
    }
}