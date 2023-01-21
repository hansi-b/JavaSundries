package org.hansib.sundries.l10n;

import org.hansib.sundries.Strings

import spock.lang.Specification

public class MissingKeysSpec extends Specification {

	enum Blubb {
		onceAgain, actuallyNever, lastOne
	}

	def 'getDescription'() {
		when:
		def mk = new MissingKeys(Blubb, EnumSet.of( //
				Blubb.actuallyNever,
				Blubb.onceAgain,
				Blubb.lastOne))
		then:
		mk.description() == Strings.toSystemEol('''Blubb:
	onceAgain
	actuallyNever
	lastOne
''')
	}
}
