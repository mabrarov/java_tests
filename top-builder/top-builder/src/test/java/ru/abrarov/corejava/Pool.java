package ru.abrarov.corejava;

public interface Pool<T> {

    T borrowObject();

    void returnObject(final T object);

}
