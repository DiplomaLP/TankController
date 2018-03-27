package TCPClient;


public class CommandMove extends Command {
    private int move;

    public CommandMove(String command, int move) throws Exception {
        super(command);

        if (command != Commands.MOVE_LEFT && command != Commands.MOVE_RIGHT) {
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
