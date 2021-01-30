package client;

import battleship.client.Battleship;
import battleship.client.BattleshipInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

// import java.beans.EventHandler;

public class javafxClient extends Application implements BattleshipInterface {
    String matchId;
    char receivedMoveCol; int receivedMoveRow;

    final int sceneWidth = 640;
    final int sceneHeight = 480;

    @Override
    public void start(Stage stage) throws Exception {
        // displayJoinpage(stage);
        Pane root = new Pane();
        
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();

        Text t = new Text(sceneWidth/2, sceneHeight/4, "Welcome to battleship!");
        t.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        t.setX(t.getX() - t.getLayoutBounds().getWidth() / 2);
        root.getChildren().add(t);

        //have watcher
        Battleship app = new Battleship(this);
        System.out.println(app.createNewGame());


        //Join game button
        Button join = new Button("Join");
        HBox hbJoin = new HBox(10);
        hbJoin.setAlignment(Pos.BOTTOM_CENTER);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                // displayJoinpage(stage);
            } 
        }; 
        hbJoin.getChildren().add(join);

        Button host = new Button("Host");
        EventHandler<ActionEvent> hostEvent = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                System.out.println(app.createNewGame());
            } 
        };

        
    }

    // public String displayJoinpage(final Stage owner){
    //     Stage stage = new Stage();
    //     stage.initOwner(owner);
    //     stage.setTitle("Join Game");

    //     GridPane grid = new GridPane();
    //     grid.setVgap(20);
    //     grid.setHgap(5);
    //     grid.setPadding(new Insets(25,10,10,25));

    //     //Join title
    //     Text joinTitle = new Text("Join Game:");
    //     joinTitle.setFont(Font.font("verdana", FontWeight.BOLD, 20));
    //     grid.add(joinTitle, 0, 0, 2, 1);

    //     //Enter join code
    //     Label joinCodeLabel = new Label("Enter your join code:");
    //     grid.add(joinCodeLabel, 0, 1);

    //     TextField joinField = new TextField();
    //     grid.add(joinField, 1, 1);

    //     //Button to join
    //     Button btn = new Button("Join!");
    //     HBox hbBtn = new HBox(10);
    //     hbBtn.setAlignment(Pos.CENTER_RIGHT);
    //     hbBtn.getChildren().add(btn);
    //     grid.add(hbBtn, 2, 1);

    //     //test if join code valid
    //     String joinCode = "";
    //     // btn.setOnAction((ActionEvent e) -> {
    //     //     // joinCode update
    //     //     stage.close();
    //     // });

    //     Scene scene = new Scene(grid, 640, 480);
    //     stage.setScene(scene);
    //     stage.showAndWait();

    //     return joinCode;
    // }
    
    public void matchJoined(String matchId) { //Lifecycle hook, will get called by library when join game
        this.matchId = matchId;
    }

    public void recievedMove(char col, int row) {
        System.out.println(col + row);
    }

    public void someClientMethod() {
        System.out.println("called");
    }

    public static void main(String[] args) throws Exception {
        // launch();

        // testing
        javafxClient c = new javafxClient();
        Battleship app = new Battleship(c);
        // System.out.println(app.createNewGame());
        String code = "ce288ea3-8b4a-40b5-b49e-ae33373b2b39";
        System.out.println(app.joinGame(code));
        // System.out.println(app.makeMove('c', 2));
        // app.joinGame("hello");
    }
}
