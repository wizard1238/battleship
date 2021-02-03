public class Fleet
{
    private Ship[] ships = new Ship[5];
    public Fleet(){
        
    }
    public void addShip(Ship ship){
        ships[ship.getNumber()] = ship;
    }
    public Ship[] getShips(){
        return ships;
    }
}