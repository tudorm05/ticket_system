package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.fileio.CommandInput;


import java.util.List;

public abstract class Command {
    /**
     *  metoda design pattern-ului
     * @param command commanda folosita
     * @param outputs output-ul programului
     */
    public abstract void execute(CommandInput command, List<ObjectNode> outputs);

    /**
     * metoda care se repeta in multe comenzi
     * creeaza nodul si adauga campurile comune
     * @param mapper mapperul pentru a pune in output
     * @param command comanda executata
     * @return
     */
    public ObjectNode createNode(final ObjectMapper mapper, final CommandInput command) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", command.getCommand());
        node.put("username", command.getUsername());
        node.put("timestamp", command.getTimestamp());
        return node;
    }
}
