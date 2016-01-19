package ru.abrarov.corejava;

public class AutoCloseableGuard<T extends AutoCloseable> implements AutoCloseable {

    private T resource;

    public AutoCloseableGuard(final T resource) {
        this.resource = resource;
    }

    @Override
    public void close() throws Exception {
        if (resource != null) {
            resource.close();
            resource = null;
        }
    }

    public T release() {
        final T resource = this.resource;
        this.resource = null;
        return resource;
    }
}
