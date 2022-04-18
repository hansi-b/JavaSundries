package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.InMemoryPrefsStore

import spock.lang.Shared
import spock.lang.Specification

public class PrefsSpec extends Specification {

	@Shared Prefs<TestKey> prefs = new PrefsWithStore(new InMemoryPrefsStore())

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
		val.getClass() == clazz
		val.get() == iniVal

		where:
		val | clazz | iniVal
		prefs.requiredString(TestKey.str, 'azz') | ReqString | 'azz'
		prefs.requiredBoolean(TestKey.bool, true) | ReqBoolean | true
		prefs.requiredInteger(TestKey.integer, 123) | ReqInteger | 123
		prefs.requiredBigDecimal(TestKey.big, new BigDecimal(1233)) | ReqBigDecimal | new BigDecimal(1233)
		prefs.requiredEnum(TestKey.enumval, TestValues.class, TestValues.one) | ReqEnum | TestValues.one
		prefs.requiredFile(TestKey.file, new File('file')) | ReqFile | new File('file')
	}
}
