package org.hansib.sundries.typedprefs

import spock.lang.Specification

public class StringPrefSpec extends Specification {

	StringPref p = new StringPref()

	def 'can set and get'() {

		expect:
		p.get() == null

		when:
		p.set('abc')
		then:
		p.get() == 'abc'
	}
}
