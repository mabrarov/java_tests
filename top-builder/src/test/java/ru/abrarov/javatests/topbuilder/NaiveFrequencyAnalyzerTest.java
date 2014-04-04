package ru.abrarov.javatests.topbuilder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * NaiveFrequencyAnalyzer test.
 */
public class NaiveFrequencyAnalyzerTest {

    /**
     * Name of current running test
     */
    public TestName testName = new TestName();

    private FrequencyAnalyzer frequencyAnalyzer;

    @Before
    public void init() {
        printTestHeader();
        frequencyAnalyzer = new NaiveFrequencyAnalyzer();
    }

    @After
    public void shutdown() {
        frequencyAnalyzer = null;
    }

    /**
     * Test set contains values with no equal frequency.
     *
     * @see #buildSourceDataWithDifferentFrequencies
     * @see #buildExpectedResultForSourceDataWithDifferentFrequencies
     */
    @Test
    public void testValuesWithDifferentFrequencies() {
        final List<String> values = buildSourceDataWithDifferentFrequencies();
        final int listSize = 3;
        printSourceDataAndTestParams(values, listSize);

        final List<FrequencyAnalyzer.Item> expectedResult = buildExpectedResultForSourceDataWithDifferentFrequencies();
        printExpectedResult(expectedResult);

        final List<FrequencyAnalyzer.Item> actualResult = frequencyAnalyzer
                .buildTopFrequentList(values.iterator(), listSize);
        printActualResult(actualResult);

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test set contains some values with equal frequency.
     *
     * @see #buildSourceDataWithSomeEqualFrequencies
     * @see #buildExpectedResultForSourceDataWithSomeEqualFrequencies
     */
    @Test
    public void testValuesWithSomeEqualFrequencies() {
        final List<String> values = buildSourceDataWithSomeEqualFrequencies();
        final int listSize = 3;
        printSourceDataAndTestParams(values, listSize);

        final List<FrequencyAnalyzer.Item> expectedResult = buildExpectedResultForSourceDataWithSomeEqualFrequencies();
        printExpectedResult(expectedResult);

        final List<FrequencyAnalyzer.Item> actualResult = frequencyAnalyzer
                .buildTopFrequentList(values.iterator(), listSize);
        printActualResult(actualResult);

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test set contains less unique values than the specified top list size.
     *
     * @see #buildSourceDataWithFewUniqueValues
     * @see #buildExpectedResultForSourceDataWithFewUniqueValues
     */
    @Test
    public void testValuesWithFewUniqueValues() {
        final List<String> values = buildSourceDataWithFewUniqueValues();
        final int listSize = values.size() * 2;
        printSourceDataAndTestParams(values, listSize);

        final List<FrequencyAnalyzer.Item> expectedResult = buildExpectedResultForSourceDataWithFewUniqueValues();
        printExpectedResult(expectedResult);

        final List<FrequencyAnalyzer.Item> actualResult = frequencyAnalyzer
                .buildTopFrequentList(values.iterator(), listSize);
        printActualResult(actualResult);

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test set contains nulls.
     *
     * @see #buildSourceDataWithNulls
     * @see #buildExpectedResultForSourceDataWithNulls
     */
    @Test
    public void testValuesWithNulls() {
        final List<String> values = buildSourceDataWithNulls();
        final int listSize = 3;
        printSourceDataAndTestParams(values, listSize);

        final List<FrequencyAnalyzer.Item> expectedResult = buildExpectedResultForSourceDataWithNulls();
        printExpectedResult(expectedResult);

        final List<FrequencyAnalyzer.Item> actualResult = frequencyAnalyzer
                .buildTopFrequentList(values.iterator(), listSize);
        printActualResult(actualResult);

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test set is empty.
     */
    @Test
    public void testNoValues() {
        final List<String> values = Collections.emptyList();
        final int listSize = 3;
        printSourceDataAndTestParams(values, listSize);

        final List<FrequencyAnalyzer.Item> expectedResult = Collections
                .emptyList();
        printExpectedResult(expectedResult);

        final List<FrequencyAnalyzer.Item> actualResult = frequencyAnalyzer
                .buildTopFrequentList(values.iterator(), listSize);
        printActualResult(actualResult);

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test set is not empty and given list size is 0.
     */
    @Test
    public void testZeroListSize() {
        final List<String> values = buildSourceDataWithSomeEqualFrequencies();
        final int listSize = 0;
        printSourceDataAndTestParams(values, listSize);

        final List<FrequencyAnalyzer.Item> expectedResult = Collections
                .emptyList();
        printExpectedResult(expectedResult);

        final List<FrequencyAnalyzer.Item> actualResult = frequencyAnalyzer
                .buildTopFrequentList(values.iterator(), listSize);
        printActualResult(actualResult);

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test invalid arguments.
     */
    @Test
    public void testInvalidListSize() {
        final List<String> values = buildSourceDataWithSomeEqualFrequencies();
        final int listSize = -3;
        printSourceDataAndTestParams(values, listSize);

        System.out.println("Expected result: java.lang.AssertionError");

        final List<FrequencyAnalyzer.Item> actualResult;
        try {
            actualResult = frequencyAnalyzer.buildTopFrequentList(
                    values.iterator(), listSize);
        } catch (AssertionError e) {
            printActualResult(e);
            return;
        }
        printActualResult(actualResult);
        Assert.fail("Actual test result differs from expected test result");
    }

    private static void assertEquals(
            List<FrequencyAnalyzer.Item> expectedResult,
            List<FrequencyAnalyzer.Item> actualResult) {
        assertTrue("Actual test result differs from expected test result",
                equals(expectedResult, actualResult));
    }

    private static String resultItemToString(FrequencyAnalyzer.Item item) {
        return String.format("Frequency: %d. Value: %s", item.frequency(),
                item.value());
    }

    private static boolean equals(FrequencyAnalyzer.Item left,
                                  FrequencyAnalyzer.Item right) {
        if (left.frequency() != right.frequency()) {
            return false;
        }
        final String leftValue = left.value();
        final String rightValue = right.value();
        if (leftValue == null) {
            return rightValue == null;
        }
        return leftValue.equals(rightValue);
    }

    private static boolean equals(List<FrequencyAnalyzer.Item> left,
                                  List<FrequencyAnalyzer.Item> right) {
        if (right.size() != left.size()) {
            return false;
        }
        final Iterator<FrequencyAnalyzer.Item> leftIterator = left.iterator();
        final Iterator<FrequencyAnalyzer.Item> rightIterator = right.iterator();
        while (leftIterator.hasNext()) {
            if (!equals(leftIterator.next(), rightIterator.next())) {
                return false;
            }
        }
        return true;
    }

    private static String sourceDataToString(List<String> values) {
        return listToString(values, new StringSerializer<String>() {
            @Override
            public String serializeToString(String value) {
                return value;
            }
        });
    }

    private static String resultToString(List<FrequencyAnalyzer.Item> items) {
        return listToString(items,
                new StringSerializer<FrequencyAnalyzer.Item>() {
                    @Override
                    public String serializeToString(FrequencyAnalyzer.Item value) {
                        return resultItemToString(value);
                    }
                });
    }

    private static <T> String listToString(Collection<T> values,
                                           StringSerializer<T> stringSerializer) {
        boolean first = true;
        final StringBuilder stringBuilder = new StringBuilder(
                values.size() * 40);
        for (T value : values) {
            if (!first) {
                stringBuilder.append("; ");
            }
            stringBuilder.append(String.format("{%s}",
                    stringSerializer.serializeToString(value)));
            first = false;
        }
        return stringBuilder.toString();
    }

    private void printActualResult(Throwable e) {
        System.out.println(String.format("Actual result  : %s", e));
    }

    private void printTestHeader() {
        System.out.println(String.format("--- %s#%s ---", getClass()
                .getSimpleName(), testName.getMethodName()));
    }

    private void printExpectedResult(List<FrequencyAnalyzer.Item> expectedResult) {
        System.out.println(String.format("Expected result: %s",
                resultToString(expectedResult)));
    }

    private void printActualResult(List<FrequencyAnalyzer.Item> actualResult) {
        System.out.println(String.format("Actual result  : %s",
                resultToString(actualResult)));
    }

    private void printSourceDataAndTestParams(List<String> values, int listSize) {
        System.out.println(String.format("Source data    : %s",
                sourceDataToString(values)));
        System.out.println(String.format("Top list size  : %d", listSize));
    }

    /**
     * Builds test set containing values with no equal frequency.
     *
     * @return List of String containing values with no equal frequency.
     * @see #buildExpectedResultForSourceDataWithDifferentFrequencies
     * @see #testValuesWithDifferentFrequencies
     */
    private List<String> buildSourceDataWithDifferentFrequencies() {
        return Arrays.asList("One", "Two", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine", "Ten", "Two", "Three", "Four",
                "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Three",
                "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Four",
                "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Five", "Six",
                "Seven", "Eight", "Nine", "Ten", "Six", "Seven", "Eight",
                "Nine", "Ten", "Seven", "Eight", "Nine", "Ten", "Eight",
                "Nine", "Ten", "Nine", "Ten", "Ten");
    }

    /**
     * Builds test set containing some values with equal frequency.
     *
     * @return List of String containing some values with equal frequency.
     * @see #buildExpectedResultForSourceDataWithSomeEqualFrequencies
     * @see #testValuesWithSomeEqualFrequencies
     */
    private List<String> buildSourceDataWithSomeEqualFrequencies() {
        return Arrays
                .asList("One", "Two", "Three", "Four", "Five", "Six", "Seven",
                        "Eight", "Nine", "Ten", "Two", "Two", "Three", "Three");
    }

    /**
     * Builds test set containing 3 unique values.
     *
     * @return List of String containing 3 unique values.
     * @see #buildExpectedResultForSourceDataWithFewUniqueValues
     * @see #testValuesWithFewUniqueValues
     */
    private List<String> buildSourceDataWithFewUniqueValues() {
        return Arrays.asList("Adam", "Eve", "Apple", "Eve", "Apple", "Apple");
    }

    /**
     * Builds test set containing 3 unique values and 2 nulls.
     *
     * @return List of String containing 3 unique values and 2 nulls.
     * @see #buildExpectedResultForSourceDataWithNulls
     * @see #testValuesWithNulls
     */
    private List<String> buildSourceDataWithNulls() {
        return Arrays.asList("Adam", "Eve", null, "Apple", "Eve", "Apple",
                "Apple", null);
    }

    /**
     * Build expected result (with size of 3) for the test set built with
     * buildSourceDataWithDifferentFrequencies
     *
     * @return Expected result (with size of 3) for the test set built with
     *         buildSourceDataWithDifferentFrequencies
     * @see #buildSourceDataWithDifferentFrequencies
     * @see #testValuesWithDifferentFrequencies
     */
    private List<FrequencyAnalyzer.Item> buildExpectedResultForSourceDataWithDifferentFrequencies() {
        return Arrays.asList(new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Ten";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 10;
                                 }
                             }, new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Nine";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 9;
                                 }
                             }, new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Eight";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 8;
                                 }
                             }
        );
    }

    private List<FrequencyAnalyzer.Item> buildExpectedResultForSourceDataWithSomeEqualFrequencies() {
        return Arrays.asList(new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Three";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 3;
                                 }
                             }, new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Two";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 3;
                                 }
                             }, new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Eight";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 1;
                                 }
                             }
        );
    }

    private List<FrequencyAnalyzer.Item> buildExpectedResultForSourceDataWithFewUniqueValues() {
        return Arrays.asList(new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Apple";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 3;
                                 }
                             }, new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Eve";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 2;
                                 }
                             }, new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Adam";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 1;
                                 }
                             }
        );
    }

    private List<FrequencyAnalyzer.Item> buildExpectedResultForSourceDataWithNulls() {
        return Arrays.asList(new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Apple";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 3;
                                 }
                             }, new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return "Eve";
                                 }

                                 @Override
                                 public int frequency() {
                                     return 2;
                                 }
                             }, new FrequencyAnalyzer.Item() {
                                 @Override
                                 public String value() {
                                     return null;
                                 }

                                 @Override
                                 public int frequency() {
                                     return 2;
                                 }
                             }
        );
    }

    private static interface StringSerializer<T> {

        String serializeToString(T value);
    }
}
