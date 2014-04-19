package ru.abrarov.javatests.topbuilder;

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

    private static final Comparator<DistributionItem> DISTRIBUTION_ITEM_COMPARATOR =
            new Comparator<DistributionItem>() {
                @Override
                public int compare(DistributionItem left, DistributionItem right) {
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
        final List<DistributionItem> valueDistribution =
                new ArrayList<DistributionItem>(buildDistributionMap(values).values());
        final List<DistributionItem> topFrequentDistributionItems =
                findTopFrequentDistributionItems(valueDistribution, size);
        return buildResult(topFrequentDistributionItems);
    }

    private static class DistributionItem implements Item {

        private final String value;
        private int frequency;

        public DistributionItem(String value) {
            this.value = value;
            this.frequency = 0;
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
     * @param values Values to be analyzed. Nulls are permitted.
     * @return Map of values distribution.
     */
    private Map<String, DistributionItem> buildDistributionMap(Iterator<String> values) {
        final Map<String, DistributionItem> distribution = new HashMap<String, DistributionItem>();
        while (values.hasNext()) {
            final String value = values.next();
            DistributionItem distributionItem = distribution.get(value);
            if (distributionItem == null) {
                distributionItem = new DistributionItem(value);
                distribution.put(value, distributionItem);
            }
            distributionItem.incFrequency();
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
    private List<DistributionItem> findTopFrequentDistributionItems(List<DistributionItem> distribution, int count) {
        final int topFrequentListSize = Math.min(distribution.size(), count);
        partialSort(distribution, topFrequentListSize, DISTRIBUTION_ITEM_COMPARATOR);
        return distribution.subList(0, topFrequentListSize);
    }

    /**
     * Partial sort of most small items.
     *
     * @param list       List to be sorted.
     * @param count      Number of most small items to be sorted.
     * @param comparator The comparator to determine the order of the list.
     */
    private static <T> void partialSort(List<T> list, int count, Comparator<? super T> comparator) {
        // todo: This implementation is rather naive and may be much better.
        // todo: JCF, Guava, Apache Commons or another ready solution needs to
        // be searched for.
        final int size = list.size();
        assert count >= 0 && count <= size : "Number of sorted items should be >= 0 and <= size of list";

        if (size < 2 || count < 1) {
            return;
        }
        Collections.sort(list, comparator);
    }

    /**
     * Builds list of Item from the given collection of DistributionItem
     *
     * @param distributionItems The source List which contents will be copied to the built list of Item.
     * @return Built list of Item.
     */
    private List<Item> buildResult(Collection<DistributionItem> distributionItems) {
        final List<Item> result = new ArrayList<Item>(distributionItems.size());
        for (DistributionItem distributionItem : distributionItems) {
            result.add(distributionItem);
        }
        return result;
    }

}
