package org.hansib.sundries.prefs

import spock.lang.Specification

public class ReqBigDecimalSpec extends Specification {

	Prefs<TestKey> store = Mock()
	ReqBigDecimal<TestKey> p = new ReqBigDecimal(TestKey.big, store)

	def 'defaults to initial value'(){

		expect:
		p.get() == null
	}

	def 'can set value'(){

		when:
		p.set(new BigDecimal('6.78'))

		then:
		1 * store.set(p, new BigDecimal('6.78'))
	}

	def 'can get given value'(){

		given:
		store.get(p) >> new BigDecimal('9.12')

		expect:
		p.get() == new BigDecimal('9.12')
	}
}
