package org.hansib.sundries.prefs

import spock.lang.Specification

enum Keys {
	oneKey
}

public class InitializerTest extends Specification {

	Prefs<Keys> prefs = Mock()
	Pref<Keys, Integer> pref = Mock()

	def 'sets initial value'() {

		given:
		pref.prefs() >> prefs
		prefs.get(pref) >> null

		when:
		def x = new Initializer().withInitial(pref, 33)

		then:
		1 * prefs.set(pref, 33)
		x.is(pref)
	}

	def 'does not set initial value when set'() {

		given:
		pref.prefs() >> prefs
		prefs.get(pref) >> 777

		when:
		def x = new Initializer().withInitial(pref, 33)

		then:
		0 * prefs.set(_, _)
		x.is(pref)
	}
}
