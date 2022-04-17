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
package org.hansib.sundries.prefs.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * keeps preferences in a simple map
 */
public class InMemoryPrefsStore<K extends Enum<K>> implements PrefsStore<K> {

	private final Map<K, String> prefs;

	public InMemoryPrefsStore() {
		this.prefs = new ConcurrentHashMap<>();
	}

	public void put(final K key, final String value) {
		prefs.put(key, value);
	}

	public String get(final K key) {
		return prefs.getOrDefault(key, null);
	}

	public boolean contains(final K key) {
		return get(key) != null;
	}

	public void remove(final K key) {
		prefs.remove(key);
	}
}
