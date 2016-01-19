package ru.abrarov.corejava;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class LeakDetector<T> implements Pool<T> {

    private final Pool<T> pool;
    private final WrapperBuilder<T> wrapperBuilder;
    private final Class<T> type;
    private final Class<? extends Wrapper<T>> wrapperType;
    private final ReferenceQueue<T> weakReferenceQueue;
    private final Map<WeakReference<T>, T> weakReferenceMap;

    public LeakDetector(final Pool<T> pool, final WrapperBuilder<T> wrapperBuilder, final Class<T> type, final Class<? extends Wrapper<T>> wrapperType) {
        if (!type.isAssignableFrom(wrapperType)) {
            throw new IllegalArgumentException("wrapperType should be able to be casted to type");
        }
        this.pool = pool;
        this.wrapperBuilder = wrapperBuilder;
        this.type = type;
        this.wrapperType = wrapperType;
        this.weakReferenceQueue = new ReferenceQueue<>();
        this.weakReferenceMap = Collections.synchronizedMap(new WeakHashMap<WeakReference<T>, T>());
    }

    @Override
    public T borrowObject() {
        final T object = pool.borrowObject();
        final WeakReference<T> weakReference = new WeakReference<>(object, weakReferenceQueue);
        final LeakageToken<T> leakageToken = new LeakageTokenImpl<>(weakReference);
        weakReferenceMap.put(weakReference, object);
        final Wrapper<T> wrapper = wrapperBuilder.createWrapper(object, leakageToken);
        return type.cast(wrapper);
    }

    @Override
    public void returnObject(final T wrappedObject) {
        final Wrapper<T> wrapper = wrapperType.cast(wrappedObject);
        final LeakageToken<T> leakageToken = wrapper.getLeakageToken();
        weakReferenceMap.remove(leakageToken.getWeakReference());
        pool.returnObject(wrapper.getWrappedObject());
    }

    private static class LeakageTokenImpl<T> implements LeakageToken<T> {

        private final WeakReference<T> weakReference;

        public LeakageTokenImpl(final WeakReference<T> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public WeakReference<T> getWeakReference() {
            return weakReference;
        }
    }

}
