package org.hansib.sundries.l10n.yaml

import spock.lang.Specification

import java.util.function.Consumer

import org.hansib.sundries.l10n.yaml.errors.DuplicateEnumValue
import org.hansib.sundries.l10n.yaml.errors.DuplicateLocaleValue
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError
import org.hansib.sundries.l10n.yaml.errors.MissingLocaleValue
import org.hansib.sundries.l10n.yaml.errors.UnknownEnumKey

public class EnumMapperSpec extends Specification {

	enum TwoItems {
		First, Second
	}

	enum Items {
		Abc
	}

	def 'can read simple maps'() {

		given:
		Consumer<L10nFormatError> errHandler = Mock()
		def mapper = new EnumMapper(errHandler, 'en')

		when:
		def res = mapper.loadMapping([
				new Entry('First', [
						new Entry('en', 'anglais'),
						new Entry('fr', 'francais')
				]),
				new Entry('Second', [
						new Entry('en', '42'),
						new Entry('fr', '35')
				])
		], TwoItems)
		then:
		res == [(TwoItems.First): 'anglais', (TwoItems.Second): '42']
		0 * errHandler.accept(_)
	}

	def 'duplicate enum key triggers error and is ignored'() {

		given:
		Consumer<L10nFormatError> errHandler = Mock()
		def mapper = new EnumMapper(errHandler, 'en')

		when:
		def res = mapper.loadMapping([
				new Entry('Abc', [
						new Entry('en', 'anglais'),
				]),
				new Entry('Abc', [
						new Entry('de', 'entirely different'),
				])
		], Items)
		then:
		res == [(Items.Abc): 'anglais']
		1 * errHandler.accept(_) >> { L10nFormatError obj ->
			assert obj instanceof DuplicateEnumValue
			def err = ((DuplicateEnumValue) obj)
			assert err.key == Items.Abc
			assert err.activeValue == 'anglais'
		}
	}

	def 'unknown enum key triggers error and is ignored'() {

		given:
		Consumer<L10nFormatError> errHandler = Mock()
		def mapper = new EnumMapper(errHandler, 'en')

		when:
		def res = mapper.loadMapping([
				new Entry('Unknown', [
						new Entry('en', 'nope'),
				]),
				new Entry('Abc', [
						new Entry('en', 'anglais'),
				])
		], Items)
		then:
		res == [(Items.Abc): 'anglais']
		1 * errHandler.accept(_) >> { L10nFormatError obj ->
			assert obj instanceof UnknownEnumKey
			def err = ((UnknownEnumKey) obj)
			assert err.enumClz == Items
			assert err.keyStr == 'Unknown'
		}
	}

	def 'missing locale value triggers error and is ignored'() {

		given:
		Consumer<L10nFormatError> errHandler = Mock()
		def mapper = new EnumMapper(errHandler, 'en')

		when:
		def res = mapper.loadMapping([
				new Entry('Abc', [
						new Entry('fr', 'anglais'),
				])
		], Items)
		then:
		res.isEmpty()
		1 * errHandler.accept(_) >> { L10nFormatError obj ->
			assert obj instanceof MissingLocaleValue
			def err = ((MissingLocaleValue) obj)
			assert err.enumkey == Items.Abc
			assert err.localeKey == 'en'
		}
	}

	def 'duplicate locale value triggers error and is ignored'() {

		given:
		Consumer<L10nFormatError> errHandler = Mock()
		def mapper = new EnumMapper(errHandler, 'en')

		when:
		def res = mapper.loadMapping([
				new Entry('Abc', [
						new Entry('en', 'anglais'),
						new Entry('en', 'another'),
				])
		], Items)
		then:
		res == [(Items.Abc): 'anglais']
		1 * errHandler.accept(_) >> { L10nFormatError obj ->
			assert obj instanceof DuplicateLocaleValue
			def err = ((DuplicateLocaleValue) obj)
			assert err.enumkey == Items.Abc
			assert err.localeKey == 'en'
			assert err.activeValue == 'anglais'
			assert err.duplicateValue == 'another'
		}
	}
}
