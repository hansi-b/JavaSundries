package org.hansib.sundries.prefs;

import org.hansib.sundries.prefs.BigDecimalConverter
import org.hansib.sundries.prefs.BooleanConverter
import org.hansib.sundries.prefs.EnumConverter
import org.hansib.sundries.prefs.FileConverter
import org.hansib.sundries.prefs.IntegerConverter
import org.hansib.sundries.prefs.StringConverter

import spock.lang.Specification

public class ConvertersSpec extends Specification {

	def 'string converter'() {

		given:
		def c = new StringConverter() {}

		expect:
		c.val2str(c.str2val('-1')) == '-1'
		c.val2str(c.str2val('')) == ''
		c.val2str(c.str2val('abc')) == 'abc'
	}

	def 'boolean converter'() {

		given:
		def c = new BooleanConverter() {}

		expect:
		c.str2val(c.val2str(false)) == false
		c.str2val(c.val2str(true)) == true

		c.val2str(c.str2val('true')) == 'true'
		c.val2str(c.str2val('false')) == 'false'
	}

	def 'integer converter'() {

		given:
		def c = new IntegerConverter() {}

		expect:
		c.str2val(c.val2str(-1)) == -1
		c.str2val(c.val2str(0)) == 0
		c.str2val(c.val2str(1)) == 1

		c.val2str(c.str2val('-1')) == '-1'
		c.val2str(c.str2val('0')) == '0'
		c.val2str(c.str2val('1')) == '1'
	}

	def 'bigdecimal converter'() {

		given:
		def c = new BigDecimalConverter() {}

		expect:
		c.str2val(c.val2str(-1.1)) == -1.1
		c.str2val(c.val2str(.009)) == .009
		c.str2val(c.val2str(1100.0)) == 1100.0

		c.val2str(c.str2val('-2.1')) == '-2.1'
		c.val2str(c.str2val('0')) == '0'
		c.val2str(c.str2val('1100.0011')) == '1100.0011'
	}

	enum Vals {
		X
	}

	def 'enum converter'() {

		given:
		def c = new EnumConverter() {
					Class valueClass() {
						return Vals.class;
					};
				}

		expect:
		c.str2val(c.val2str(Vals.X)) == Vals.X
		c.val2str(c.str2val('X')) == 'X'
	}

	def 'file converter'() {

		given:
		def c = new FileConverter() {}

		expect:
		c.str2val('c:/abc') == new File('c:/abc')
		c.str2val('../abc') == new File('../abc')

		c.val2str(new File('../xyz')) == '../xyz'
		c.val2str(new File('c:/xyz')) == 'c:/xyz'
	}
}
