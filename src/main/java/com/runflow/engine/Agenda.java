package com.runflow.engine;

public interface Agenda {

    boolean isEmpty();

    Runnable getNextOperation();

    /**
     * Generic method to plan a {@link Runnable}.
     */
    void planOperation(Runnable operation);

}
