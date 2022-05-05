import com.bham.fsd.assignments.jabberserver.JabberMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    Socket socket;
    JabberDatabase db;
    ObjectInputStream inFromClient;
    ObjectOutputStream outToClient;



    public ClientHandler(Socket socket, JabberDatabase db){
    this.socket = socket;
    this.db = db;

    }


    @Override
    public void run() {
        boolean serverOn = true;
        System.out.println("connection established");
        try {
            JabberMessage jabberToClient = null;
            String currentUser = "";
            int currentUserID = -1;
            inFromClient = new ObjectInputStream(socket.getInputStream());
            outToClient = new ObjectOutputStream(socket.getOutputStream());
            while (serverOn) {
                String toClient = "";
                JabberMessage jabberFromClient = (JabberMessage) inFromClient.readObject();
                String[] fromClientArray = jabberFromClient.getMessage().split(" ");
                switch (fromClientArray[0]) {
                    case "signin": {
                        currentUser = fromClientArray[1];
                        currentUserID = db.getUserID(currentUser);
                        System.out.println(currentUserID);
                        if (currentUserID != -1) {
                            toClient = "signedin";
                        } else {
                            toClient = "unknown-user";
                        }
                        jabberToClient = new JabberMessage(toClient);
                        break;
                    }

                    case "register":{
                        currentUser = fromClientArray[1];
                        currentUserID = db.getUserID(currentUser);
                        int isUser = db.getUserID(currentUser);
                        System.out.println(isUser);
                        if(isUser == -1) {
                            String email = currentUser + "@gmail.com";
                            db.addUser(currentUser, email);
                            toClient = "signedin";

                        }else{
                            toClient = "exists";
                        }
                        jabberToClient = new JabberMessage(toClient);
                        break;

                    }

                    case "signout": {
                        serverOn = false;
                        break;
                    }

                    case "timeline":{
                        System.out.println(currentUser);
                        toClient = "timeline";
                        ArrayList<ArrayList<String>> data = db.getTimelineOfUserEx(currentUserID);
                        jabberToClient = new JabberMessage(toClient,data);
                        break;
                    }

                    case "users":{
                        toClient = "users";
                        ArrayList<ArrayList<String>> data = db.getUsersNotFollowed(currentUserID);
                        jabberToClient = new JabberMessage(toClient,data);
                        break;
                    }

                    case "post":{

                        String post = jabberFromClient.getMessage().substring(5);
                        db.addJab(currentUser,post);
                        toClient = "posted";
                        jabberToClient = new JabberMessage(toClient);
                        break;
                    }

                    case "like":{
                        int jabid = Integer.parseInt(fromClientArray[1]);
                        db.addLike(currentUserID,jabid);
//                        toClient = "posted";
//                        jabberToClient = new JabberMessage(toClient);
                        break;
                    }

                    case "follow":{
                        String userToFollow = fromClientArray[1];
                        db.addFollower(currentUserID,userToFollow);
                        toClient = "posted";
                        jabberToClient = new JabberMessage(toClient);
                        break;
                    }

                }
                    outToClient.writeObject(jabberToClient);
                    outToClient.flush();
            }


        } catch (EOFException | ClassNotFoundException e) {
            System.err.print(e);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                outToClient.close();
                inFromClient.close();
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }

    }
}
