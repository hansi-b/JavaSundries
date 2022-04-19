package org.hansib.sundries.prefs

import spock.lang.Specification

public class ReqBooleanSpec extends Specification {

	Prefs<TestKey> store = Mock()
	ReqBoolean<TestKey> p = new ReqBoolean(TestKey.bool, store)

	def 'defaults to initial value'() {

		expect:
		p.get() == null
	}

	def 'can set value'() {

		when:
		p.set(true)

		then:
		1 * store.set(p, true)
	}

	def 'can get given value'() {

		given:
		store.get(p) >> false

		expect:
		p.get() == false
	}

	def 'primitive access'() {
		given:
		store.get(p) >> false

		expect:
		p.isFalse() == true
		p.isTrue() == false
	}
}
