package core.logging;

public class ClearCommand extends AbstractConsoleCommand {

    public ClearCommand() {
        super("clear");
    }

    @Override
    public void handleCommand(String[] args) {
        Console.clear();
    }
}