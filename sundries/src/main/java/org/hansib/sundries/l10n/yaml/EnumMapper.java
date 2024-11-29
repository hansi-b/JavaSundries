/*-
 * MIT License
 *
 * for JavaSundries (https://github.com/hansi-b/JavaSundries)
 *
 * Copyright (c) 2022-2024 Hans Bering
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
package org.hansib.sundries.l10n.yaml;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.hansib.sundries.l10n.FormatKey;
import org.hansib.sundries.l10n.yaml.errors.DuplicateEnumValue;
import org.hansib.sundries.l10n.yaml.errors.DuplicateLocaleValue;
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError;
import org.hansib.sundries.l10n.yaml.errors.MissingEnumKey;
import org.hansib.sundries.l10n.yaml.errors.MissingLocaleValue;
import org.hansib.sundries.l10n.yaml.errors.UnknownEnumKey;

class EnumMapper {

	private final Consumer<L10nFormatError> errorHandler;
	private final String locale;

	EnumMapper(Consumer<L10nFormatError> errorHandler, String locale) {
		this.errorHandler = errorHandler;
		this.locale = locale;
	}

	<K extends Enum<K> & FormatKey> Map<K, String> loadMapping(List<Entry<List<Entry<String>>>> mapping,
			Class<K> enumClz) {

		final EnumMap<K, String> result = new EnumMap<>(enumClz);
		final EnumSet<K> missingKeys = EnumSet.allOf(enumClz);

		for (Entry<List<Entry<String>>> enumEntry : mapping) {
			String keyStr = enumEntry.key();
			try {
				K enumKey = Enum.valueOf(enumClz, keyStr);
				missingKeys.remove(enumKey);
				if (result.containsKey(enumKey))
					errorHandler.accept(new DuplicateEnumValue<K>(enumKey, result.get(enumKey)));
				else
					readLocale(result, enumKey, enumEntry.value());
			} catch (IllegalArgumentException ex) {
				errorHandler.accept(new UnknownEnumKey<>(enumClz, keyStr));
			}
		}
		missingKeys.forEach(k -> errorHandler.accept(new MissingEnumKey<>(k)));
		return result;
	}

	private <K extends Enum<K> & FormatKey> void readLocale(final EnumMap<K, String> foundEntries, K enumKey,
			List<Entry<String>> localeEntries) {
		List<String> localeValues = localeEntries.stream().filter(e -> locale.equals(e.key())).map(Entry<String>::value)
				.toList();
		if (localeValues.isEmpty())
			errorHandler.accept(new MissingLocaleValue<>(enumKey, locale));
		else {
			foundEntries.put(enumKey, localeValues.get(0));
			for (int i = 1; i < localeValues.size(); i++)
				errorHandler
						.accept(new DuplicateLocaleValue<>(enumKey, locale, localeValues.get(0), localeValues.get(i)));

		}
	}
}