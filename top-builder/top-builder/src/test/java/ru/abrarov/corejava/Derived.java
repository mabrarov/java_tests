package ru.abrarov.corejava;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Derived extends Parent {

    private class ParentCloseGuard implements AutoCloseable {

        private final Logger logger = LoggerFactory.getLogger(ParentCloseGuard.class);

        public ParentCloseGuard() {
            logger.info("ParentCloseGuard constructor");
        }

        @Override
        public void close() throws Exception {
            logger.info("ParentCloseGuard#close()");
            Derived.super.close();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(Derived.class);
    private final ParentCloseGuard parentCloseGuard;
    private boolean closed;

    public Derived(final Resource resource, final boolean throwParentExceptionInConstructor, final boolean throwParentExceptionInClose, final boolean throwDerivedException) throws Exception {
        super(resource, throwParentExceptionInConstructor, throwParentExceptionInClose);
        try {
            logger.info("Derived constructor");
            if (throwDerivedException) {
                throw new TestException("Derived constructor exception");
            }
            parentCloseGuard = new ParentCloseGuard();
        } catch (final Throwable e) {
            try {
                super.close();
            } catch (final Throwable nested) {
                e.addSuppressed(nested);
            }
            Throwables.propagateIfInstanceOf(e, Exception.class);
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void close() throws Exception {
        try (final AutoCloseable ignored = this.parentCloseGuard) {
            // Derived close logic goes here
            logger.info("Derived#close()");
            closed = true;
        }
    }

    public boolean isDerivedClosed() {
        return closed;
    }

    @Override
    public boolean isClosed() {
        return super.isClosed() && isDerivedClosed();
    }
}
