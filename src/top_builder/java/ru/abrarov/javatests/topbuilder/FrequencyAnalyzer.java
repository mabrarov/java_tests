package ru.abrarov.javatests.topbuilder;

import java.util.Iterator;
import java.util.List;

/**
 * Algorithm that builds the list of the most frequent values.
 * The contract is defined by task definition.
 *
 * @see NaiveFrequencyAnalyzer
 */
public interface FrequencyAnalyzer {

    /**
     * Top list item.
     */
    public static interface Item {

        /**
         * Gets the value of the top list item.
         *
         * @return Value of the top list item. Nullable.
         */
        String value();

        /**
         * Gets the frequency of the top list item value.
         *
         * @return Frequency of the top list item value.
         */
        int frequency();
    }

    /**
     * Builds the list of the most frequent values.
     *
     * @param values Source values to be analyzed. Null iterator values are permitted.
     * @param size   The maximum size of the list to be built. Must be >= 0.
     * @return List of the most frequent values with their frequencies.
     */
    public List<Item> buildTopFrequentList(Iterator<String> values, int size);
}
