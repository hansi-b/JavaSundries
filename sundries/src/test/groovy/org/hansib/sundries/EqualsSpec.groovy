package org.hansib.sundries;

import spock.lang.Specification

public class EqualsSpec extends Specification {

	def "null-safe equals"() {

		expect:
		Equals.nullSafeEquals(null, null)
		!Equals.nullSafeEquals(null, Integer.valueOf(3))
		!Equals.nullSafeEquals(Integer.valueOf(3), null)
		Equals.nullSafeEquals(Integer.valueOf(3), Integer.valueOf(3))
		!Equals.nullSafeEquals(Integer.valueOf(3), Integer.valueOf(4))
	}
}
