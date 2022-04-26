package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.InMemoryPrefsStore

import spock.lang.Shared
import spock.lang.Specification

public class PrefsSpec extends Specification {

	Prefs<TestKey> prefs = new Prefs(new InMemoryPrefsStore())
	@Shared Prefs<TestKey> sharedPrefs = new Prefs(new InMemoryPrefsStore())

	def 'can get initialised val'(){

		when:
		ReqInteger p = prefs.requiredInteger(TestKey.integer, 78)

		then:
		prefs.get(TestKey.integer) == '78'
	}

	def 'can get not-set val'(){

		when:
		OptBoolean p = prefs.optionalBoolean(TestKey.bool)

		then:
		prefs.get(TestKey.bool) == null
	}

	def 'can set val'(){

		when:
		ReqString p = prefs.requiredString(TestKey.str, 'abc')
		p.set('xyz')

		then:
		prefs.get(TestKey.str) == 'xyz'
	}

	def 'factory methods return correct opt types'() {

		expect:
		prefs.optionalString(TestKey.str) instanceof OptString
		prefs.optionalBoolean(TestKey.bool) instanceof OptBoolean
		prefs.optionalInteger(TestKey.integer) instanceof OptInteger
		prefs.optionalBigDecimal(TestKey.big) instanceof OptBigDecimal
		prefs.optionalEnum(TestKey.enumval, TestValues.class) instanceof OptEnum
		prefs.optionalFile(TestKey.file) instanceof OptFile
	}

	def 'factory methods return correct req types with initial values'() {

		expect:
		pref.getClass() == clazz
		pref.get() == iniVal

		where:
		pref | clazz | iniVal
		sharedPrefs.requiredString(TestKey.str, 'azz') | ReqString | 'azz'
		sharedPrefs.requiredBoolean(TestKey.bool, true) | ReqBoolean | true
		sharedPrefs.requiredInteger(TestKey.integer, 123) | ReqInteger | 123
		sharedPrefs.requiredBigDecimal(TestKey.big, new BigDecimal(1233)) | ReqBigDecimal | new BigDecimal(1233)
		sharedPrefs.requiredEnum(TestKey.enumval, TestValues.class, TestValues.one) | ReqEnum | TestValues.one
		sharedPrefs.requiredFile(TestKey.file, new File('file')) | ReqFile | new File('file')
	}
}
