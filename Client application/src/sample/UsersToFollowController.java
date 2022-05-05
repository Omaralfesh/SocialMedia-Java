package sample;

import com.bham.fsd.assignments.jabberserver.JabberMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class UsersToFollowController implements Initializable {

    @FXML
    private Label username;


    @FXML
    private Button followButton;

    public void setData(String user){
        username.setText(user);
    }

    public void setFollowButton(ObjectOutputStream outputStream){

        followButton.setOnAction(event -> {
            String message = "follow "+username.getText();
            JabberMessage jm = new JabberMessage(message);
            followButton.setDisable(true);
            try {
                outputStream.writeObject(jm);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
