package TCPClient;


import java.util.Objects;

public class CommandMove extends Command {
    private String command;
    private int move;

    public static int POWER_RATIO = 50;

    public CommandMove(String command, int move) throws Exception {
        this.command = command;

        if (!Objects.equals(command, Commands.MOVE_LEFT) && !Objects.equals(command, Commands.MOVE_RIGHT)) {
            throw new Exception("Bad command in!" + getClass().getName() + " , command = " + command);
        }

        this.move = move;
    }

    @Override
    public String getCommand() {
        String ret = command + ":" + move + ";";
        return ret;
    }
}
