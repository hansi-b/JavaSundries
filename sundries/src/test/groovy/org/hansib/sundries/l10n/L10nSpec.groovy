package org.hansib.sundries.l10n;

import spock.lang.Specification

public class L10nSpec extends Specification {

	enum Buttons implements FormatKey {
		open, close
	}
	enum Stuff implements FormatKey {
		open, close
	}

	Domain buttonsDomain = new Domain().with(Buttons.class)

	def 'can add key for domain'() {

		given:
		Localiser localiser = Mock()
		when:
		def l10n = new L10n(buttonsDomain, localiser).add(Buttons.open, 'ouvrir')
		then:
		1 * localiser.add(Buttons.open, 'ouvrir')
	}

	def 'key from wrong domain is rejected'() {

		when:
		new L10n(buttonsDomain).add(Stuff.open, 'ouvrir')

		then:
		thrown IllegalArgumentException
	}
}
