package org.hansib.sundries.prefs

import spock.lang.Specification

public class ReqStringSpec extends Specification {

	Prefs<TestKey> store = Mock()
	ReqString<TestKey> p = new ReqString(TestKey.one, store)

	def 'defaults to initial value'(){

		expect:
		p.get() == null
	}

	def 'can set value'(){

		when:
		p.set('aaa')

		then:
		1 * store.set(p, 'aaa')
	}

	def 'can get given value'(){

		given:
		store.get(p) >> 'abc'

		expect:
		p.get() == 'abc'
	}
}
