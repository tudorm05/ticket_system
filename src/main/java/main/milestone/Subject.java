package main.milestone;

import main.users.Observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<Observer> observers = new ArrayList<>();

    /**
     * subject de la design pattern
     * adauga observerul in lista
     * @param observer observer-ul adaugay
     */
    public void addObserver(final Observer observer) {
        observers.add(observer);
    }

    /**
     * notifica toti observerii
     * @param notification notificarea trimisa
     */
    public void notifyObservers(final String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
