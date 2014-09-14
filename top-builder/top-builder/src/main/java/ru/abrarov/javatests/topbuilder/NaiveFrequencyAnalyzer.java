package ru.abrarov.javatests.topbuilder;

import ru.abrarov.javatests.topbuilder.example.CollectionUtils;

import java.util.*;

/**
 * Naive implementation of the FrequencyAnalyzer. Uses {@link java.util.HashMap java.util.HashMap} to build distribution
 * of the analyzed values so the number of unique values (among the analyzed ones) and their hashes distribution are the
 * most significant factors for memory consumption (the first of this factors) and performance. Because of usage of
 * java.util.HashMap and {@link java.util.Collections#sort java.util.Collections.sort} the complexity of {@link
 * #buildTopFrequentList} is o(n * log(n)).
 *
 * @see FrequencyAnalyzer
 */
public class NaiveFrequencyAnalyzer implements FrequencyAnalyzer {

    private static final Comparator<Item> DISTRIBUTION_ITEM_COMPARATOR = new Comparator<Item>() {
        @Override
        public int compare(Item left, Item right) {
            final int leftFrequency = left.frequency();
            final int rightFrequency = right.frequency();
            if (leftFrequency > rightFrequency) {
                return -1;
            }
            if (leftFrequency < rightFrequency) {
                return 1;
            }
            // If frequencies are equal then compare values to make results of
            // algorithm predictable
            final String leftValue = left.value();
            final String rightValue = right.value();
            if (leftValue == rightValue) {
                return 0;
            }
            if (leftValue == null) {
                return -1;
            }
            if (rightValue == null) {
                return 1;
            }
            return leftValue.compareTo(rightValue);
        }
    };

    @Override
    public List<Item> buildTopFrequentList(Iterator<String> values, int size) {
        assert size >= 0 : "Size of the list must be >= 0";

        if (size == 0) {
            return Collections.emptyList();
        }
        return findTopFrequentItems(buildDistributionMap(values).values(), size);
    }

    private static class DistributionItem implements Item {

        private final String value;
        private int frequency;

        public DistributionItem(String value) {
            this.value = value;
            this.frequency = 1;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public int frequency() {
            return frequency;
        }

        /**
         * Increments stored frequency.
         */
        public void incFrequency() {
            ++frequency;
        }
    }

    /**
     * Builds distribution map of the given values.
     *
     * @param values Values to be analyzed. Nulls as values of iterator are permitted.
     * @return Map of values distribution.
     */
    private Map<String, Item> buildDistributionMap(Iterator<String> values) {
        final Map<String, Item> distribution = new HashMap<String, Item>();
        while (values.hasNext()) {
            final String value = values.next();
            final DistributionItem item = (DistributionItem) distribution.get(value);
            if (item == null) {
                distribution.put(value, new DistributionItem(value));
            } else {
                item.incFrequency();
            }
        }
        return distribution;
    }

    /**
     * Finds items of the given distribution having max frequency.
     *
     * @param distribution Distribution to search in.
     * @param count        Maximum number of returned items.
     * @return List of items of the given distribution having max frequency.
     */
    private List<Item> findTopFrequentItems(Collection<Item> distribution, int count) {
        return CollectionUtils.partialSorted(distribution, count, DISTRIBUTION_ITEM_COMPARATOR);
    }

}
