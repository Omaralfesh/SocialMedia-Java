import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT_NUMBER = 44441;
    private ServerSocket serverSocket;

    public Server() {
        JabberDatabase db = new JabberDatabase();
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            serverSocket.setReuseAddress(true);
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("A thread started");
                new Thread(new ClientHandler(clientSocket,db)).start();

            } catch (IOException exception) {
                System.out.println("Exception encountered on accept");
                exception.printStackTrace();
            }

        }






    }


    public static void main(String[] args) {

        new Server();

    }







}
