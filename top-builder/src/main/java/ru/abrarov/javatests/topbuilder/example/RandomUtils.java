package ru.abrarov.javatests.topbuilder.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utilities for generation of random values. May be replaced with another ready solution. Implemented here to make app
 * depends only on JRE 1.5.
 */
public class RandomUtils {

    private RandomUtils() {
    }

    /**
     * Generates random integer that lays in [min; max).
     *
     * @param random Random generator to be used.
     * @param min    Minimum value of generated integer. Must be <= max.
     * @param max    Maximum value of generated integer. Must be >= min.
     * @return Generated integer that lays in [min; max).
     */
    public static int randomInt(Random random, int min, int max) {
        assert min <= max : "min must be <= max";

        return random.nextInt(max - min) + min;
    }

    /**
     * Generates random string.
     *
     * @param alphabet Alphabet to be used in string generation. Must be not empty.
     * @param length   Length of the generated string. Must be >= 0.
     * @param random   Random generator to be used.
     * @return String of desired length containing random characters of desired alphabet.
     */
    public static String randomString(char[] alphabet, int length, Random random) {
        assert length >= 0 : "length must be >= 0";
        assert alphabet.length > 0 : "alphabet must be not empty";

        if (length == 0) {
            return "";
        }
        final StringBuilder valueBuilder = new StringBuilder(length);
        for (int j = 0; j < length; ++j) {
            final int alphabetPos = randomInt(random, 0, alphabet.length);
            valueBuilder.append(alphabet[alphabetPos]);
        }
        return valueBuilder.toString();
    }

    /**
     * Generates list containing random number of any value from the given collection.
     *
     * @param values   Collection of values used to fill generated list.
     * @param listSize Size of the list to be generated.
     * @param random   Random generator to be used.
     * @param <T>      Value type.
     * @return Generated list containing random number of any value from the given collection.
     */
    public static <T> List<T> randomFilledList(List<T> values, int listSize, Random random) {
        assert !(values.isEmpty() && listSize > 0) : "listSize > 0 is not permitted for non empty values";
        assert !values.isEmpty() && listSize < 0 : "listSize <= 0 is permitted for empty values only";

        final int valuesCount = values.size();
        final List<T> result = new ArrayList<T>(listSize);
        for (int i = 0; i < listSize; i++) {
            final int randomIndex = randomInt(random, 0, valuesCount);
            result.add(values.get(randomIndex));
        }
        return result;
    }

}
