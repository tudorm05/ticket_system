package main.users;

import main.fileio.UserInput;

public final class ManagerCreator extends UserFactory {
    @Override
    public User createUser(final UserInput userInput) {
        return new Manager(userInput);
    }
}
