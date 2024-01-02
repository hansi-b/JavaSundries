package org.hansib.sundries.l10n;

import spock.lang.Specification

public class L10nSpec extends Specification {

	enum Buttons implements FormatKey {
		open, close
	}

	enum Stuff implements FormatKey {
		open, close
	}

	Domain buttonsDomain = new Domain()

	def'can add key for domain'() {

		given:
		Localiser localiser = Mock()
		when:
		def l10n = new L10n(buttonsDomain, localiser).with(Buttons.class).withFormat(Buttons.open, 'ouvrir')
		then:
		1 * localiser.setFormat(Buttons.open, 'ouvrir')
		0 * localiser.setFormat(_, _)
	}

	def 'can add all keys from map' () {

		given:
		Localiser localiser = Mock()
		Map maps = [ (Buttons.open): 'ouvrir',(Buttons.close): 'close' ]
		when:
		def l10n = new L10n(buttonsDomain, localiser).with(Buttons.class).withFormats(maps)
		then:
		1 * localiser.setFormat(Buttons.open, 'ouvrir')
		1 * localiser.setFormat(Buttons.close, 'close')
		0 * localiser.setFormat(_, _)
	}

	def 'key from wrong domain is rejected' () {

		when:
		new L10n().with(Buttons.class).withFormat(Stuff.open, 'ouvrir')

		then:
		IllegalArgumentException ex = thrown()
		ex.getMessage() == "Domain 'L10n' does not map class 'Stuff' of key 'open'"
	}

	def 'active localiser must be activated' () {
		given:
		Localiser l1 = Mock()
		def l10n = new L10n(buttonsDomain, l1)

		when:
		l10n.active()

		then:
		NullPointerException ex = thrown()
	}

	def 'can set active localiser across different L10ns' () {

		given:
		Localiser l1 = Mock()
		def l10n_1 = new L10n(buttonsDomain, l1)

		Localiser l2 = Mock()
		def l10n_2 = new L10n(buttonsDomain, l2)

		when:
		l10n_1.activate()
		then:
		l10n_1.active() == l1
		l10n_2.active() == l1
	}
}
