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
package org.hansib.sundries.typedprefs;

import java.util.EnumMap;
import java.util.EnumSet;

import org.hansib.sundries.Errors;

/**
 * A collection of typed preferences sharing the same key enum.
 *
 * @param <K> the preferences keys enum
 */
public class TypedPrefs<K extends Enum<K>> {

	public static class Builder<K extends Enum<K>> {

		private final Class<K> keysClass;
		private final EnumMap<K, TypedPref<?>> prefByKey;

		public Builder(Class<K> keysClass) {
			this.keysClass = keysClass;
			this.prefByKey = new EnumMap<>(keysClass);
		}

		public Builder<K> stringPref(K key, String initialValue) {
			return checkAndMap(key, withInitial(new StringPref(), initialValue));
		}

		public Builder<K> booleanPref(K key, boolean initialValue) {
			return checkAndMap(key, withInitial(new BooleanPref(), initialValue));
		}

		public <L extends Enum<L>> Builder<K> enumPref(K key, Class<L> valueClass) {
			return checkAndMap(key, new EnumPref<>(valueClass));
		}

		public <L extends Enum<L>> Builder<K> prefsPref(K key, TypedPrefs<L> prefs) {
			return checkAndMap(key, withInitial(new PrefsPref<>(prefs), prefs));
		}

		public TypedPrefs<K> build() {
			EnumSet<K> required = EnumSet.allOf(keysClass);
			required.removeAll(prefByKey.keySet());
			if (!required.isEmpty())
				throw Errors.illegalState("Missing preference mapping(s) for %s", required);
			return new TypedPrefs<>(keysClass, new EnumMap<>(prefByKey));
		}

		private <X extends TypedPref<?>> Builder<K> checkAndMap(K key, X pref) {
			if (prefByKey.containsKey(key))
				throw Errors.illegalArg("Cannot set existing preference %s to new %s (is: %s)", key, pref,
						prefByKey.get(key));
			prefByKey.put(key, pref);
			return this;
		}

		private static <V, P extends TypedPref<V>> P withInitial(P pref, V initialValue) {
			pref.set(initialValue);
			return pref;
		}
	}

	private final Class<K> keysClass;
	private final EnumMap<K, TypedPref<?>> map;

	private TypedPrefs(Class<K> keysClass, EnumMap<K, TypedPref<?>> map) {
		this.keysClass = keysClass;
		this.map = map;
	}

	public Class<K> keysClass() {
		return keysClass;
	}

	/**
	 * Sets the preference of the argument key to the argument value.
	 */
	public <V> void set(K key, V value) {
		pref(key).set(value);
	}

	/**
	 * @return the value of the preference for the argument key
	 */
	@SuppressWarnings("unchecked")
	public <V> V get(K key) {
		return (V) pref(key).get();
	}

	/**
	 * @return the preference for the argument key
	 */
	@SuppressWarnings("unchecked")
	public <V, P extends TypedPref<V>> P pref(K key) {
		return (P) map.get(key);
	}
}