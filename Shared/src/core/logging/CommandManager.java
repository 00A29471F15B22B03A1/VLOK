package core.logging;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private static Map<String, AbstractConsoleCommand> commands = new HashMap<>();

    static {
        registerCommand(new ClearCommand());
        registerCommand(new StopCommand());
    }

    public static void registerCommand(AbstractConsoleCommand consoleCommand) {
        if (commands.containsKey(consoleCommand.getCommandName())) {
            Console.err("Command with name " + consoleCommand.getCommandName() + " is already registered");
            return;
        }

        commands.put(consoleCommand.getCommandName(), consoleCommand);
    }

    public static void handleCommand(String input) {
        input = input.trim();
        if (!input.startsWith("/")) {
            Console.err(input + " is not a command");
            return;
        }

        String[] words = input.split(" ");
        String commandName = words[0].replaceFirst("/", "");
        AbstractConsoleCommand command = commands.get(commandName);

        if (command == null) {
            Console.err(commandName + " is not a command");
            return;
        }

        command.handleCommand(input.replaceFirst("/" + commandName, "").split(" "));
    }

}