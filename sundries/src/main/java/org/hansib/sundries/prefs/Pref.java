/**
 * MIT License
 *
 * for JavaSundries (https://github.com/hansi-b/JavaSundries)
 *
 * Copyright (c) 2023 Hans Bering
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

import org.hansib.sundries.Errors;
import org.hansib.sundries.prefs.store.PrefsStore;

/**
 * a preference tied to an enum with a typed value
 */
public interface Pref<V> extends Converter<V> {

	PrefsStore store();

	String key();

	/**
	 * @throws IllegalArgumentException on null argument
	 */
	default void set(V value) {
		if (value == null)
			throw Errors.illegalArg("Cannot set null value on %s", key());
		store().put(key(), val2str(value));
	}
}

abstract class PrefClz<V> implements Pref<V> {

	private final String key;
	private final PrefsStore store;

	PrefClz(String key, PrefsStore store) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(store);

		this.key = key;
		this.store = store;
	}

	@Override
	public PrefsStore store() {
		return store;
	}

	@Override
	public String key() {
		return key;
	}
}

abstract class ReqPrefClz<V> extends PrefClz<V> implements RequiredPref<V> {

	ReqPrefClz(String key, PrefsStore store) {
		super(key, store);
	}
}

abstract class OptPrefClz<V> extends PrefClz<V> implements OptionalPref<V> {

	OptPrefClz(String key, PrefsStore store) {
		super(key, store);
	}
}
