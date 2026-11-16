package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.commands.Command;
import main.commands.CommandCreator;
import main.database.Database;
import main.fileio.CommandInput;
import main.fileio.UserInput;
import main.ticket.Ticket;
import main.users.User;
import main.users.UserFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static main.database.Database.updateBusinessPriority;
import static main.database.Database.updateMilestones;
import static main.users.UserFactory.obtainCreator;

/**
 * main.App represents the main application logic that processes input commands,
 * generates outputs, and writes them to a file
 */
public final class App {
    private App() {
    }

    private static final String INPUT_USERS_FIELD = "input/database/users.json";

    private static final ObjectWriter WRITER =
            new ObjectMapper().writer().withDefaultPrettyPrinter();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * Runs the application: reads commands from an input file,
     * processes them, generates results, and writes them to an output file
     *
     * @param inputPath path to the input file containing commands
     * @param outputPath path to the file where results should be written
     */
    public static void run(final String inputPath, final String outputPath) {
        // feel free to change this if needed
        // however keep 'outputs' variable name to be used for writing
        Database.getInstance().getTickets().clear();
        Database.getInstance().getMilestones().clear();
        Database.getInstance().setStartingDate(null);
        Ticket.resetIdCounter();
        List<ObjectNode> outputs = new ArrayList<>();
        CommandInput[] inputs = null;

        /*
            TODO 1 :
            Load initial user data and commands. we strongly recommend using jackson library.
            you can use the reading from hw1 as a reference.
            however you can use some of the more advanced features of
            jackson library, available here: https://www.baeldung.com/jackson-annotations
        */
        // TODO 2: process commands.

        // TODO 3: create objectnodes for output, add them to outputs list.
        UserInput[] userInputs = null;
        try {
            userInputs = MAPPER.readValue(new File(INPUT_USERS_FIELD), UserInput[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<User> users = new ArrayList<>();
        for (UserInput userInput: userInputs) {
            UserFactory userFactory = obtainCreator(userInput.getRole());
            User user = userFactory.createUser(userInput);
            users.add(user);
        }
        Database.getInstance().setUsers(users);
        try {
            inputs = MAPPER.readValue(new File(inputPath), CommandInput[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (CommandInput currentCommand : inputs) {
            String commandName = currentCommand.getCommand();
            if ("lostInvestors".equals(commandName)) {
                break;
            }
            updateBusinessPriority(currentCommand);
            Command command = CommandCreator.createCommand(commandName, MAPPER);
            command.execute(currentCommand, outputs);
            updateMilestones(currentCommand.getTimestamp());
        }
        // DO NOT CHANGE THIS SECTION IN ANY WAY
        try {
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();
            WRITER.withDefaultPrettyPrinter().writeValue(outputFile, outputs);
        } catch (IOException e) {
            System.out.println("error writing to output file: " + e.getMessage());
        }
    }
}
