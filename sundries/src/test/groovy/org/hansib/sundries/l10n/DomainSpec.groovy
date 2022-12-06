package org.hansib.sundries.l10n;

import spock.lang.Specification

public class DomainSpec extends Specification {

	enum Buttons {
		one, two
	}
	enum MenuItem {
		one, two
	}

	def 'can determine mapping status of key'() {
		expect:
		new Domain().with(Buttons.class).get(Buttons.one) == Buttons
		new Domain().with(Buttons.class).get(MenuItem.one) == null
	}

	def 'can resolve class by simple name'() {
		when:
		def d = new Domain().with(MenuItem.class)
		then:
		d.get("MenuItem") == MenuItem.class
		d.get("OtherItem") == null
	}

	def 'adding different class with same simple name throws exception'() {
		given:
		def d = new Domain().with(MenuItem.class)
		when:
		d.with(org.hansib.sundries.l10n.MenuItem)
		then:
		thrown IllegalArgumentException
	}
}
