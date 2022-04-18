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

import org.hansib.sundries.Errors;
import org.hansib.sundries.prefs.store.PrefsStore;

public class PrefsWithStore<K extends Enum<K>> implements Prefs<K> {

	private final PrefsStore<K> store;

	public PrefsWithStore(PrefsStore<K> store) {
		this.store = store;
	}

	@Override
	public <V> V get(Pref<K, V> pref) {
		String val = store.get(pref.key());
		return val == null ? null : pref.str2val(val);
	}

	@Override
	public <V> void set(Pref<K, V> pref, V val) {
		if (val == null)
			throw Errors.illegalArg("Cannot set null value (%s)", pref);
		store.put(pref.key(), pref.val2str(val));
	}

	@Override
	public void remove(OptionalPref<K, ?> pref) {
		store.remove(pref.key());
	}
}