package com.ynero.ss.event_receiver.states;

public interface ConnectionState<T> extends AutoCloseable{

    T access(IContext context);

    default void onEnter(IContext context) {
    }
}
