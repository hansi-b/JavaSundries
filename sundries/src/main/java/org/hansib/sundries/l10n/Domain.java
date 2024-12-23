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
package org.hansib.sundries.l10n;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.hansib.sundries.Errors;

/**
 * Groups the key classes known in an {@link L10n}
 */
public class Domain {

	private final Map<String, Class<? extends Enum<?>>> enumClzByName;

	Domain() {
		enumClzByName = new LinkedHashMap<>();
	}

	<K extends Enum<K> & FormatKey> Domain add(Class<K> formatClz) {
		String classMapKey = formatClz.getSimpleName();
		if (getKeysClass(classMapKey) != null)
			throw Errors.illegalArg("Duplicate mapper class name '%s': old %s, new %s", classMapKey,
					getKeysClass(classMapKey), formatClz.getName());
		enumClzByName.put(classMapKey, formatClz);
		return this;
	}

	<K extends Enum<K> & FormatKey> boolean contains(K key) {
		return enumClzByName.containsValue(key.getClass());
	}

	/**
	 * @return the enum class names known in this domain, sorted by addition to the
	 *         domain
	 */
	public Set<String> classNames() {
		return Collections.unmodifiableSet(enumClzByName.keySet());
	}

	/**
	 * @return the enum class for the argument class name; null if the name is not
	 *         known in this domain
	 */
	@SuppressWarnings("unchecked")
	public <K extends Enum<K> & FormatKey> Class<K> getKeysClass(String formatClassName) {
		return (Class<K>) enumClzByName.getOrDefault(formatClassName, null);
	}
}
