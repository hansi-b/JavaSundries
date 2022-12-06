package org.hansib.sundries.l10n;

import spock.lang.Specification

public class L10nMapReaderSpec extends Specification {

	enum MenuItems {
		Open, Exit
	}

	def 'can read simple yaml'() {
		expect:
		new L10nMapReader(new Domain().with(MenuItems.class)).read('''MenuItems:
    Open: Ouvrir
    Exit: Sortir
OtherStuff:
    1a: Hello'World
    2b: "Guten Tag"
''') == null
	}
}
