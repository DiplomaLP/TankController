package TCPClient;

public abstract class Command {
    protected String command;

    public Command (String commandString) {
        command = commandString;
    }

    public abstract String getCommand();
}
