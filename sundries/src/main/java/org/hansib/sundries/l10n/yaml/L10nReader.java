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
package org.hansib.sundries.l10n.yaml;

import java.util.List;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.hansib.sundries.l10n.Domain;
import org.hansib.sundries.l10n.FormatKey;
import org.hansib.sundries.l10n.L10n;
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError;
import org.hansib.sundries.l10n.yaml.errors.ParseError;
import org.hansib.sundries.testing.VisibleForTesting;

public class L10nReader {

	private final L10n l10n;
	private final EnumMapper enumMapper;
	private final Consumer<L10nFormatError> errorHandler;

	public L10nReader(L10n l10n, String locale, Consumer<L10nFormatError> errorHandler) {
		this.l10n = l10n;
		this.errorHandler = errorHandler;
		this.enumMapper = new EnumMapper(errorHandler, locale);
	}

	public <K extends Enum<K> & FormatKey> void loadEnums(String resourcesPath) {
		L10nResourcesLoader loader = new L10nResourcesLoader(resourcesPath, errorHandler);
		Domain domain = l10n.domain();
		for (String clzName : domain.classNames()) {
			String enumYaml = loader.load(clzName);
			if (enumYaml == null)
				continue;
			Class<K> keysClass = domain.getKeysClass(clzName);
			readEnum(enumYaml, keysClass);
		}
	}

	@VisibleForTesting
	<K extends Enum<K> & FormatKey> void readEnum(String enumYaml, Class<K> keysClass) {

		List<Entry<List<Entry<String>>>> mapping;
		try {
			mapping = new MappingReader().read(enumYaml);
		} catch (JsonProcessingException ex) {
			errorHandler.accept(new ParseError(enumYaml, ex));
			return;
		}

		l10n.withFormats(enumMapper.loadMapping(mapping, keysClass));
	}
}