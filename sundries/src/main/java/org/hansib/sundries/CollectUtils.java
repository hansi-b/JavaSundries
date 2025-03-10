/*-
 * MIT License
 *
 * for JavaSundries (https://github.com/hansi-b/JavaSundries)
 *
 * Copyright (c) 2022-2023 Hans Bering
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.hansib.sundries;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CollectUtils {

	private CollectUtils() {
		// instantiation prevention
	}

	/**
	 * Defines a comparator over SortedSets of Comparable elements: Order is defined
	 * by pairwise comparison of the elements (i.e., compare the first two elements,
	 * then the next two, etc.) according to the iteration order of the respective
	 * sets. If one set is a "prefix" of the other, it is considered smaller.
	 */
	private record SortedSetComparator<E>(Comparator<E> elementComparator) implements Comparator<SortedSet<E>> {

		@Override
		public int compare(final SortedSet<E> s1, final SortedSet<E> s2) {

			if (s1 == s2)
				return 0;

			final Iterator<E> myIter = s1.iterator();
			final Iterator<E> otherIter = s2.iterator();

			while (myIter.hasNext()) {
				if (!otherIter.hasNext())
					return 1;
				final int comp = elementComparator.compare(myIter.next(), otherIter.next());
				if (comp != 0)
					return comp;
			}
			return otherIter.hasNext() ? -1 : 0;
		}
	}

	/**
	 * A not necessarily efficient method to get combinations from a SortedSet -
	 * good enough for our purposes here.
	 *
	 * @return a List of all distinct combinations of count elements
	 */
	public static <T> Stream<SortedSet<T>> combinations(final SortedSet<T> elements, final int count) {
		if (count > elements.size() || count < 1)
			return Stream.empty();

		if (count == elements.size()) {
			return Stream.of(new TreeSet<>(elements));
		}

		if (count == 1) {
			return elements.stream().map(e -> {
				final SortedSet<T> res = new TreeSet<>(elements.comparator());
				res.add(e);
				return res;
			});
		}

		return elements.stream().flatMap(e -> combinations(elements.headSet(e), count - 1).peek(s -> s.add(e)));
	}

	/**
	 * Generate all distinct ascending pairs up the argument bound; returns them as
	 * pair arrays in a list
	 *
	 * @param max the exclusive upper bound
	 * @return a list of int arrays, each containing two elements
	 */
	public static List<int[]> pairCombinations(final int max) {
		return IntStream.range(0, max - 1).mapToObj(i -> IntStream.range(i + 1, max).mapToObj(j -> new int[] { i, j }))
				.flatMap(i -> i).toList();
	}

	@SuppressWarnings("unchecked")
	public static <T> Stream<T[]> pairCombinations(final List<T> elements) {
		return IntStream.range(0, elements.size() - 1).mapToObj(i -> IntStream.range(i + 1, elements.size())
				.mapToObj(j -> genericArray(elements.get(i), elements.get(j)))).flatMap(i -> i);
	}

	/**
	 * This seems to work, but it's black magic. See
	 * <a href="https://stackoverflow.com/a/530289/1016514">details on
	 * Stackoverflow</a>
	 */
	@SuppressWarnings("unchecked")
	private static <T> T[] genericArray(final T... es) {
		final T[] newInstance = (T[]) Array.newInstance(es[0].getClass(), es.length);
		System.arraycopy(es, 0, newInstance, 0, es.length);
		return newInstance;
	}

	/**
	 * Provides the intersection set of the argument collections: Copies a Set from
	 * the first argument, retains only elements which are present in all following
	 * arguments.
	 *
	 * @return the intersection of all argument collections
	 */
	@SafeVarargs
	public static <T> Set<T> intersection(final Collection<T>... collections) {

		if (collections.length == 0)
			return Collections.emptySet();
		final Set<T> result = new HashSet<>(collections[0]);
		for (int i = 1; i < collections.length; i++) {
			result.retainAll(collections[i]);
			if (result.isEmpty())
				return Collections.emptySet();
		}
		return result;
	}

	/**
	 * Provides the union set of the argument collection, i.e., all elements which
	 * are present in any argument collection.
	 *
	 * @return the union set of the argument collections
	 */
	@SafeVarargs
	public static <T> Set<T> union(final Collection<T>... collections) {
		if (collections.length == 0)
			return Collections.emptySet();

		return Arrays.stream(collections).flatMap(Collection::stream).collect(Collectors.toSet());
	}

	/**
	 * @return a copy of the 'one' set without the elements from the other
	 */
	public static <T> SortedSet<T> difference(final SortedSet<T> one, final Collection<T> other) {
		final TreeSet<T> base = new TreeSet<>(one);
		base.removeAll(other);
		return base;
	}

	public static <E> Comparator<SortedSet<E>> sortedSetComparator(final Comparator<E> comparator) {
		return new SortedSetComparator<>(comparator);
	}

	public static <E extends Comparable<E>> Comparator<SortedSet<E>> sortedSetComparator() {
		return new SortedSetComparator<>(Comparable::compareTo);
	}

	/**
	 * A convenience method for mapping a map and getting a stream back. Relies on
	 * the iteration order of the entrySet. Somewhat similar to
	 * <code>...forEach</code>, but with a return value.
	 *
	 * @param <K>          the type of the keys in the map
	 * @param <V>          the type of the values in the map
	 * @param <M>          the type of the input map
	 * @param <R>          the type of elements in the resulting stream
	 * @param map          the map to be mapped to a list
	 * @param keyValueFunc the transformation to call on the map
	 * @return a stream resulting from mapping the function on the argument map
	 */
	public static <K, V, M extends Map<K, V>, R> Stream<R> mapMap(final M map,
			final BiFunction<? super K, ? super V, ? extends R> keyValueFunc) {
		return map.entrySet().stream().map(e -> keyValueFunc.apply(e.getKey(), e.getValue()));
	}

	/**
	 * A convenience method for filtering a map by key/value criteria.
	 *
	 * @param <M>        the type of the input and result map
	 * @param <K>        the type of the keys in the map
	 * @param <V>        the type of the values in the map
	 * @param map        the map to be filtered
	 * @param filter     the BiPredicate applied on keys and values
	 * @param mapFactory the initializer for the result map
	 * @return a map created by the mapFactory filled with those values from the
	 *         input that match the filter
	 */
	public static <M extends Map<K, V>, K, V> M filterMap(final M map, final BiPredicate<? super K, ? super V> filter,
			final Supplier<M> mapFactory) {
		return map.entrySet().stream().filter(e -> filter.test(e.getKey(), e.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, mapFactory));
	}

	/**
	 * Fills the argument <code>result</code> map with an aggregated inverse mapping
	 * of the argument <code>map</code>, where each value in <code>map</code> is
	 * mapped to a collection of the keys that mapped to it.
	 *
	 * @param <K>                    the keys in the argument map
	 * @param <V>                    the values in the argument map (= keys in the
	 *                               result)
	 * @param <M>                    the incoming map type
	 * @param <C>                    the type of collection in which the new values
	 *                               (former keys) are aggregated
	 * @param <O>                    the type of the result map
	 * @param map                    the incoming mappings to invert
	 * @param valueCollectionFactory the factory for the new value (former key)
	 *                               collections
	 * @param result                 the mapping to be filled; is returned again
	 * @return the filled <code>result</code> argument map
	 */
	public static <K, V, M extends Map<K, V>, C extends Collection<K>, O extends Map<V, C>> O invertMap(final M map,
			final Function<? super V, C> valueCollectionFactory, final O result) {
		map.forEach((k, v) -> result.computeIfAbsent(v, valueCollectionFactory).add(k));
		return result;
	}
}
