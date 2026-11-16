package main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.fileio.CommandInput;
import main.users.Developer;
import main.users.User;

import java.util.List;

public final class ViewNotifications extends Command {
    private final ObjectMapper mapper;
    public ViewNotifications(final ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void execute(final CommandInput command, final List<ObjectNode> outputs) {
        ObjectNode node = createNode(mapper, command);
        User user = Database.getInstance().findUser(command.getUsername());
        Developer developer = (Developer) user;
        ArrayNode notificationsNode = mapper.createArrayNode();
        for (String currentNotification : developer.getNotifications()) {
            notificationsNode.add(currentNotification);
        }
        node.set("notifications", notificationsNode);
        outputs.add(node);
        developer.clearNotifications();
    }
}
