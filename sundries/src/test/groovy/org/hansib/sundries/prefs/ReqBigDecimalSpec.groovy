package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.PrefsStore

import spock.lang.Specification

public class ReqBigDecimalSpec extends Specification {

	PrefsStore store = Mock()
	ReqBigDecimal p = new ReqBigDecimal('big', store)

	def 'defaults to initial value'(){

		expect:
		p.get() == null
	}

	def 'can set value'(){

		when:
		p.set(new BigDecimal('6.78'))

		then:
		1 * store.put(p.key(), '6.78')
	}

	def 'can get given value'(){

		given:
		store.get(p.key()) >> '9.12'

		expect:
		p.get() == new BigDecimal('9.12')
	}
}
