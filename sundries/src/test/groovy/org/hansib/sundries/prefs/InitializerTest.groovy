package org.hansib.sundries.prefs

import spock.lang.Specification

enum Keys {
	oneKey,twoKey
}

public class InitializerTest extends Specification {

	Prefs<Keys> store = Mock()
	Pref<Keys, Integer> pref = Mock()

	def 'sets initial value'() {

		given:
		pref.store() >> store
		store.get(pref) >> null

		when:
		def x = new Initializer().withInitial(pref, 33)

		then:
		1 * store.set(pref, 33)
		x.is(pref)
	}

	def 'does not set initial value when set'() {

		given:
		pref.store() >> store
		store.get(pref) >> 777

		when:
		def x = new Initializer().withInitial(pref, 33)

		then:
		0 * store.set(_, _)
		x.is(pref)
	}
}
