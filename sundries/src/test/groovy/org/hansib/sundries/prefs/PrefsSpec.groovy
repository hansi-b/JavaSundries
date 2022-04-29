package org.hansib.sundries.prefs

import org.hansib.sundries.prefs.store.InMemoryPrefsStore

import spock.lang.Specification

public class PrefsSpec extends Specification {

	def 'can build with strings'() {

		given:
		def builder = new Prefs.Builder<>(TestKeys.class, new InMemoryPrefsStore())

		when:
		def prefs = builder.optionalString(TestKeys.opt).requiredString(TestKeys.req, 'abc').build()

		then:
		prefs.getPref(TestKeys.opt) instanceof OptString
		prefs.getPref(TestKeys.req) instanceof ReqString
	}

	def 'throws exception on duplicate key'() {

		given:
		def builder = new Prefs.Builder<>(TestKeys.class, new InMemoryPrefsStore())

		when:
		def prefs = builder.optionalString(TestKeys.opt).requiredString(TestKeys.opt, 'abc').build()

		then:
		thrown IllegalArgumentException
	}

	def 'throws exception on missing key'() {

		given:
		def builder = new Prefs.Builder<>(TestKeys.class, new InMemoryPrefsStore())

		when:
		def prefs = builder.optionalString(TestKeys.opt).build()

		then:
		thrown IllegalStateException
	}

	def 'can build with integers'() {

		given:
		def builder = new Prefs.Builder<>(TestKeys.class, new InMemoryPrefsStore())

		when:
		def prefs = builder.optionalInteger(TestKeys.opt).requiredInteger(TestKeys.req, 24).build()

		then:
		prefs.getPref(TestKeys.opt) instanceof OptInteger
		prefs.getPref(TestKeys.req) instanceof ReqInteger
	}

	def 'can build with bools'() {

		given:
		def builder = new Prefs.Builder<>(TestKeys.class, new InMemoryPrefsStore())

		when:
		def prefs = builder.optionalBoolean(TestKeys.opt).requiredBoolean(TestKeys.req, true).build()

		then:
		prefs.getPref(TestKeys.opt) instanceof OptBoolean
		prefs.getPref(TestKeys.req) instanceof ReqBoolean
	}

	def 'can build with BigDecimals'() {

		given:
		def builder = new Prefs.Builder<>(TestKeys.class, new InMemoryPrefsStore())

		when:
		def prefs = builder.optionalBigDecimal(TestKeys.opt).requiredBigDecimal(TestKeys.req, new BigDecimal('123')).build()

		then:
		prefs.getPref(TestKeys.opt) instanceof OptBigDecimal
		prefs.getPref(TestKeys.req) instanceof ReqBigDecimal
	}

	def 'can build with enum'() {

		given:
		def builder = new Prefs.Builder<>(TestKeys.class, new InMemoryPrefsStore())

		when:
		def prefs = builder.optionalEnum(TestKeys.opt, TestValues.class).requiredEnum(TestKeys.req, TestValues.class, TestValues.one).build()

		then:
		prefs.getPref(TestKeys.opt) instanceof OptEnum
		prefs.getPref(TestKeys.req) instanceof ReqEnum
	}

	def 'can build with files'() {

		given:
		def builder = new Prefs.Builder<>(TestKeys.class, new InMemoryPrefsStore())

		when:
		def prefs = builder.optionalFile(TestKeys.opt).requiredFile(TestKeys.req, new File('abc.txt')).build()

		then:
		prefs.getPref(TestKeys.opt) instanceof OptFile
		prefs.getPref(TestKeys.req) instanceof ReqFile
	}
}
