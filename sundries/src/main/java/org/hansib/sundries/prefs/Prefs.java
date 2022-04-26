/**
 * MIT License
 *
 * for JavaSundries (https://github.com/hansi-b/JavaSundries)
 *
 * Copyright (c) 2022 Hans Bering
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

import org.hansib.sundries.prefs.store.PrefsStore;

public class Prefs<K extends Enum<K>> {

	private final PrefsStore store;

	public Prefs(PrefsStore store) {
		this.store = store;
	}

	/**
	 * @return the stored String value, possibly null
	 */
	public String get(K key) {
		return store.get(key.name());
	}

	public <V> void set(K key, String val) {
		store.put(key.name(), val);
	}

	/*
	 * factory methods
	 */

	public OptString optionalString(K key) {
		return new OptString(key.name(), store);
	}

	public ReqString requiredString(K key, String initialValue) {
		return withInitial(new ReqString(key.name(), store), initialValue);
	}

	public OptBoolean optionalBoolean(K key) {
		return new OptBoolean(key.name(), store);
	}

	public ReqBoolean requiredBoolean(K key, boolean initialValue) {
		return withInitial(new ReqBoolean(key.name(), store), initialValue);
	}

	public OptInteger optionalInteger(K key) {
		return new OptInteger(key.name(), store);
	}

	public ReqInteger requiredInteger(K key, int initialValue) {
		return withInitial(new ReqInteger(key.name(), store), initialValue);
	}

	public OptBigDecimal optionalBigDecimal(K key) {
		return new OptBigDecimal(key.name(), store);
	}

	public ReqBigDecimal requiredBigDecimal(K key, BigDecimal initialValue) {
		return withInitial(new ReqBigDecimal(key.name(), store), initialValue);
	}

	public <L extends Enum<L>> OptEnum<L> optionalEnum(K key, Class<L> valueClass) {
		return new OptEnum<>(key.name(), valueClass, store);
	}

	public <L extends Enum<L>> ReqEnum<L> requiredEnum(K key, Class<L> valueClass, L initialValue) {
		return withInitial(new ReqEnum<>(key.name(), valueClass, store), initialValue);
	}

	public OptFile optionalFile(K key) {
		return new OptFile(key.name(), store);
	}

	public ReqFile requiredFile(K key, File initialValue) {
		return withInitial(new ReqFile(key.name(), store), initialValue);
	}

	/*
	 * helpers
	 */

	<V, P extends Pref<V>> P withInitial(P pref, V initialValue) {
		if (!pref.store().contains(pref.key()))
			pref.set(initialValue);
		return pref;
	}
}