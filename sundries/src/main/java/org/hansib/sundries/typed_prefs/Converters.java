package org.hansib.sundries.typed_prefs;

import java.io.File;
import java.math.BigDecimal;

interface Converter<T> {

	T str2val(String val);

	String val2str(T val);
}

interface StringConverter extends Converter<String> {

	@Override
	public default String str2val(String val) {
		return val;
	}

	@Override
	public default String val2str(String val) {
		return val;
	}
}

interface BooleanConverter extends Converter<Boolean> {

	@Override
	public default Boolean str2val(String val) {
		return Boolean.valueOf(val);
	}

	@Override
	public default String val2str(Boolean val) {
		return Boolean.toString(val);
	}
}

interface FileConverter extends Converter<File> {

	@Override
	public default File str2val(String val) {
		return new File(val);
	}

	@Override
	public default String val2str(File val) {
		return val.getPath();
	}
}

interface IntegerConverter extends Converter<Integer> {

	@Override
	public default Integer str2val(String val) {
		return Integer.valueOf(val);
	}

	@Override
	public default String val2str(Integer val) {
		return Integer.toString(val);
	}
}

interface BigDecimalConverter extends Converter<BigDecimal> {

	@Override
	public default BigDecimal str2val(String val) {
		return new BigDecimal(val);
	}

	@Override
	public default String val2str(BigDecimal val) {
		return val.toString();
	}
}
