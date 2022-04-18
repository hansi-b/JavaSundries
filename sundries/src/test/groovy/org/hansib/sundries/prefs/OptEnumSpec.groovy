package org.hansib.sundries.prefs

import spock.lang.Specification

public class OptEnumSpec extends Specification {

	Prefs<TestKey> prefs = Mock()
	OptEnum<TestKey, TestValues> p = new OptEnum(TestKey.enumval, TestValues.class, prefs)

	def 'defaults to empty value'(){

		expect:
		p.get() == Optional.empty()
	}

	def 'can set value'(){

		when:
		p.set(TestValues.two)

		then:
		1 * prefs.set(p, TestValues.two)
	}

	def 'can get given value'(){

		given:
		prefs.get(p) >> TestValues.one

		expect:
		p.get() == Optional.of(TestValues.one)
	}
}
