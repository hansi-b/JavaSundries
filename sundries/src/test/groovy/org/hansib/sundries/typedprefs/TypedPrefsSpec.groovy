package org.hansib.sundries.typedprefs

import org.hansib.sundries.typedprefs.TypedPrefs
import org.hansib.sundries.typedprefs.StringPref

import spock.lang.Specification

public class TypedPrefsSpec extends Specification {

	enum SingleKey {
		onlyOne
	}

	enum TwoKeys{
		one, two
	}

	def 'can build with strings'() {

		given:
		def builder = new TypedPrefs.Builder<>(SingleKey.class)

		when:
		def prefs = builder.stringPref(SingleKey.onlyOne, 'xyz').build()

		then:
		prefs.pref(SingleKey.onlyOne) instanceof StringPref
		prefs.get(SingleKey.onlyOne) == 'xyz'
	}

	def 'throws exception on duplicate key'() {

		given:
		def builder = new TypedPrefs.Builder<>(SingleKey.class)

		when:
		def prefs = builder.stringPref(SingleKey.onlyOne, 'xyz').stringPref(SingleKey.onlyOne, 'abc').build()

		then:
		thrown IllegalArgumentException
	}

	def 'throws exception on missing key'() {

		given:
		def builder = new TypedPrefs.Builder<>(TwoKeys.class)

		when:
		def prefs = builder.stringPref(TwoKeys.one, 'xyz').build()

		then:
		thrown IllegalStateException
	}
}
