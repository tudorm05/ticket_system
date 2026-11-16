package main.users;

import main.fileio.UserInput;

public final class ReporterCreator extends UserFactory {
    @Override
    public User createUser(final UserInput userInput) {
        return new Reporter(userInput);
    }
}
