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
package org.hansib.sundries.prefs;

import java.io.File;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;

import org.hansib.sundries.Errors;
import org.hansib.sundries.prefs.store.PrefsStore;

public class Prefs<K extends Enum<K>> {

	public static class Builder<K extends Enum<K>> {

		private PrefsStore store;
		private EnumMap<K, Pref<?>> mapped;
		private EnumSet<K> required;

		public Builder(Class<K> keysClass, PrefsStore store) {
			this.store = store;
			this.mapped = new EnumMap<>(keysClass);
			this.required = EnumSet.allOf(keysClass);
		}

		public Builder<K> optionalString(K key) {
			return mapped(key, new OptString(key.name(), store));
		}

		public Builder<K> requiredString(K key, String initialValue) {
			return mapped(key, withInitial(new ReqString(key.name(), store), initialValue));
		}

		public Builder<K> optionalBoolean(K key) {
			return mapped(key, new OptBoolean(key.name(), store));
		}

		public Builder<K> requiredBoolean(K key, boolean initialValue) {
			return mapped(key, withInitial(new ReqBoolean(key.name(), store), initialValue));
		}

		public Builder<K> optionalInteger(K key) {
			return mapped(key, new OptInteger(key.name(), store));
		}

		public Builder<K> requiredInteger(K key, int initialValue) {
			return mapped(key, withInitial(new ReqInteger(key.name(), store), initialValue));
		}

		public Builder<K> optionalBigDecimal(K key) {
			return mapped(key, new OptBigDecimal(key.name(), store));
		}

		public Builder<K> requiredBigDecimal(K key, BigDecimal initialValue) {
			return mapped(key, withInitial(new ReqBigDecimal(key.name(), store), initialValue));
		}

		public <L extends Enum<L>> Builder<K> optionalEnum(K key, Class<L> valueClass) {
			return mapped(key, new OptEnum<>(key.name(), valueClass, store));
		}

		public <L extends Enum<L>> Builder<K> requiredEnum(K key, Class<L> valueClass, L initialValue) {
			return mapped(key, withInitial(new ReqEnum<>(key.name(), valueClass, store), initialValue));
		}

		public Builder<K> optionalFile(K key) {
			return mapped(key, new OptFile(key.name(), store));
		}

		public Builder<K> requiredFile(K key, File initialValue) {
			return mapped(key, withInitial(new ReqFile(key.name(), store), initialValue));
		}

		public Prefs<K> build() {
			required.removeAll(mapped.keySet());
			if (!required.isEmpty())
				throw Errors.illegalState("Missing preference mapping(s) for %s", required);
			return new Prefs<>(mapped);
		}

		/*
		 * helpers
		 */

		private <X extends Pref<?>> Builder<K> mapped(K key, X pref) {
			if (mapped.containsKey(key))
				throw Errors.illegalArg("Cannot set existing preference %s to new %s (is: %s)", key, pref,
						mapped.get(key));
			mapped.put(key, pref);
			return this;
		}

		private <V, P extends Pref<V>> P withInitial(P pref, V initialValue) {
			if (!pref.store().contains(pref.key()))
				pref.set(initialValue);
			return pref;
		}
	}

	private final EnumMap<K, Pref<?>> map;

	private Prefs(EnumMap<K, Pref<?>> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public <P extends Pref<?>> P getPref(K key) {
		return (P) map.get(key);
	}
}