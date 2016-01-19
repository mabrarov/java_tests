package ru.abrarov.corejava;

public interface Wrapper<T> {

    T getWrappedObject();

    LeakageToken<T> getLeakageToken();

}
