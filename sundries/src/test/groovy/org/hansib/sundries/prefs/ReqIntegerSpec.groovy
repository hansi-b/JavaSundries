package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.PrefsStore

import spock.lang.Specification

public class ReqIntegerSpec extends Specification {

	PrefsStore store = Mock()
	ReqInteger p = new ReqInteger('integer', store)

	def 'defaults to initial value'(){

		expect:
		p.get() == null
	}

	def 'can set value'(){

		when:
		p.set(987)

		then:
		1 * store.put(p.key(), '987')
	}

	def 'can get given value'(){

		given:
		store.get(p.key()) >> 321

		expect:
		p.get() == 321
		p.intValue() == 321
	}
}
