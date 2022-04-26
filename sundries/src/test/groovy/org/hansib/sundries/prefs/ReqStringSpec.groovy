package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.PrefsStore

import spock.lang.Specification

public class ReqStringSpec extends Specification {

	PrefsStore store = Mock()
	ReqString p = new ReqString('str', store)

	def 'defaults to initial value'(){

		expect:
		p.get() == null
	}

	def 'can set value'(){

		when:
		p.set('aaa')

		then:
		1 * store.put(p.key(), 'aaa')
	}
	
	
	def 'throws exception on setting to null'(){

		when:
		p.set(null)

		then:
		thrown IllegalArgumentException
	}


	def 'can get given value'(){

		given:
		store.get(p.key()) >> 'abc'

		expect:
		p.get() == 'abc'
	}
}
