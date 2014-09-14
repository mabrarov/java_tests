package ru.abrarov.javatests.topbuilder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.junit.Assert.assertTrue;

public class NaiveFrequencyAnalyzerIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(NaiveFrequencyAnalyzerIntegrationTest.class);

    /**
     * Name of current running test
     */
    @Rule
    public TestName testName = new TestName();

    @Before
    public void init() {
        //printTestHeader();
    }


    @Test
    public void testClassPath() {
        final URL frequencyAnalyzerLocation = FrequencyAnalyzer.class.getProtectionDomain().getCodeSource().getLocation();
        assertTrue(frequencyAnalyzerLocation.getFile().endsWith(".jar"));
        logger.info(frequencyAnalyzerLocation.toString());
    }


}
