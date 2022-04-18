package org.hansib.sundries.prefs

import spock.lang.Specification

public class OptStringSpec extends Specification {

	Prefs<TestKey> store = Mock()
	OptString<TestKey> p = new OptString(TestKey.str, store)

	def 'defaults to empty value'(){

		expect:
		p.get() == Optional.empty()
	}

	def 'can set value'(){

		when:
		p.set('xyz')

		then:
		1 * store.set(p, 'xyz')
	}

	def 'can get given value'(){

		given:
		store.get(p) >> 'abc'

		expect:
		p.get() == Optional.of('abc')
		p.isSet() == true
	}

	def 'can remove value'(){

		when:
		p.remove()

		then:
		1 * store.remove(p)
		p.isSet() == false
	}
}
