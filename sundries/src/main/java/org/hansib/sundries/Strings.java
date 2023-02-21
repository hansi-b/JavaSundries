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
package org.hansib.sundries;

import java.util.stream.IntStream;

public class Strings {

	private Strings() {
		// instantiation prevention
	}

	/**
	 * @param id some semantic identifier of the object
	 * @return a canonical representation in the format
	 *         [className]@[objectHexHash](id)
	 */
	public static <T> String idStr(T o, String id) {
		return String.format("%s@%08X(%s)", o.getClass().getSimpleName(), ((Object) o).hashCode(), id);
	}

	public static String join(String joiner, String[] arr, int beginIncl, int endExcl) {
		return String.join(joiner, IntStream.range(beginIncl, endExcl).boxed().map(i -> arr[i]).toList());
	}

	/**
	 * Canonicalises \n in the argument string to the system line separator. Needed,
	 * e.g., for Groovy multilines which always use \n. NB: Will also replace \n
	 * when present within a system line separator, so only use when you know your
	 * string is not already canonicalised.
	 * 
	 * @return the argument string with all occurrences of \n replaced by the System
	 *         line separator
	 */
	public static String toSystemEol(String str) {
		return str.replace("\n", System.lineSeparator());
	}
}
