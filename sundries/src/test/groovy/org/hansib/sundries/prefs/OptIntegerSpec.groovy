package org.hansib.sundries.prefs

import spock.lang.Specification

import org.hansib.sundries.prefs.store.PrefsStore

public class OptIntegerSpec extends Specification {

	PrefsStore store = Mock()
	OptInteger p = new OptInteger('integer', store)

	def 'defaults to empty value'() {

		expect:
		p.get() == Optional.empty()
	}

	def 'empty value throws NPE for intValue'() {

		when:
		p.intValue()
		then:
		thrown NullPointerException
	}

	def 'can set value'() {

		when:
		p.set(34)

		then:
		1 * store.put(p.key(), '34')
	}

	def 'can get given value'() {

		given:
		store.get(p.key()) >> '78'

		expect:
		p.get() == Optional.of(78)
		p.intValue() == 78
	}
}
