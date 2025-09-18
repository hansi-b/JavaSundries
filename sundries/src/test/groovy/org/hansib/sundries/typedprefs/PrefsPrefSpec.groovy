package org.hansib.sundries.typedprefs;

import spock.lang.Specification

public class PrefsPrefSpec extends Specification {

	enum PrefKeys {
		width, height
	}

	TypedPrefs<PrefKeys> prefsValue = new TypedPrefs.Builder(PrefKeys.class)
	.stringPref(PrefKeys.width, "abc").stringPref(PrefKeys.height, "xyz").build()

	PrefsPref<PrefKeys> p = new PrefsPref(prefsValue)

	def 'can set and get'() {

		expect:
		p.get() == prefsValue
		p.get().get(PrefKeys.width) == 'abc'
	}
}
