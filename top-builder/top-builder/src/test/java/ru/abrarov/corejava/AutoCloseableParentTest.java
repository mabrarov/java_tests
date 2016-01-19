package ru.abrarov.corejava;

import org.junit.Test;

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
}
