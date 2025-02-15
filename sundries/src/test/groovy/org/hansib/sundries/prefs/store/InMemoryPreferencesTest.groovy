package org.hansib.sundries.prefs.store

import org.hansib.sundries.preferences.InMemoryPreferences
import spock.lang.Specification

class InMemoryPreferencesTest extends Specification {

	def 'test put and get'() {
		given:
		def prefs = InMemoryPreferences.createRoot()

		when:
		prefs.putBoolean('key1', true)
		prefs.put('key2', 'value2')

		then:
		prefs.getBoolean('key1', false)
		prefs.get('key2', 'noVal') == 'value2'
	}

	def 'can add and access preference in child node'() {
		given:
		def prefs = InMemoryPreferences.createRoot()

		when:
		prefs.node('child').putBoolean('key1', true)

		then:
		prefs.nodeExists('child')
		prefs.node('child').getBoolean('key1', false)
	}
}
