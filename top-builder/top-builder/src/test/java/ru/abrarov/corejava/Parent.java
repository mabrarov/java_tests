package ru.abrarov.corejava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parent implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Parent.class);
    private final Resource resource;
    private final boolean throwExceptionInClose;
    private boolean closed;

    public Parent(final Resource resource, final boolean throwExceptionInConstructor, final boolean throwExceptionInClose) throws Exception {
        logger.info("Parent constructor");
        this.resource = resource;
        this.throwExceptionInClose = throwExceptionInClose;
        try (final AutoCloseableGuard<Resource> resourceGuard = new AutoCloseableGuard<>(resource)) {
            resource.open();
            if (throwExceptionInConstructor) {
                throw new TestException("Parent constructor exception");
            }
            resourceGuard.release();
        }
    }

    @Override
    public void close() throws Exception {
        try (final AutoCloseable ignored = resource) {
            // Parent close logic goes here
            logger.info("Parent#close()");
            if (throwExceptionInClose) {
                throw new TestException("Parent close exception");
            }
            closed = true;
        }
    }

    public boolean isParentClosed() {
        return closed;
    }

    public boolean isClosed() {
        return closed;
    }
}
