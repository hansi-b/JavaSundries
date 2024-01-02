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

import java.util.Map;
import java.util.function.Consumer;

import org.hansib.sundries.Errors;
import org.hansib.sundries.l10n.yaml.L10nReader;
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError;
import org.hansib.sundries.testing.VisibleForTesting;

/**
 * Bundles a domain with a specific localiser.
 */
public class L10n {

	private static Localiser activeLocaliser;

	private final Domain domain;
	private final Localiser localiser;

	@VisibleForTesting
	L10n(Domain domain, Localiser localiser) {
		this.domain = domain;
		this.localiser = localiser;
	}

	public L10n() {
		this(new Domain(), new Localiser());
	}

	/**
	 * Shorthand constructor for a single format class.
	 * 
	 * @return an L10n for a single domain enum
	 * 
	 * @param formatKeyClazz the class of the domain enum for this L10n
	 */
	public <T extends Enum<T> & FormatKey> L10n(Class<T> formatKeyClazz) {
		this();
		with(formatKeyClazz);
	}

	public <T extends Enum<T> & FormatKey> L10n with(Class<T> formatKeyClazz) {
		domain.add(formatKeyClazz);
		return this;
	}

	public <K extends Enum<K> & FormatKey> Class<K> getKeysClass(String simpleName) {
		return domain.getKeysClass(simpleName);
	}

	Domain domain() {
		return domain;
	}

	<K extends FormatKey> boolean hasFormat(K key) {
		return localiser.hasFormat(key);
	}

	static synchronized Localiser active() {
		if (activeLocaliser == null)
			throw Errors.nullPointer("No activated localiser available");

		return activeLocaliser;
	}

	public synchronized void activate() {
		activeLocaliser = localiser;
	}

	public L10n load(String yaml, Consumer<L10nFormatError> errorHandler) {
		new L10nReader(this).read(yaml, errorHandler);
		return this;
	}

	public <K extends Enum<K> & FormatKey> L10n withFormats(Map<K, String> vals) {
		vals.entrySet().forEach(e -> withFormat(e.getKey(), e.getValue()));
		return this;
	}

	public <K extends Enum<K> & FormatKey> L10n withFormat(K key, String fmtString) {
		if (!domain.contains(key))
			throw Errors.illegalArg("Domain '%s' does not map class '%s' of key '%s'", this.getClass().getSimpleName(),
					key.getClass().getSimpleName(), key);
		localiser.setFormat(key, fmtString);
		return this;
	}
}
