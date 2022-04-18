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

import java.util.Optional;

/**
 * a preference tied to an enum with a typed value
 */
interface Pref<K extends Enum<K>, V> extends Converter<V> {

	Prefs<K> prefs();

	K key();

	void set(V value);
}

interface RequiredPref<K extends Enum<K>, V> extends Pref<K, V> {
	V get();
}

interface OptionalPref<K extends Enum<K>, V> extends Pref<K, V> {

	Optional<V> get();

	void remove();
}

abstract class PrefClz<K extends Enum<K>, V> implements Pref<K, V> {

	private final K key;
	private final Prefs<K> prefs;

	PrefClz(K key, Prefs<K> prefs) {
		this.key = key;
		this.prefs = prefs;
	}

	@Override
	public Prefs<K> prefs() {
		return prefs;
	}

	public K key() {
		return key;
	}

	@Override
	public void set(V value) {
		prefs.set(this, value);
	}
}

abstract class ReqPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements RequiredPref<K, V> {

	ReqPrefClz(K key, Prefs<K> prefs) {
		super(key, prefs);
	}

	@Override
	public V get() {
		return prefs().get(this);
	}
}

abstract class OptPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements OptionalPref<K, V> {

	OptPrefClz(K key, Prefs<K> prefs) {
		super(key, prefs);
	}

	@Override
	public Optional<V> get() {
		return Optional.ofNullable(prefs().get(this));
	}

	@Override
	public void remove() {
		prefs().remove(this);
	}
}
