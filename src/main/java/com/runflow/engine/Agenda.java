package com.runflow.engine;

public interface Agenda {

    boolean isEmpty();

    Runnable getNextOperation();

    void planOperation(Runnable operation);

}
