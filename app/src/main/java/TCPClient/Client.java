package TCPClient;


import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class Client {

    public static final int OK = 0;
    public static final int ERROR = -1;
    public static final int ALREADY_CONNECTED = -2;

    SendDataAsync sendDataAsync;

    private Socket clientSocket;
    private DataOutputStream outToServer;
    private DataInputStream inFromServer;

    public Client(){
    }

    public int connect(String ipAddress, int port) {
        try {
            clientSocket = new Socket(ipAddress, port);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }

        Command command = recvCommand();

        System.err.print("The command after connect = " + ((command == null)? "null\n" : (command.getCommand() + "\n")));

        if (command == null) {
            return ERROR;
        }

        if (command.getCommand().equals(Commands.CONNECTED_SUCCESSFULLY)) {
            return OK;
        }
        if (command.getCommand().equals(Commands.ALREADY_CONNECTED)) {
            return ALREADY_CONNECTED;
        }

        return ERROR;
    }

    public void disconnect() {
        try {
            sendCommand(CommandFactory.CreateCommand(Commands.CLOSE_CONNECTION), true);

            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!clientSocket.isClosed()) {
            disconnect();
        }
    }

    public Command recvCommand() {
        byte []data = new byte[1024];
        String str = "";

        int read_size;
        try {
            read_size = inFromServer.read(data, 0, data.length);
            for (int i = 0; i < read_size; i++) {
                str += (char) data[i];
            }
            System.err.print("Get from server: " + str + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CommandFactory.CreateCommand(str);
    }

    public void sendCommand(Command command, boolean wait) {
        sendDataAsync = new SendDataAsync();
        sendDataAsync.execute(command);
        if (wait) {
            try {
                sendDataAsync.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
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
