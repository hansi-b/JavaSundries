package org.hansib.sundries.l10n;

import spock.lang.Specification

public class DomainSpec extends Specification {

	enum Buttons {
		one, two
	}
	enum Birds {
		one, two
	}

	def 'can determine mapping status of key'() {
		expect:
		new Domain().with(Buttons.class).maps(Buttons.one)
		!new Domain().with(Buttons.class).maps(Birds.one)
	}
}
