package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.PrefsStore

import spock.lang.Specification

public class OptStringSpec extends Specification {

	PrefsStore store = Mock()
	OptString p = new OptString('str', store)

	def 'defaults to empty value'(){

		expect:
		p.get() == Optional.empty()
	}

	def 'can set value'(){

		when:
		p.set('xyz')

		then:
		1 * store.put(p.key(), 'xyz')
	}

	def 'can get given value'(){

		given:
		store.get(p.key()) >> 'abc'

		expect:
		p.get() == Optional.of('abc')
		p.isSet() == true
	}

	def 'can remove value'(){

		when:
		p.unset()

		then:
		1 * store.remove(p.key())
		p.isSet() == false
	}
}
