package sample;

import com.bham.fsd.assignments.jabberserver.JabberMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class JabController implements Initializable {

    @FXML
    private Label userJab;

    @FXML
    private Label numLikesLabel;

    @FXML
    private Button likeButton;


    public void setData(String user,String numLikes){
        userJab.setText(user);
        this.numLikesLabel.setText(numLikes);

    }

    public void setLikeButton(ObjectOutputStream outputStream, String jabid){


        likeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int number = Integer.parseInt(numLikesLabel.getText());
                String newNumber = String.valueOf(number+1);
                numLikesLabel.setText(newNumber);
                String message = "like "+jabid;
                JabberMessage jm = new JabberMessage(message);
                likeButton.setDisable(true);
                try {
                    outputStream.writeObject(jm);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
