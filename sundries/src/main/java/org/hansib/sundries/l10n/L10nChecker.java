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

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Checks whether the localiser of the given {@link L10n} has values for all
 * format keys.
 */
public class L10nChecker {

	public record MissingKeys<K extends Enum<K> & FormatKey>(Class<K> enumClz, Set<K> missing) {
		public MissingKeys {
			missing = Set.copyOf(missing);
		}

		public String description() {
			return "%s:%n\t%s%n".formatted(enumClz.getSimpleName(), //
					String.join("%n\t".formatted(), EnumSet.copyOf(missing).stream().map(Enum::name).toList()));
		}
	}

	/**
	 * Whether to call the missing keys handler only when at least one missing key
	 * has been found, or to call it also when no missing keys have been found (with
	 * an empty set of missing keys).
	 */
	public enum MissingKeysHandleMode {
		OnlyWithMissingKeys, AlsoWithoutMissingKeys
	}

	private final L10n l10n;

	public L10nChecker(L10n l10n) {
		this.l10n = l10n;
	}

	public <K extends Enum<K> & FormatKey> void checkCompleteness(Consumer<MissingKeys<K>> handler,
			MissingKeysHandleMode handleMode) {
		Domain domain = l10n.domain();
		for (String s : domain.classNames()) {
			Class<K> enumClz = domain.getKeysClass(s);
			checkEnumClz(enumClz, handler, handleMode);
		}
	}

	private <K extends Enum<K> & FormatKey> void checkEnumClz(Class<K> enumClz, Consumer<MissingKeys<K>> handler,
			MissingKeysHandleMode handleMode) {
		EnumSet<K> missing = EnumSet.allOf(enumClz);
		for (K key : EnumSet.allOf(enumClz)) {
			if (l10n.hasFormat(key))
				missing.remove(key);
		}
		if (!missing.isEmpty() || handleMode == MissingKeysHandleMode.AlsoWithoutMissingKeys)
			handler.accept(new MissingKeys<>(enumClz, missing));
	}
}
