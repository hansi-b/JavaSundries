package org.hansib.sundries.l10n;

import java.util.function.Consumer

import org.hansib.sundries.l10n.L10nChecker.MissingKeysHandleMode

import spock.lang.Specification

public class L10nCheckerSpec extends Specification {

	enum MenuItems {
		Open, Close
	}

	enum OtherItems {
		Alpha, Beta, Gamma
	}

	def 'can get empty missing keys'() {

		given:
		Domain domain = new Domain().with(MenuItems.class)
		L10n l10n = new L10n(domain)
		def lmc = new L10nChecker(l10n)
		l10n.add(MenuItems.Open, "Open")
		l10n.add(MenuItems.Close, "Close")

		Consumer<MissingKeys<?>> handler = Mock()

		when:
		lmc.checkCompleteness(handler, MissingKeysHandleMode.AlsoWithoutMissingKeys)

		then:
		1 * handler.accept(_) >> { MissingKeys<?> m ->
			assert m.enumClz == MenuItems
			assert m.missing.isEmpty()
		}
	}

	def 'can skip empty missing keys'() {

		given:
		Domain domain = new Domain().with(MenuItems.class)
		L10n l10n = new L10n(domain)
		def lmc = new L10nChecker(l10n)
		l10n.add(MenuItems.Open, "Open")
		l10n.add(MenuItems.Close, "Close")

		Consumer<MissingKeys<?>> handler = Mock()

		when:
		lmc.checkCompleteness(handler, MissingKeysHandleMode.OnlyWithMissingKeys)

		then:
		0 * handler.accept(_)
	}

	def 'can get keys from multiple enums'() {

		given:
		Domain domain = new Domain().with(MenuItems.class).with(OtherItems.class)
		L10n l10n = new L10n(domain)
		def lmc = new L10nChecker(l10n)
		l10n.add(MenuItems.Close, "Close")
		l10n.add(OtherItems.Gamma, "Gamma")

		Consumer<MissingKeys<?>> handler = Mock()

		when:
		lmc.checkCompleteness(handler, MissingKeysHandleMode.OnlyWithMissingKeys)

		then:
		1 * handler.accept(_) >> { MissingKeys<?> m ->
			assert m.enumClz == MenuItems
			assert m.missing == [MenuItems.Open,] as Set
		}
		1 * handler.accept(_) >> { MissingKeys<?> m ->
			assert m.enumClz == OtherItems
			assert m.missing == [
				OtherItems.Alpha,
				OtherItems.Beta
			] as Set
		}
	}
}
