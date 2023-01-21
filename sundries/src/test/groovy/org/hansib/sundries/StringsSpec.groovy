package org.hansib.sundries

import spock.lang.Specification

public class StringsSpec extends Specification {

	def 'get simple id string'() {
		when:
		def x = 'hello'
		def xHexHash = String.format('%08X', ((Object)x).hashCode())

		then:
		Strings.idStr(x, 'world') == "String@${xHexHash}(world)"
	}

	def 'join happy paths'() {
		expect:
		Strings.join('', ['a', 'b', 'c'] as String[], 0, 3) == 'abc'
		Strings.join('_', ['a', 'bb', 'ccc'] as String[], 1, 3) == 'bb_ccc'
		Strings.join('__', ['a', 'bb', 'c'] as String[], 0, 2) == 'a__bb'
	}

	def 'join throws ArrayIndexOutOfBoundsException'() {

		when:
		Strings.join('', ['a', 'b', 'c'] as String[], 0, 4) == 'abc'

		then:
		thrown ArrayIndexOutOfBoundsException
	}

	def 'can normalize EOL to system line break'() {
		given:
		def eol = System.lineSeparator()

		expect:
		Strings.toSystemEol('''abc\ndef\n''') == """abc${eol}def${eol}"""
	}
}
