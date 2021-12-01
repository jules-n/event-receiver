package com.ynero.ss.event_receiver.states;

public interface ConnectionState<T> {

    T access(IContext context);

    default void onEnter(IContext context) {
    }
}
