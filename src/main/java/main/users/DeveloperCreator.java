package main.users;

import main.fileio.UserInput;

public final class DeveloperCreator extends UserFactory {

    @Override
    public User createUser(final UserInput userInput) {
        return new Developer(userInput);
    }
}
