package TCPClient;

public class CommandFactory {
    public static Command CreateCommand(String command, int move) throws Exception {

        if (command.equals(Commands.MOVE_LEFT) || command.equals(Commands.MOVE_RIGHT)) {
            return new CommandMove(command, move);
        }

        return null;
    }
}
