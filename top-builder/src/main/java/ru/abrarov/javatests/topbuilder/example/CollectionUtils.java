package ru.abrarov.javatests.topbuilder.example;

import java.util.*;

/**
 * Utility algorithms for JCF.
 */
public final class CollectionUtils {

  private CollectionUtils() {
    throw new AssertionError("Utility classes cannot be instantiated");
  }

  /**
   * Builds a list consisting of first items of the sorted original collection.
   *
   * @param collection Collection to sort. Isn't modified.
   * @param count      Number of items to be returned in a built collection. If greater than the size of collection
   *                   then the last will be used.
   * @param comparator The comparator to determine the order of the collection.
   * @return List consisting of first items of the sorted original collection.
   */
  public static <T> List<T> partialSorted(Collection<T> collection, int count, Comparator<? super T> comparator) {
    assert count >= 0 : "Number of sorted items should be >= 0";

    // Some simple optimizations
    if (count < 1 || collection.isEmpty()) {
      return Collections.emptyList();
    }
    if (count == 1) {
      return Collections.singletonList(Collections.min(collection, comparator));
    }
    if (collection.size() <= count) {
      final List<T> temp = new ArrayList<T>(collection);
      Collections.sort(temp, comparator);
      return temp;
    }
    // Remove conditional branch from the cycle by splitting the only cycle into 2 cycles.
    final NavigableSet<T> set = new TreeSet<T>(comparator);
    final Iterator<T> iterator = collection.iterator();
    // The first cycle
    for (int i = 0; i < count; ++i) {
      set.add(iterator.next());
    }
    // The rest part of the cycle
    while (iterator.hasNext()) {
      set.add(iterator.next());
      set.pollLast();
    }
    return new ArrayList<T>(set);
  }
}
