package org.hansib.sundries.prefs

import spock.lang.Specification

public class OptBooleanSpec extends Specification {

	Prefs<TestKey> store = Mock()
	OptBoolean<TestKey> p = new OptBoolean(TestKey.bool, store)

	def 'defaults to empty value'(){

		expect:
		p.get() == Optional.empty()
	}

	def 'can set value'(){

		when:
		p.set(true)

		then:
		1 * store.set(p, true)
	}

	def 'can get given value'(){

		given:
		store.get(p) >> false

		expect:
		p.get() == Optional.of(false)
	}
}
