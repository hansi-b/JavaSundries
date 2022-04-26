package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.PrefsStore

import spock.lang.Specification

public class OptEnumSpec extends Specification {

	PrefsStore store = Mock()
	OptEnum<TestValues> p = new OptEnum('enumval', TestValues.class, store)

	def 'defaults to empty value'(){

		expect:
		p.get() == Optional.empty()
	}

	def 'can set value'(){

		when:
		p.set(TestValues.two)

		then:
		1 * store.put(p.key(), 'two')
	}

	def 'can get given value'(){

		given:
		store.get(p.key()) >> TestValues.one

		expect:
		p.get() == Optional.of(TestValues.one)
	}
}
