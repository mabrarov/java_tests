package ru.abrarov.corejava;

import java.lang.ref.WeakReference;

public interface LeakageToken<T> {

    WeakReference<T> getWeakReference();

}
