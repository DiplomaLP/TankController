package TCPClient;

public class CommandCloseConnection extends Command {

    @Override
    public String getCommand() {
        return Commands.CLOSE_CONNECTION + ";";
    }
}
