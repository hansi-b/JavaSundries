package org.hansib.sundries.prefs

import spock.lang.Specification

import org.hansib.sundries.prefs.store.PrefsStore

public class OptBigDecimalSpec extends Specification {

	PrefsStore store = Mock()
	OptBigDecimal p = new OptBigDecimal('big', store)

	def 'defaults to empty value'() {

		expect:
		p.get() == Optional.empty()
	}

	def 'can set value'() {

		when:
		p.set(new BigDecimal('4.5'))

		then:
		1 * store.put(p.key(), '4.5')
	}

	def 'can get given value'() {

		given:
		store.get(p.key()) >> new BigDecimal('64.5')

		expect:
		p.get() == Optional.of(new BigDecimal('64.5'))
	}
}
