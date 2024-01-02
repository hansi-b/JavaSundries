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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hansib.sundries.Errors;

/**
 * A mapping of format keys to their respective message formats. Ensures
 * constraints such as uniqueness of keys.
 */
class Localiser {

	private final Map<FormatKey, MessageFormat> formats;

	Localiser() {
		formats = new HashMap<>();
	}

	/**
	 * Formats the argument key with the (optional arguments) according to the rules
	 * of {@link MessageFormat}
	 */
	String fmt(FormatKey key, Object... args) {
		MessageFormat fmt = formats.get(key);
		if (fmt == null)
			return "%s:%s%s".formatted(key.getClass().getSimpleName(), key, Arrays.toString(args));
		return fmt.format(args);
	}

	<K extends FormatKey> boolean hasFormat(K key) {
		return formats.containsKey(key);
	}

	<K extends FormatKey> void setFormat(K key, String fmt) {
		if (hasFormat(key))
			throw Errors.illegalArg("Duplicate format mapping for '%s' ('%s') in %s: new '%s', old '%s'", key,
					key.getClass().getSimpleName(), this.getClass(), formats.get(key).toPattern(), fmt);
		formats.put(key, new MessageFormat(fmt));
	}
}