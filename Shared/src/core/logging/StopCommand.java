package core.logging;

public class StopCommand extends AbstractConsoleCommand {

    public StopCommand() {
        super("stop");
    }

    @Override
    public void handleCommand(String[] args) {
        System.exit(0);
    }
}
