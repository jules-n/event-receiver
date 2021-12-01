package com.ynero.ss.event_receiver.states;

@FunctionalInterface
public interface IContext<T> {
    void setState(ConnectionState<T> state);
}
