package TCPClient;

/**
 * Created by yura on 04.04.18.
 */

public class CommandMoveCamera extends Command {
    private int _progress;

    public CommandMoveCamera(int progress) {
        _progress = progress;
    }

    @Override
    public String getCommand() {
        return Commands.MOVE_CAMERA + ":" + _progress + ";";
    }
}
