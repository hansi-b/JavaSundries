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
package org.hansib.sundries.typedprefs.store;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hansib.sundries.typedprefs.TypedPrefs;

/**
 * Keeps preferences in a simple map; mainly useful for tests
 */
public class InMemoryPrefsStore<K extends Enum<K>> implements PrefsStore<K> {

	private final Map<K, Object> prefsMap;

	public InMemoryPrefsStore() {
		this.prefsMap = new ConcurrentHashMap<>();
	}

	@Override
	public void save(TypedPrefs<K> prefs) {
		EnumSet.allOf(prefs.keysClass()).forEach(k -> save(k, prefs));
	}

	private void save(K k, TypedPrefs<K> prefs) {
		Object value = prefs.get(k);
		if (value == null)
			prefsMap.remove(k);
		else
			prefsMap.put(k, value);
	}

	@Override
	public void load(TypedPrefs<K> prefs) {
		EnumSet.allOf(prefs.keysClass()).forEach(k -> prefs.pref(k).set(prefsMap.getOrDefault(k, null)));
	}
}
