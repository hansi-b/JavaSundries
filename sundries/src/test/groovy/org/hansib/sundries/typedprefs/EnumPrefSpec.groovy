package org.hansib.sundries.typedprefs

import org.hansib.sundries.typedprefs.EnumPref

import spock.lang.Specification

public class EnumPrefSpec extends Specification {

	enum TwoKeys{
		one, two
	}

	EnumPref<TwoKeys> p = new EnumPref(TwoKeys.class)

	def 'can set and get'() {

		expect:
		p.get() == null

		when:
		p.set(TwoKeys.two)
		then:
		p.get() == TwoKeys.two
	}
}
