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
package org.hansib.sundries.l10n;

import org.hansib.sundries.Errors;
import org.hansib.sundries.testing.VisibleForTesting;

public class L10n {

	private static Localiser activeLocaliser;

	private final Domain domain;
	private final Localiser localiser;

	/**
	 * @return an L10n for a single domain enum
	 * 
	 * @param formatClz the class of the domain enum for this L10n
	 */
	public <T extends Enum<T> & FormatKey> L10n(Class<T> formatClz) {
		this(new Domain().with(formatClz), new Localiser());
	}

	/**
	 * @return an L10n for the argument domain
	 */
	public L10n(Domain domain) {
		this(domain, new Localiser());
	}

	@VisibleForTesting
	L10n(Domain domain, Localiser Localiser) {
		this.domain = domain;
		this.localiser = Localiser;
	}

	static synchronized Localiser active() {
		if (activeLocaliser == null)
			throw Errors.nullPointer("No activated localiser available");

		return activeLocaliser;
	}

	public synchronized void activate() {
		activeLocaliser = localiser;
	}

	public <K extends Enum<K> & FormatKey> L10n add(K key, String fmtString) {
		if (!domain.maps(key))
			throw Errors.illegalArg("Domain '%s' does not map class '%s' of key '%s'", this.getClass().getSimpleName(),
					key.getClass().getSimpleName(), key);
		localiser.add(key, fmtString);
		return this;
	}
}
