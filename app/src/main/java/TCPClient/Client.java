package TCPClient;


import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    public static final int OK = 0;
    public static final int ERROR = -1;

    SendDataAsync sendDataAsync;

    private Socket clientSocket;
    private DataOutputStream outToServer;

    public Client(){
    }

    public int connect(String ipAddress, int port) {
        try {
            clientSocket = new Socket(ipAddress, port);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ERROR;
        }

        return OK;
    }

    public void disconnect() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(Command command) {
        sendDataAsync = new SendDataAsync();
        sendDataAsync.execute(command);
    }

    class SendDataAsync extends AsyncTask<Command, Void, Void> {

        @Override
        protected Void doInBackground(Command... commands) {
            try {
                String str = commands[0].getCommand();
                byte[] bytes = str.getBytes();
                outToServer.write(bytes);
                outToServer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
