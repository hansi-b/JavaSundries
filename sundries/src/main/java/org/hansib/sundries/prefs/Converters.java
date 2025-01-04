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
package org.hansib.sundries.prefs;

import java.io.File;
import java.math.BigDecimal;

/**
 * A prerequisite for a preference in order to be able to (de)serialize it.
 */
interface Converter<T> {

	T str2val(String val);

	String val2str(T val);
}

interface StringConverter extends Converter<String> {

	@Override
	default String str2val(String val) {
		return val;
	}

	@Override
	default String val2str(String val) {
		return val;
	}
}

interface BooleanConverter extends Converter<Boolean> {

	@Override
	default Boolean str2val(String val) {
		return Boolean.valueOf(val);
	}

	@Override
	default String val2str(Boolean val) {
		return Boolean.toString(val);
	}
}

interface IntegerConverter extends Converter<Integer> {

	@Override
	default Integer str2val(String val) {
		return Integer.valueOf(val);
	}

	@Override
	default String val2str(Integer val) {
		return Integer.toString(val);
	}
}

interface BigDecimalConverter extends Converter<BigDecimal> {

	@Override
	default BigDecimal str2val(String val) {
		return new BigDecimal(val);
	}

	@Override
	default String val2str(BigDecimal val) {
		return val.toString();
	}
}

interface EnumConverter<L extends Enum<L>> extends Converter<L> {

	Class<L> valueClass();

	@Override
	default L str2val(String val) {
		return Enum.valueOf(valueClass(), val);
	}

	@Override
	default String val2str(L val) {
		return val.name();
	}
}

interface FileConverter extends Converter<File> {

	@Override
	default File str2val(String val) {
		return new File(val);
	}

	@Override
	default String val2str(File val) {
		return val.getPath();
	}
}
