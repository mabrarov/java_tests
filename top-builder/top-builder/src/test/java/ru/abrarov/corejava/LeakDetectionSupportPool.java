package ru.abrarov.corejava;

public interface LeakDetectionSupportPool<T> extends Pool<T> {

    void returnLeakedObject(final T object);

}
