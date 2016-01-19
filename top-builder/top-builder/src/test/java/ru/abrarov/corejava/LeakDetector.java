package ru.abrarov.corejava;

import com.google.common.util.concurrent.MoreExecutors;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeakDetector<T> implements Pool<T>, AutoCloseable {

    private final LeakDetectionSupportPool<T> pool;
    private final WrapperBuilder<T> wrapperBuilder;
    private final Class<T> type;
    private final Class<? extends Wrapper<T>> wrapperType;
    private final ReferenceQueue<T> weakReferenceQueue;
    private final Map<Reference<? extends T>, T> weakReferenceMap;
    private final ScheduledExecutorService executorService;

    public LeakDetector(final LeakDetectionSupportPool<T> pool, final WrapperBuilder<T> wrapperBuilder, final Class<T> type, final Class<? extends Wrapper<T>> wrapperType) {
        if (!type.isAssignableFrom(wrapperType)) {
            throw new IllegalArgumentException("wrapperType should be able to be casted to type");
        }
        this.pool = pool;
        this.wrapperBuilder = wrapperBuilder;
        this.type = type;
        this.wrapperType = wrapperType;
        this.weakReferenceQueue = new ReferenceQueue<>();
        this.weakReferenceMap = Collections.synchronizedMap(new WeakHashMap<Reference<? extends T>, T>());
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        final int delay = 100;

        scheduleWeakReferenceCheck(executorService, new Runnable() {
            @Override
            public void run() {
                try {
                    final Reference<? extends T> reference = weakReferenceQueue.poll();
                    if (reference != null) {
                        final T object = weakReferenceMap.remove(reference);
                        if (object != null) {
                            pool.returnLeakedObject(object);
                        }
                    }
                } finally {
                    scheduleWeakReferenceCheck(LeakDetector.this.executorService, this, delay);
                }
            }

        }, delay);
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

    @Override
    public void close() throws Exception {
        MoreExecutors.shutdownAndAwaitTermination(executorService, 60, TimeUnit.SECONDS);
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

    private static void scheduleWeakReferenceCheck(final ScheduledExecutorService executorService, final Runnable runnable, final long delay) {
        executorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

}
