package TCPClient;

public class CommandLight extends Command {

    private int onOff;

    public CommandLight(int on_off){
        this.onOff = on_off;
    }

    @Override
    public String getCommand() {
        return Commands.LIGHT_TURN + ":" + Integer.toString(onOff) + ";";
    }
}
