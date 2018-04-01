package TCPClient;

public class CommandAlreadyConnected extends Command {
    @Override
    public String getCommand() {
        return Commands.ALREADY_CONNECTED;
    }
}
