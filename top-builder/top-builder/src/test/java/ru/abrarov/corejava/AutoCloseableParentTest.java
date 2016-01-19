package ru.abrarov.corejava;

import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AutoCloseableParentTest {

    @Test
    public void testWithoutExceptions() throws Exception {
        final Resource resource = new Resource();
        try (final Derived ignored = new Derived(resource, false, false, false)) {
            assertFalse(resource.isClosed());
        }
        assertTrue(resource.isClosed());
    }

    @Test
    public void testExceptionInParentConstructor() throws Exception {
        final Resource resource = new Resource();
        try {
            try (final Derived ignored = new Derived(resource, true, false, false)) {
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
            try (final Derived ignored = new Derived(resource, false, false, true)) {
                assertFalse(resource.isClosed());
            }
        } catch (final TestException ignored) {
            // This exception is expected
        } finally {
            assertTrue(resource.isClosed());
        }
    }

    @Test
    public void testExceptionInParentClose() throws Exception {
        final Resource resource = new Resource();
        try {
            try (final Derived ignored = new Derived(resource, false, true, true)) {
                assertFalse(resource.isClosed());
            }
        } catch (final TestException ignored) {
            // This exception is expected
        } finally {
            assertTrue(resource.isClosed());
        }
    }

    @Test
    public void testWeakReferenceQueue() throws InterruptedException {
        final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        final WeakReference<Object> testReference = new WeakReference<>(new Object(), referenceQueue);
        while (true) {
            final Reference<?> reference = referenceQueue.poll();
            if (reference != null && reference.equals(testReference)) {
                break;
            }
            Thread.sleep(100);
            System.gc();
        }
    }

    public interface TestResource {

    }

    public static class TestResourceWrapper implements Wrapper<TestResource> {

        private final TestResource wrappedObject;
        private final LeakageToken<TestResource> leakageToken;

        public TestResourceWrapper(final TestResource wrappedObject, final LeakageToken<TestResource> leakageToken) {
            this.wrappedObject = wrappedObject;
            this.leakageToken = leakageToken;
        }

        @Override
        public TestResource getWrappedObject() {
            return wrappedObject;
        }

        @Override
        public LeakageToken<TestResource> getLeakageToken() {
            return leakageToken;
        }
    }

    public static class TestResourceWrapperBuilder implements WrapperBuilder<TestResource> {

        @Override
        public Wrapper<TestResource> createWrapper(final TestResource object, final LeakageToken<TestResource> leakageToken) {
            return new TestResourceWrapper(object, leakageToken);
        }
    }

}
