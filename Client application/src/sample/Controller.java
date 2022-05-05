package sample;

import com.bham.fsd.assignments.jabberserver.JabberMessage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller {
    Socket clientSocket;
    ObjectInputStream inFromServer;
    ObjectOutputStream outToServer;
    JabberMessage serverMessage = null;
    JabberMessage clientMessage = null;
    String currentUser = null;
    int currentUserID = 0;


    @FXML private Label status;
    @FXML private TextField T1;





    @FXML
    public void register(ActionEvent event) throws IOException {
        String username = T1.getText();
        String message = "register " + username;
        clientMessage = new JabberMessage(message);
        try {
            outToServer.writeObject(clientMessage);
            serverMessage = (JabberMessage) inFromServer.readObject();
        } catch (IOException | ClassNotFoundException exception ) {
            exception.printStackTrace();
        }

        if(serverMessage.getMessage().equals("signedin")) {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Jabber Platfrom");
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("user1.fxml").openStream());
            HBox hBox = new HBox();
            VBox vBox = new VBox();
            Label timeline = new Label("Timeline");
            timeline.setFont(Font.font("Aerial",35));
            timeline.setMinWidth(200);
            TextField text= new TextField();
            text.setPromptText("What's in your mind ?");
            text.setFocusTraversable(false);
            text.setFont(Font.font("Aerial",24));
            text.setMinWidth(400);
            Button button = new Button("Post");
            button.setMinWidth(100);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String newJab = "post " + text.getText();
                    clientMessage = new JabberMessage(newJab);

                    try {
                        outToServer.writeObject(clientMessage);
                        serverMessage = (JabberMessage) inFromServer.readObject();
                    } catch (IOException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            Button signout = new Button("Sign Out");
            signout.setMinWidth(100);
            signout.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        signout(event);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            vBox.getChildren().addAll(timeline,text,button);
            hBox.setSpacing(200);
            Label lbl = new Label("Users to follow");
            lbl.setFont(Font.font("Aerial",24));
            Region region = new Region();
            region.setPrefSize(200,200);
            vBox.setPadding(new Insets(50,100,50,50));
            VBox vBox1 = new VBox();
            vBox1.setPadding(new Insets(70));
            vBox1.getChildren().addAll(signout,lbl);
            hBox.getChildren().addAll(vBox,region,vBox1);
            vBox.setSpacing(50);
            showTimeline(vBox);
            getUserToFollow(vBox1);
            root.getChildren().addAll(hBox);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else{
            status.setText("User already exists");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("User already exists");
            alert.showAndWait();
        }

    }

    @FXML
    public void signin(ActionEvent event) throws IOException {
        String username = T1.getText();

        if(T1.getText().isEmpty())
            username = "x";
        String message = "signin " +username;
        clientMessage = new JabberMessage(message);
        try {
            outToServer.writeObject(clientMessage);
            serverMessage = (JabberMessage) inFromServer.readObject();

        } catch (IOException | ClassNotFoundException exception ) {
            exception.printStackTrace();
        }

        if(serverMessage.getMessage().equals("signedin")) {
            currentUser = username;

            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Jabber Platfrom");
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("user1.fxml").openStream());
            HBox hBox = new HBox();
            VBox vBox = new VBox();
            Label timeline = new Label("Timeline");
            timeline.setFont(Font.font("Aerial", 35));
            timeline.setMinWidth(200);
            TextField text = new TextField();
            text.setPromptText("What's in your mind ?");
            text.setFocusTraversable(false);
            text.setFont(Font.font("Aerial", 24));
            text.setMinWidth(400);
            Button button = new Button("Post");
            button.setMinWidth(100);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String newJab = "post " + text.getText();
                    clientMessage = new JabberMessage(newJab);

                    try {
                        outToServer.writeObject(clientMessage);
                        serverMessage = (JabberMessage) inFromServer.readObject();
                    } catch (IOException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            Button signout = new Button("Sign Out");
            signout.setMinWidth(100);
            signout.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        signout(event);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            vBox.getChildren().addAll(timeline, text, button);
            hBox.setSpacing(200);
            Label lbl = new Label("Users to follow");
            lbl.setFont(Font.font("Aerial", 24));
            Region region = new Region();
            region.setPrefSize(200, 200);
            vBox.setPadding(new Insets(50, 100, 50, 50));
            VBox vBox1 = new VBox();
            vBox1.setPadding(new Insets(70));
            vBox1.getChildren().addAll(signout, lbl);
            hBox.getChildren().addAll(vBox, region, vBox1);
            vBox.setSpacing(50);
            showTimeline(vBox);
            getUserToFollow(vBox1);
            root.getChildren().addAll(hBox);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            status.setText("InCorrect user");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Username does not exist");
//            alert.setContentText("Ooops, there was an error!");

            alert.showAndWait();

        }


    }


    @FXML
    public void signout(ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("sample.fxml").openStream());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        String message = "signout";
        clientMessage = new JabberMessage(message);
        try {
            outToServer.writeObject(clientMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }


    }

    @FXML
    public void addJab(TextField textField){



    }

    @FXML
    public void getUserToFollow(Pane root){
        String message = "users";
        clientMessage = new JabberMessage(message);
        try {
            outToServer.writeObject(clientMessage);
            serverMessage = (JabberMessage) inFromServer.readObject();
            VBox uservBox = new VBox();
            uservBox.setPadding(new Insets(40,20,10,10));


            for(ArrayList<String> oneUser : serverMessage.getData()){
                String user = oneUser.get(0);
                FXMLLoader fxmlLoader1 = new FXMLLoader();
                fxmlLoader1.setLocation(getClass().getResource("usersToFollow.fxml"));
                HBox hBox = fxmlLoader1.load();
                UsersToFollowController usersToFollowController = fxmlLoader1.getController();
                usersToFollowController.setData(user);
                usersToFollowController.setFollowButton(outToServer);
                uservBox.getChildren().addAll(hBox);
            }

            root.getChildren().addAll(uservBox);


        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }


    }

    @FXML
    private Label Username;

    @FXML private VBox vBoxLayout;

    @FXML
    public void showTimeline(Pane root){
        String message = "timeline";
        clientMessage = new JabberMessage(message);
        try {
            outToServer.writeObject(clientMessage);
            serverMessage = (JabberMessage) inFromServer.readObject();
            VBox vBox = new VBox();
            for(ArrayList<String> oneJab : serverMessage.getData()) {
                String userJab = oneJab.get(0) + ": " + oneJab.get(1);
                String numLikes = oneJab.get(3);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("jab.fxml"));
                HBox hBox = fxmlLoader.load();
                JabController jabController = fxmlLoader.getController();
                jabController.setData(userJab, numLikes);
                jabController.setLikeButton(outToServer, oneJab.get(3));
                vBox.getChildren().addAll(hBox);
            }


            root.getChildren().addAll(vBox);


        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        }


    public Controller() throws IOException {
        clientSocket = new Socket("localhost",44441);
        try {
            outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }











}
