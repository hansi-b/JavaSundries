package org.hansib.sundries.prefs

import spock.lang.Specification

public class ReqIntegerSpec extends Specification {

	Prefs<TestKey> store = Mock()
	ReqInteger<TestKey> p = new ReqInteger(TestKey.integer, store)

	def 'defaults to initial value'(){

		expect:
		p.get() == null
	}

	def 'can set value'(){

		when:
		p.set(987)

		then:
		1 * store.set(p, 987)
	}

	def 'can get given value'(){

		given:
		store.get(p) >> 321

		expect:
		p.get() == 321
	}
}
