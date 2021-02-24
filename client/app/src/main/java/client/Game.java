package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.geometry.HPos;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.TextField;

import java.util.concurrent.atomic.AtomicInteger;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import battleship.client.Battleship;
import battleship.client.BattleshipInterface;

public class Game extends Application implements BattleshipInterface
{
    private Battleship battleshipLib;
    private Stage stage;
    private Scene scene;
    private String code;
    private Board myBoard, enemyBoard;
    private GridPane pane;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        battleshipLib = new Battleship(this);
        //System.out.println(battleshipLib.createNewGame());

        myBoard = new Board();
        enemyBoard = new Board();
        
        Fleet fleet = new Fleet();
        Ship destroyer = new Ship("Destroyer", 0, 2);
        fleet.addShip(destroyer);
        Ship submarine = new Ship("Submarine", 1, 3);
        fleet.addShip(submarine);
        Ship cruiser = new Ship("Cruiser", 2, 3);
        fleet.addShip(cruiser);
        Ship battleship = new Ship("Battleship", 3, 4);
        fleet.addShip(battleship);
        Ship carrier = new Ship("Carrier", 4, 5);
        fleet.addShip(carrier);
        
        AtomicInteger locationX = new AtomicInteger(-1); // col
        AtomicInteger locationY = new AtomicInteger(-1); // row
        AtomicInteger counter = new AtomicInteger(0);
        Rectangle[][] myRectangles = new Rectangle[11][9];
        Rectangle[][] enemyRectangles = new Rectangle[11][9];
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 9; j++){
                myRectangles[i][j] = new Rectangle(40, 40);
                myRectangles[i][j].setFill(Color.TRANSPARENT);
                myRectangles[i][j].setStroke(Color.BLACK);
                enemyRectangles[i][j] = new Rectangle(40, 40);
                enemyRectangles[i][j].setFill(Color.TRANSPARENT);
                enemyRectangles[i][j].setStroke(Color.BLACK);
            }
        }
        pane = new GridPane();
        
        for(int i = 0; i < 880/40; i++){
            pane.getColumnConstraints().add(new ColumnConstraints(40));
        }
        
        for(int i = 0; i < 520/40; i++){
            pane.getRowConstraints().add(new RowConstraints(40));
        }

        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 9; j++){
                pane.add(myRectangles[i][j], j+1, i+1);
                pane.add(enemyRectangles[i][j], j+12, i+1);
            }
        }
        
        for(int i = 0; i < 9; i++){
            Label label = new Label(String.valueOf(i));
            Label label2 = new Label(String.valueOf(i));
            pane.add(label, i+1, 0);
            pane.add(label2, i+12, 0);
            pane.setHalignment(label, HPos.CENTER);
            pane.setHalignment(label2, HPos.CENTER);
        }
        
        for(int i = 0; i < 11; i++){
            Label label = new Label(Character.toString((char)(i+65)));
            Label label2 = new Label(Character.toString((char)(i+65)));
            pane.add(label, 0, i+1);
            pane.add(label2, 11, i+1);
            pane.setHalignment(label, HPos.CENTER);
            pane.setHalignment(label2, HPos.CENTER);
        }
        
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        
        Popup directionPopup = new Popup();
        directionPopup.setX(visualBounds.getWidth()/2-150);
        directionPopup.setY(visualBounds.getHeight()/2-150);
        
        Rectangle directionPopupRect = new Rectangle(300, 300);
        directionPopupRect.setFill(Color.WHITE);
        
        Button northButton = new Button("North");
        northButton.setLayoutX(130);
        northButton.setLayoutY(5);
        
        Button southButton = new Button("South");
        southButton.setLayoutX(130);
        southButton.setLayoutY(270);
        
        Button eastButton = new Button("East");
        eastButton.setLayoutX(250);
        eastButton.setLayoutY(140);
        
        Button westButton = new Button("West");
        westButton.setLayoutX(5);
        westButton.setLayoutY(140);
        
        directionPopup.getContent().addAll(directionPopupRect);
        directionPopup.getContent().addAll(northButton);
        directionPopup.getContent().addAll(southButton);
        directionPopup.getContent().addAll(eastButton);
        directionPopup.getContent().addAll(westButton);
        directionPopup.hide();
        
        northButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int valid = myBoard.validPlacement(locationY.get(), locationX.get(), fleet.getShips()[counter.get()].getLength(), 'N');
                if(valid == 0){
                    fleet.getShips()[counter.get()].set(locationY.get(), locationX.get(), 'N');
                    myBoard.update(fleet.getShips()[counter.get()]);
                    for(int i = 0; i < fleet.getShips()[counter.get()].getLength(); i++){
                        Rectangle rectangle = new Rectangle(40, 40);
                        rectangle.setFill(Color.GRAY);
                        pane.add(rectangle, locationX.get()+1, locationY.get()+1-i);
                    }
                    myBoard.update(fleet.getShips()[counter.get()]);
                    if(counter.incrementAndGet() == 5){
                        battleshipLib.setReady(code);
                    }
                } else {
                    System.out.println("Invalid placement");
                }
                directionPopup.hide();
            }
        });
        
        southButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int valid = myBoard.validPlacement(locationY.get(), locationX.get(), fleet.getShips()[counter.get()].getLength(), 'S');
                if(valid == 0){
                    fleet.getShips()[counter.get()].set(locationY.get(), locationX.get(), 'S');
                    myBoard.update(fleet.getShips()[counter.get()]);
                    for(int i = 0; i < fleet.getShips()[counter.get()].getLength(); i++){
                        Rectangle rectangle = new Rectangle(40, 40);
                        rectangle.setFill(Color.GRAY);
                        pane.add(rectangle, locationX.get()+1, locationY.get()+1+i);
                    }
                    if(counter.incrementAndGet() == 5){
                        battleshipLib.setReady(code);
                    }
                } else {
                    System.out.println("Invalid placement");
                }
                directionPopup.hide();
            }
        });
        
        eastButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int valid = myBoard.validPlacement(locationY.get(), locationX.get(), fleet.getShips()[counter.get()].getLength(), 'E');
                if(valid == 0){
                    fleet.getShips()[counter.get()].set(locationY.get(), locationX.get(), 'E');
                    myBoard.update(fleet.getShips()[counter.get()]);
                    for(int i = 0; i < fleet.getShips()[counter.get()].getLength(); i++){
                        Rectangle rectangle = new Rectangle(40, 40);
                        rectangle.setFill(Color.GRAY);
                        pane.add(rectangle, locationX.get()+1+i, locationY.get()+1);
                    }
                    if(counter.incrementAndGet() == 5){
                        battleshipLib.setReady(code);
                    }
                } else {
                    System.out.println("Invalid placement");
                }
                directionPopup.hide();
            }
        });
        
        westButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                int valid = myBoard.validPlacement(locationY.get(), locationX.get(), fleet.getShips()[counter.get()].getLength(), 'W');
                if(valid == 0){
                    fleet.getShips()[counter.get()].set(locationY.get(), locationX.get(), 'W');
                    myBoard.update(fleet.getShips()[counter.get()]);
                    for(int i = 0; i < fleet.getShips()[counter.get()].getLength(); i++){
                        Rectangle rectangle = new Rectangle(40, 40);
                        rectangle.setFill(Color.GRAY);
                        pane.add(rectangle, locationX.get()+1-i, locationY.get()+1);
                    }
                    if(counter.incrementAndGet() == 5){
                        battleshipLib.setReady(code);
                    }
                } else {
                    System.out.println("Invalid placement");
                }
                directionPopup.hide();
            }
        });
    
        
        pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            int x = ((int)(event.getSceneX())/40);
            int y = ((int)(event.getSceneY())/40);
            // System.out.println("X: " + x + " Y: " + y);
            if(x >= 1 && x <= 9 && y >= 1 && y <= 11){
                int count = counter.get();
                if(count < 5){
                    locationX.set(x-1);
                    locationY.set(y-1);
                    directionPopup.show(stage);
                }
            } else if(x >= 12 && x <= 20 && y >= 1 && y <= 11) {
                locationX.set(x-12); //Col
                locationY.set(y-1); //Row

                if(battleshipLib.makeMove(locationX.get(), (char)(locationY.get() + 97)).equals("hit")){
                    enemyBoard.updatePos(locationY.get(), locationX.get(), 'H');
                    addCircle(locationX.get()+12, locationY.get()+1, Color.RED);
                } else {
                    enemyBoard.updatePos(locationY.get(), locationX.get(), 'M');
                    addCircle(locationX.get()+12, locationY.get()+1, Color.GREEN);
                }
                // hitPopup.show(stage);
            }
        });

        scene = new Scene(pane,880,520);

        Pane startupPane = new Pane();
        Label startupLabel = new Label("Welcome to Battleship");
        startupLabel.setLayoutX(90);
        startupPane.getChildren().add(startupLabel);

        TextField inputCode = new TextField();
        inputCode.setLayoutX(70);
        inputCode.setLayoutY(150);

        Button hostButton = new Button("Host");
        hostButton.setLayoutX(50);
        hostButton.setLayoutY(200);

        Button joinButton = new Button("Join");
        joinButton.setLayoutX(200);
        joinButton.setLayoutY(200);

        hostButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e) {
                inputCode.setText(battleshipLib.createNewGame());
                code = inputCode.getText();
                e.consume();
            }
        });
        joinButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e) {
                battleshipLib.joinGame(inputCode.getText());
                code = inputCode.getText();
                e.consume();
            }
        });
        startupPane.getChildren().add(hostButton);
        startupPane.getChildren().add(joinButton);
        startupPane.getChildren().add(inputCode);


        Scene startupPage = new Scene(startupPane, 300, 300);
        stage.setTitle("Battleship");
        stage.setScene(startupPage);
        stage.show();

    }

    @Override
    public void gameDestroyed() {
        // TODO Auto-generated method stub
    }

    @Override
    public void matchJoined(String arg0) {
        stage.setScene(scene);
    }

    @Override
    public void recievedMove(int arg0, char arg1) {
        myBoard.updateUserBoard((int)(arg1)-97, arg0);
        if(myBoard.getValue((int)(arg1)-97, arg0) == 'H'){
            battleshipLib.respondToMove("hit");
            addCircle((int)(arg1)-97 + 1, arg0 + 1, Color.RED);
        } else {
            battleshipLib.respondToMove("miss");
            addCircle((int)(arg1)-97 + 1, arg0 + 1, Color.GREEN);
        }
    }
    public void addCircle(int x, int y, Color color){
        Circle circle = new Circle();
        circle.setRadius(15);
        circle.setFill(color);
        pane.add(circle, x, y);
    }
}