package TCPClient;

import android.support.annotation.Nullable;

public class CommandFactory {
    @Nullable
    public static Command CreateCommand(String command, int value) throws Exception {

        if (command.equals(Commands.MOVE_LEFT) || command.equals(Commands.MOVE_RIGHT)) {
            return new CommandMove(command, value);
        } else if (command.equals(Commands.MOVE_CAMERA)) {
            return new CommandMoveCamera(value);
        } else if (command.equals(Commands.LIGHT_TURN)) {
            return new CommandLight(value);
        }

        return null;
    }

    @Nullable
    public static Command CreateCommand(String command) {
        if (command.equals(Commands.CLOSE_CONNECTION)) {
            return new CommandCloseConnection();
        } else if (command.equals(Commands.CONNECTED_SUCCESSFULLY)) {
            return new CommandConnectedSuccessfully();
        } else if (command.equals(Commands.ALREADY_CONNECTED)) {
            return new CommandAlreadyConnected();
        }

        return null;
    }
}
