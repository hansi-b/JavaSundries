package org.hansib.sundries.prefs

import spock.lang.Specification

public class OptBigDecimalSpec extends Specification {

	Prefs<TestKey> store = Mock()
	OptBigDecimal<TestKey> p = new OptBigDecimal(TestKey.one, store)

	def 'defaults to empty value'(){

		expect:
		p.get() == Optional.empty()
	}

	def 'can set value'(){

		when:
		p.set(new BigDecimal('4.5'))

		then:
		1 * store.set(p, new BigDecimal('4.5'))
	}

	def 'can get given value'(){

		given:
		store.get(p) >> new BigDecimal('64.5')

		expect:
		p.get() == Optional.of(new BigDecimal('64.5'))
	}
}
