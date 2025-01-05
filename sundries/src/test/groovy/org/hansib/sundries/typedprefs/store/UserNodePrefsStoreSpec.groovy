package org.hansib.sundries.typedprefs.store

import java.util.prefs.Preferences

import org.hansib.sundries.preferences.InMemoryPreferences
import org.hansib.sundries.typedprefs.TypedPrefs

import spock.lang.Specification

enum TestKeys {
	SomePref
}

enum EnumPrefKeys {
	One, Two
}

enum PrefsPrefKeys {
	OnePref, TwoPref
}

class UserNodePrefsStoreSpec extends Specification {

	Preferences backingPreferences = InMemoryPreferences.createRoot()
	UserNodePrefsStore store = new UserNodePrefsStore(backingPreferences)

	def 'can save and load StringPref'() {

		given:
		TypedPrefs prefs = new TypedPrefs.Builder(TestKeys.class).stringPref(TestKeys.SomePref, 'initialString').build()

		when:
		prefs.pref(TestKeys.SomePref).set('not initial value')
		store.save(prefs)

		then:
		backingPreferences.get(TestKeys.SomePref.name(), null) == 'not initial value'

		when:
		backingPreferences.put(TestKeys.SomePref.name(), 'different')
		store.load(prefs)

		then:
		prefs.get(TestKeys.SomePref) == 'different'
	}

	def 'can save and load BooleanPref'() {

		given:
		TypedPrefs prefs = new TypedPrefs.Builder(TestKeys.class).booleanPref(TestKeys.SomePref, false).build()

		when:
		prefs.pref(TestKeys.SomePref).set(true)
		store.save(prefs)

		then:
		backingPreferences.getBoolean(TestKeys.SomePref.name(), false)

		when:
		backingPreferences.putBoolean(TestKeys.SomePref.name(), false)
		store.load(prefs)

		then:
		!prefs.get(TestKeys.SomePref)
	}

	def 'can save and load EnumPref'() {

		given:
		TypedPrefs prefs = new TypedPrefs.Builder(TestKeys.class).enumPref(TestKeys.SomePref, EnumPrefKeys).build()

		when:
		prefs.pref(TestKeys.SomePref).set(EnumPrefKeys.Two)
		store.save(prefs)

		then:
		backingPreferences.get(TestKeys.SomePref.name(), null) == 'Two'

		when:
		backingPreferences.put(TestKeys.SomePref.name(), 'One')
		store.load(prefs)

		then:
		prefs.get(TestKeys.SomePref) == EnumPrefKeys.One
	}
}