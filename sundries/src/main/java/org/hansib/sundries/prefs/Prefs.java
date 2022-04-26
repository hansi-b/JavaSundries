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

	private final PrefsStore store;

	public Prefs(PrefsStore store) {
		this.store = store;
	}

	<V> V get(Pref<V> pref) {
		String val = store.get(pref.key());
		return val == null ? null : pref.str2val(val);
	}

	<V> void set(Pref<V> pref, V val) {
		if (val == null)
			throw Errors.illegalArg("Cannot set null value (%s)", pref);
		store.put(pref.key(), pref.val2str(val));
	}

	<V> boolean contains(OptionalPref<V> pref) {
		return get(pref) != null;
	}

	<V> void remove(OptionalPref<V> pref) {
		store.remove(pref.key());
	}

	/*
	 * factory methods
	 */

	public OptString optionalString(String key) {
		return new OptString(key, store);
	}

	public ReqString requiredString(String key, String initialValue) {
		return withInitial(new ReqString(key, store), initialValue);
	}

	public OptBoolean optionalBoolean(String key) {
		return new OptBoolean(key, store);
	}

	public ReqBoolean requiredBoolean(String key, boolean initialValue) {
		return withInitial(new ReqBoolean(key, store), initialValue);
	}

	public OptInteger optionalInteger(String key) {
		return new OptInteger(key, store);
	}

	public ReqInteger requiredInteger(String key, int initialValue) {
		return withInitial(new ReqInteger(key, store), initialValue);
	}

	public OptBigDecimal optionalBigDecimal(String key) {
		return new OptBigDecimal(key, store);
	}

	public ReqBigDecimal requiredBigDecimal(String key, BigDecimal initialValue) {
		return withInitial(new ReqBigDecimal(key, store), initialValue);
	}

	public <L extends Enum<L>> OptEnum<L> optionalEnum(String key, Class<L> valueClass) {
		return new OptEnum<>(key, valueClass, store);
	}

	public <L extends Enum<L>> ReqEnum<L> requiredEnum(String key, Class<L> valueClass, L initialValue) {
		return withInitial(new ReqEnum<>(key, valueClass, store), initialValue);
	}

	public OptFile optionalFile(String key) {
		return new OptFile(key, store);
	}

	public ReqFile requiredFile(String key, File initialValue) {
		return withInitial(new ReqFile(key, store), initialValue);
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