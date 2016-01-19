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

}
