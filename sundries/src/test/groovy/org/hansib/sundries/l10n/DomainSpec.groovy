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
		new Domain().add(Buttons.class).contains(Buttons.one) == true
		new Domain().add(Buttons.class).contains(MenuItem.one) == false
	}

	def 'can resolve class by simple name'() {
		when:
		def d = new Domain().add(MenuItem.class)
		then:
		d.getKeysClass('MenuItem') == MenuItem.class
		d.getKeysClass('OtherItem') == null
	}

	def 'adding different class with same simple name throws exception'() {
		given:
		def d = new Domain().add(MenuItem.class)
		when:
		d.add(org.hansib.sundries.l10n.MenuItem)
		then:
		thrown IllegalArgumentException
	}
}
