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
package org.hansib.sundries.l10n.yaml;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import org.hansib.sundries.l10n.FormatKey;
import org.hansib.sundries.l10n.L10n;
import org.hansib.sundries.l10n.yaml.EnumMapsReader.EnumMapRecord;
import org.hansib.sundries.l10n.yaml.EnumMapsReader.PairList;
import org.hansib.sundries.l10n.yaml.errors.DuplicateEnum;
import org.hansib.sundries.l10n.yaml.errors.DuplicateEnumValue;
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError;
import org.hansib.sundries.l10n.yaml.errors.ParseError;
import org.hansib.sundries.l10n.yaml.errors.UnknownEnum;
import org.hansib.sundries.l10n.yaml.errors.UnknownEnumKey;

import com.fasterxml.jackson.core.JsonProcessingException;

public class L10nReader {

	private final L10n l10n;

	public L10nReader(L10n l10n) {
		this.l10n = l10n;
	}

	public <K extends Enum<K> & FormatKey> void read(String yaml, Consumer<L10nFormatError> errorHandler) {
		Objects.requireNonNull(errorHandler);

		List<EnumMapRecord> records;
		try {
			records = new EnumMapsReader().read(yaml);
		} catch (JsonProcessingException ex) {
			errorHandler.accept(new ParseError(yaml, ex));
			return;
		}
		Set<String> foundEnumNames = new HashSet<>();
		records.forEach(r -> {
			Class<K> enumClz = resolveEnumClz(r.name(), foundEnumNames, errorHandler);
			if (enumClz == null)
				return;

			l10n.addAll(readEnumValues(enumClz, r.values(), errorHandler));
		});
	}

	private <K extends Enum<K> & FormatKey> Class<K> resolveEnumClz(final String enumName, Set<String> foundEnumNames,
			Consumer<L10nFormatError> errorHandler) {
		final Class<K> enumClz = l10n.domain().get(enumName);
		if (enumClz == null) {
			errorHandler.accept(new UnknownEnum(enumName));
			return null;
		} else if (foundEnumNames.contains(enumName)) {
			errorHandler.accept(new DuplicateEnum(enumName));
			return null;
		}

		foundEnumNames.add(enumName);
		return enumClz;
	}

	static <K extends Enum<K> & FormatKey> EnumMap<K, String> readEnumValues(Class<K> enumClz, PairList pairList,
			Consumer<L10nFormatError> errorHandler) {
		final EnumMap<K, String> result = new EnumMap<>(enumClz);

		pairList.pairs.forEach(p -> {
			final String keyStr = p.key();
			try {
				K l10nKey = Enum.valueOf(enumClz, keyStr);
				String l10nVal = p.value();
				if (result.containsKey(l10nKey))
					errorHandler.accept(new DuplicateEnumValue<K>(l10nKey, result.get(l10nKey), l10nVal));
				else
					result.put(l10nKey, l10nVal);
			} catch (IllegalArgumentException ex) {
				errorHandler.accept(new UnknownEnumKey<>(enumClz, keyStr));
			}
		});
		return result;
	}
}