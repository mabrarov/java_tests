package ru.abrarov.topbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Builder of list of random strings.
 */
public class RandomSourceDataProvider {

    private static final char[] RANDOM_ALPHABET =
            "0123546789ABCDEFGHIJKLMNOPQRSTUWXWZabcdefghijklmnopqrstuwxwz".toCharArray();
    private final Random random;

    public RandomSourceDataProvider() {
        this.random = new Random();
    }

    /**
     * Builds list of random strings.
     *
     * @param minValueLength   Minimum length of string to be generated.
     * @param maxValueLength   Maximum length of string to be generated.
     * @param uniqueValueCount Number of unique strings to be generated.
     * @param totalValueCount  Total number of strings to be generated.
     *                         Equals to the size (defines the size) of the built list.
     * @return List of the desired size (totalValueCount) containing random strings of the desired length limits.
     */
    public List<String> buildRandomValues(int minValueLength, int maxValueLength, int uniqueValueCount,
                                          int totalValueCount) {
        final List<String> uniqueValues = buildUniqueValues(minValueLength, maxValueLength, uniqueValueCount);
        return RandomUtils.randomFilledList(uniqueValues, totalValueCount, random);
    }

    private List<String> buildUniqueValues(int minValueLength, int maxValueLength, int uniqueValueCount) {
        final List<String> values = new ArrayList<String>(uniqueValueCount);
        for (int i = 0; i < uniqueValueCount; ++i) {
            values.add(buildRandomString(minValueLength, maxValueLength));
        }
        return values;
    }

    private String buildRandomString(int minLength, int maxLength) {
        final int stringLength = RandomUtils.randomInt(random, minLength, maxLength + 1);
        return RandomUtils.randomString(RANDOM_ALPHABET, stringLength, random);
    }

}
