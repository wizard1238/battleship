package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.geometry.HPos;
import javafx.scene.input.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;


public class Game extends Application
{
    @Override
    public void start(Stage stage)
    {
        Board myBoard = new Board();
        Board enemyBoard = new Board();
        
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
        
        AtomicInteger locationX = new AtomicInteger(-1);
        AtomicInteger locationY = new AtomicInteger(-1);
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
        GridPane pane = new GridPane();
        
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
                    counter.incrementAndGet();
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
                    counter.incrementAndGet();
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
                    counter.incrementAndGet();
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
                    counter.incrementAndGet();
                } else {
                    System.out.println("Invalid placement");
                }
                directionPopup.hide();
            }
        });
        
        Popup hitPopup = new Popup();
        hitPopup.setX(visualBounds.getWidth()/2-150);
        hitPopup.setY(visualBounds.getHeight()/2-150);
        
        Rectangle hitPopupRect = new Rectangle(300, 300);
        hitPopupRect.setFill(Color.WHITE);
        
        Button hitButton = new Button("Hit");
        hitButton.setLayoutX(10);
        hitButton.setLayoutY(140);
        
        Button missButton = new Button("Miss");
        missButton.setLayoutX(130);
        missButton.setLayoutY(140);
        
        Button sinkButton = new Button("Sink");
        sinkButton.setLayoutX(250);
        sinkButton.setLayoutY(140);
        
        hitPopup.getContent().addAll(hitPopupRect);
        hitPopup.getContent().addAll(hitButton);
        hitPopup.getContent().addAll(missButton);
        hitPopup.getContent().addAll(sinkButton);
        hitPopup.hide();
        
        hitButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                enemyBoard.updatePos(locationY.get(), locationX.get(), 'H');
                Circle circle = new Circle();
                circle.setRadius(15);
                circle.setFill(Color.RED);
                pane.add(circle, locationX.get()+12, locationY.get()+1);
                hitPopup.hide();
            }
        });
        
        missButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                enemyBoard.updatePos(locationY.get(), locationX.get(), 'M');
                Circle circle = new Circle();
                circle.setRadius(15);
                circle.setFill(Color.GREEN);
                pane.add(circle, locationX.get()+12, locationY.get()+1);
                hitPopup.hide();
            }
        });
        
        sinkButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                enemyBoard.updatePos(locationY.get(), locationX.get(), 'S');
                Circle circle = new Circle();
                circle.setRadius(15);
                circle.setFill(Color.YELLOW);
                pane.add(circle, locationX.get()+12, locationY.get()+1);
                hitPopup.hide();
            }
        });
        
        pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            int x = ((int)(event.getSceneX())/40);
            int y = ((int)(event.getSceneY())/40);
            System.out.println("X: " + x + " Y: " + y);
            if(x >= 1 && x <= 9 && y >= 1 && y <= 11){
                int count = counter.get();
                if(count < 5){
                    locationX.set(x-1);
                    locationY.set(y-1);
                    directionPopup.show(stage);
                } else {
                   Circle circle = new Circle();
                   circle.setRadius(15);
                   myBoard.updateUserBoard(y-1, x-1);
                   if(myBoard.getValue(y-1, x-1) == 'H'){
                       circle.setFill(Color.RED);
                   } else {
                       circle.setFill(Color.GREEN);
                   }
                   pane.add(circle, x, y);
                }
            } else if(x >= 12 && x <= 20 && y >= 1 && y <= 11){
                locationX.set(x-12);
                locationY.set(y-1);
                hitPopup.show(stage);
            }
        });
        
        Scene scene = new Scene(pane,880,520);
        stage.setTitle("Battleship");
        stage.setScene(scene);
        stage.show();
    }
}