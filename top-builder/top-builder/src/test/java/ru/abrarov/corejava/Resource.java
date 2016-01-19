package ru.abrarov.corejava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource implements AutoCloseable {

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
        if (closed) {
            logger.warn("Is already closed");
        }
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
}
