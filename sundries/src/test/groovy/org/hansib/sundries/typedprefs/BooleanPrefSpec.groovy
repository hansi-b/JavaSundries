package org.hansib.sundries.typedprefs

import spock.lang.Specification

public class BooleanPrefSpec extends Specification {

	BooleanPref p = new BooleanPref()

	def 'can set and check value'() {

		expect:
		p.get() == null

		p.isTrue() == false
		p.isFalse() == false

		when:
		p.set(true)

		then:
		p.get() == true
		p.isTrue() == true
		p.isFalse() == false

		when:
		p.set(false)

		then:
		p.get() == false
		p.isTrue() == false
		p.isFalse() == true
	}
}