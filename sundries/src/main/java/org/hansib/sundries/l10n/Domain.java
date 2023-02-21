/**
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
package org.hansib.sundries.l10n;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.hansib.sundries.Errors;

public class Domain {

	private final Map<String, Class<? extends Enum<?>>> enumClzBySimpleName;

	public Domain() {
		enumClzBySimpleName = new LinkedHashMap<>();
	}

	public <T extends Enum<T> & FormatKey> Domain with(Class<T> formatClz) {
		String simpleName = formatClz.getSimpleName();
		if (get(simpleName) != null)
			throw Errors.illegalArg("Duplicate mapper class name '%s': old %s, new %s", simpleName, get(simpleName),
					formatClz.getName());
		enumClzBySimpleName.put(simpleName, formatClz);
		return this;
	}

	<K extends Enum<K> & FormatKey> Class<K> get(K key) {
		return get(key.getClass().getSimpleName());
	}

	@SuppressWarnings("unchecked")
	public <K extends Enum<K> & FormatKey> Class<K> get(String simpleName) {
		return (Class<K>) enumClzBySimpleName.getOrDefault(simpleName, null);
	}

	Set<String> simpleNames() {
		return Collections.unmodifiableSet(enumClzBySimpleName.keySet());
	}
}
