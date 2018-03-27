package TCPClient;


public class Client {

    public static final int OK = 0;

    public Client(){

    }

    public int connect(String ipAddress, int port) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return OK;
    }

    public void disconnect() {

    }

    public void sendCommand(Command command) {

    }
}
