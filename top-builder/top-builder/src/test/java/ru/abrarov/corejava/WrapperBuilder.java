package ru.abrarov.corejava;

public interface WrapperBuilder<T> {

    Wrapper<T> createWrapper(final T object, final LeakageToken<T> leakageToken);

}
