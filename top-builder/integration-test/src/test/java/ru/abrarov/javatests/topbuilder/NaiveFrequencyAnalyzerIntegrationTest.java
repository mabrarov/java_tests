package ru.abrarov.javatests.topbuilder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.info(FrequencyAnalyzer.class.getProtectionDomain().getCodeSource().getLocation().toString());
    }


}
