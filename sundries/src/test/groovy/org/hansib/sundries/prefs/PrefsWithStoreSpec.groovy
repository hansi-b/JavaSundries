package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.InMemoryPrefsStore

import spock.lang.Specification

public class PrefsWithStoreSpec extends Specification {

	PrefsWithStore<TestKey> prefs = new PrefsWithStore<>(new InMemoryPrefsStore());

	def 'can get initialised val'(){

		when:
		ReqInteger<TestKey> p = prefs.requiredInteger(TestKey.integer, 78)

		then:
		prefs.get(p) == 78
	}

	def 'can get not-set val'(){

		when:
		OptBoolean<TestKey> p = prefs.optionalBoolean(TestKey.bool)

		then:
		prefs.get(p) == null
	}

	def 'can set val'(){

		when:
		ReqString<TestKey> p = prefs.requiredString(TestKey.str, 'abc')
		p.set('xyz')

		then:
		prefs.get(p) == 'xyz'
	}

	def 'throws exception on setting to null'(){

		when:
		OptString<TestKey> p = prefs.optionalString(TestKey.str)
		p.set(null)

		then:
		thrown IllegalArgumentException
	}

	def 'can set and remove optional val'(){

		given:
		OptFile<TestKey> p = prefs.optionalFile(TestKey.file)

		when:
		p.set(new File('../else'))

		then:
		prefs.get(p) == new File('../else')
		prefs.contains(p) == true

		when:
		prefs.remove(p)

		then:
		prefs.get(p) == null
		prefs.contains(p) == false
	}
}
