package core.logging;

public abstract class AbstractConsoleCommand {

    private String commandName;

    public AbstractConsoleCommand(String commandName) {
        this.commandName = commandName;
    }

    public abstract void handleCommand(String[] args);

    public String getCommandName() {
        return commandName;
    }
}
