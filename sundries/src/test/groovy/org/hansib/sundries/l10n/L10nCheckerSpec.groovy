package org.hansib.sundries.l10n;

import java.util.function.Consumer

import org.hansib.sundries.Strings
import org.hansib.sundries.l10n.L10nChecker.MissingKeys
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
		L10n l10n = new L10n().with(MenuItems.class)
		l10n.withFormat(MenuItems.Open, "Open")
		l10n.withFormat(MenuItems.Close, "Close")

		def lmc = new L10nChecker(l10n)
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
		L10n l10n = new L10n().with(MenuItems.class)
		l10n.withFormat(MenuItems.Open, "Open")
		l10n.withFormat(MenuItems.Close, "Close")

		def lmc = new L10nChecker(l10n)
		Consumer<MissingKeys<?>> handler = Mock()

		when:
		lmc.checkCompleteness(handler, MissingKeysHandleMode.OnlyWithMissingKeys)

		then:
		0 * handler.accept(_)
	}

	def 'can get keys from multiple enums'() {

		given:
		L10n l10n = new L10n().with(MenuItems.class).with(OtherItems.class)
		def lmc = new L10nChecker(l10n)
		l10n.withFormat(MenuItems.Close, "Close")
		l10n.withFormat(OtherItems.Gamma, "Gamma")

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
