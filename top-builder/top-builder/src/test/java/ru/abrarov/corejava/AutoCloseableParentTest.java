package ru.abrarov.corejava;

import com.google.common.base.Throwables;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AutoCloseableParentTest {

    public static class AutoCloseableGuard<T extends AutoCloseable> implements AutoCloseable {

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

    public static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }

    private static class Resource implements AutoCloseable {

        private static final Logger logger = LoggerFactory.getLogger(Resource.class);
        private boolean closed;

        public Resource() {
        }

        public void open() {
            closed = false;
        }

        @Override
        public void close() throws Exception {
            logger.info("Resource#close()");
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }

    private static class Parent implements AutoCloseable {

        private static final Logger logger = LoggerFactory.getLogger(Parent.class);
        private final Resource resource;
        private boolean closed;

        public Parent(final Resource resource, final boolean throwException) throws Exception {
            logger.info("Parent constructor");
            this.resource = resource;
            try (final AutoCloseableGuard<Resource> resourceGuard = new AutoCloseableGuard<>(resource)) {
                resource.open();
                if (throwException) {
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

    private static class Derived extends Parent {

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

        public Derived(final Resource resource, final boolean throwParentException, final boolean throwDerivedException) throws Exception {
            super(resource, throwParentException);
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

    @Test
    public void testWithoutExceptions() throws Exception {
        final Resource resource = new Resource();
        try (final Derived ignored = new Derived(resource, false, false)) {
            assertFalse(resource.isClosed());
        }
        assertTrue(resource.isClosed());
    }

    @Test
    public void testExceptionInParentConstructor() throws Exception {
        final Resource resource = new Resource();
        try {
            try (final Derived ignored = new Derived(resource, true, false)) {
                assertFalse(resource.isClosed());
            }
        } catch (final TestException ignored) {
            // This exception is expected
        } finally {
            assertTrue(resource.isClosed());
        }
    }

    @Test
    public void testExceptionInDerivedConstructor() throws Exception {
        final Resource resource = new Resource();
        try {
            try (final Derived ignored = new Derived(resource, false, true)) {
                assertFalse(resource.isClosed());
            }
        } catch (final TestException ignored) {
            // This exception is expected
        } finally {
            assertTrue(resource.isClosed());
        }
    }
}
