package org.hansib.sundries.prefs

import spock.lang.Specification

import org.hansib.sundries.prefs.store.PrefsStore

public class OptBooleanSpec extends Specification {

	PrefsStore store = Mock()
	OptBoolean p = new OptBoolean('bool', store)

	def 'defaults to empty value'() {

		expect:
		p.get() == Optional.empty()
	}

	def 'can set value'() {

		when:
		p.set(true)

		then:
		1 * store.put(p.key(), 'true')
	}

	def 'can get given value'() {

		given:
		store.get(p.key()) >> 'false'

		expect:
		p.get() == Optional.of(false)
	}

	def 'primitive access'() {
		given:
		store.get(p.key()) >> 'false'

		expect:
		p.isFalse() == true
		p.isTrue() == false
	}
}
