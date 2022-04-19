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

import java.util.Objects;
import java.util.Optional;

/**
 * a preference tied to an enum with a typed value
 */
interface Pref<K extends Enum<K>, V> extends Converter<V> {

	Prefs<K> prefs();

	K key();

	default void set(V value) {
		prefs().set(this, value);
	}
}

interface RequiredPref<K extends Enum<K>, V> extends Pref<K, V> {
	default V get() {
		return prefs().get(this);
	}
}

interface OptionalPref<K extends Enum<K>, V> extends Pref<K, V> {

	default Optional<V> get() {
		return Optional.ofNullable(prefs().get(this));
	}

	/**
	 * @return whether this preference is currently set (to a non-null value); same
	 *         as {@code get().isPresent()}
	 */
	default boolean isSet() {
		return prefs().get(this) != null;
	}

	/**
	 * Unsets this preference
	 */
	default void unset() {
		prefs().remove(this);
	}
}

interface PrimitiveBooleanPref<K extends Enum<K>> extends Pref<K, Boolean> {

	default boolean isTrue() {
		return Boolean.TRUE.equals(prefs().get(this));
	}

	default boolean isFalse() {
		return Boolean.FALSE.equals(prefs().get(this));
	}
}

interface PrimitiveIntegerPref<K extends Enum<K>> extends Pref<K, Integer> {

	/**
	 * @return the auto-unboxed value of this preference
	 * @throws NullPointerException is this preference is not set (can only happen
	 *                              for optional preferences)
	 */
	default int intValue() {
		return prefs().get(this);
	}
}

abstract class PrefClz<K extends Enum<K>, V> implements Pref<K, V> {

	private final K key;
	private final Prefs<K> prefs;

	PrefClz(K key, Prefs<K> prefs) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(prefs);

		this.key = key;
		this.prefs = prefs;
	}

	@Override
	public Prefs<K> prefs() {
		return prefs;
	}

	@Override
	public K key() {
		return key;
	}
}

abstract class ReqPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements RequiredPref<K, V> {

	ReqPrefClz(K key, Prefs<K> prefs) {
		super(key, prefs);
	}
}

abstract class OptPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements OptionalPref<K, V> {

	OptPrefClz(K key, Prefs<K> prefs) {
		super(key, prefs);
	}
}
