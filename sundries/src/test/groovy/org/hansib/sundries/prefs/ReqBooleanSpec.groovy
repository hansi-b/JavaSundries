package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.PrefsStore

import spock.lang.Specification

public class ReqBooleanSpec extends Specification {

	PrefsStore store = Mock()
	ReqBoolean p = new ReqBoolean('bool', store)

	def 'defaults to initial value'() {

		expect:
		p.get() == null
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
		p.get() == false
	}

	def 'primitive access'() {
		given:
		store.get(p.key()) >> false

		expect:
		p.isFalse() == true
		p.isTrue() == false
	}
}
