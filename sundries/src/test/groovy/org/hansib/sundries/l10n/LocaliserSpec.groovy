package org.hansib.sundries.l10n;

import spock.lang.Specification

public class LocaliserSpec extends Specification {

	enum Buttons implements FormatKey {
		one, other, third
	}

	def 'can add and format'() {
		when:
		def l = new Localiser()
		l.add(Buttons.one, 'hello world')
		then:
		l.fmt(Buttons.one) == 'hello world'
	}

	def 'duplicate addition throws exception'() {
		when:
		def l = new Localiser()
		l.add(Buttons.one, 'hi {1}')
		l.add(Buttons.one, 'hello world')
		then:
		IllegalArgumentException ex = thrown()
		ex.message == "Duplicate format mapping for 'one' ('Buttons') in class org.hansib.sundries.l10n.Localiser: new 'hi {1}', old 'hello world'"
	}

	def 'can use message format replacement'() {
		when:
		def l = new Localiser()
		l.add(Buttons.one, 'hello {1} - {0}')
		then:
		l.fmt(Buttons.one, 27, 'mars') == 'hello mars - 27'
	}

	def 'without mapping default to class + key + args'() {
		expect:
		new Localiser().fmt(Buttons.one, 27, 'mars') == 'Buttons:one[27, mars]'
	}
}
