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

import org.hansib.sundries.Errors;
import org.hansib.sundries.prefs.store.PrefsStore;

public class Prefs<K extends Enum<K>> {

	private final PrefsStore<K> store;

	public Prefs(PrefsStore<K> store) {
		this.store = store;
	}

	<V> V get(Pref<K, V> pref) {
		String val = store.get(pref.key());
		return val == null ? null : pref.str2val(val);
	}

	<V> void set(Pref<K, V> pref, V val) {
		if (val == null)
			throw Errors.illegalArg("Cannot set null value (%s)", pref);
		store.put(pref.key(), pref.val2str(val));
	}

	boolean contains(OptionalPref<K, ?> pref) {
		return get(pref) != null;
	}

	void remove(OptionalPref<K, ?> pref) {
		store.remove(pref.key());
	}

	/*
	 * factory methods
	 */

	public OptString<K> optionalString(K key) {
		return new OptString<>(key, this);
	}

	public ReqString<K> requiredString(K key, String initialValue) {
		return withInitial(new ReqString<>(key, this), initialValue);
	}

	public OptBoolean<K> optionalBoolean(K key) {
		return new OptBoolean<>(key, this);
	}

	public ReqBoolean<K> requiredBoolean(K key, boolean initialValue) {
		return withInitial(new ReqBoolean<>(key, this), initialValue);
	}

	public OptInteger<K> optionalInteger(K key) {
		return new OptInteger<>(key, this);
	}

	public ReqInteger<K> requiredInteger(K key, int initialValue) {
		return withInitial(new ReqInteger<>(key, this), initialValue);
	}

	public OptBigDecimal<K> optionalBigDecimal(K key) {
		return new OptBigDecimal<>(key, this);
	}

	public ReqBigDecimal<K> requiredBigDecimal(K key, BigDecimal initialValue) {
		return withInitial(new ReqBigDecimal<>(key, this), initialValue);
	}

	public <L extends Enum<L>> OptEnum<K, L> optionalEnum(K key, Class<L> valueClass) {
		return new OptEnum<>(key, valueClass, this);
	}

	public <L extends Enum<L>> ReqEnum<K, L> requiredEnum(K key, Class<L> valueClass, L initialValue) {
		return withInitial(new ReqEnum<>(key, valueClass, this), initialValue);
	}

	public OptFile<K> optionalFile(K key) {
		return new OptFile<>(key, this);
	}

	public ReqFile<K> requiredFile(K key, File initialValue) {
		return withInitial(new ReqFile<>(key, this), initialValue);
	}

	/*
	 * helpers
	 */

	<V, P extends Pref<K, V>> P withInitial(P pref, V initialValue) {
		if (pref.prefs().get(pref) == null)
			pref.prefs().set(pref, initialValue);
		return pref;
	}
}