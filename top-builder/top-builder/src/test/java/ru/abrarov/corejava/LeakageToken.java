package ru.abrarov.corejava;

import java.lang.ref.WeakReference;

public interface LeakageToken<T> {

    WeakReference<LeakageToken<T>> getWeakReference();

}
