package org.hansib.sundries.l10n.yaml

import java.util.function.Consumer

import org.hansib.sundries.l10n.L10n
import org.hansib.sundries.l10n.L10nCheckerSpec.MenuItems
import org.hansib.sundries.l10n.L10nCheckerSpec.OtherItems
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError
import org.hansib.sundries.l10n.yaml.errors.ParseError

import spock.lang.Specification

public class L10nReaderSpec extends Specification {

	enum Items {
		Alpha, Beta
	}

	def 'wrong root node creates ParseError'() {
		given:
		L10n l10n = Mock()
		Consumer<L10nFormatError> errHandler = Mock()

		def lmr = new L10nReader(l10n, 'locale', errHandler)


		when:
		def on = lmr.readEnum('''
  - Open: Ouvrir
  - Exit: 'Sortir'
''', null)

		then:
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof ParseError
			def loc = ((ParseError)err).error.location
			assert loc.lineNr == 2
			assert loc.columnNr == 3
		}
		0 * l10n.withFormats(_)
	}

	def 'later parse exception skips all enums'() {
		given:
		L10n l10n = Mock()
		Consumer<L10nFormatError> errHandler = Mock()

		def lmr = new L10nReader(l10n, 'locale', errHandler)

		when:
		def on = lmr.readEnum('''
  Open:
    Ouvrir: Yep
  MenuItems'
''', null)

		then:
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof ParseError
			def loc = ((ParseError)err).error.location
			assert loc.lineNr == 3
			assert loc.columnNr == 16
		}
		0 * l10n.withFormats(_)
	}

	def 'can read locale'() {
		given:
		L10n l10n = Spy(new L10n().with(MenuItems).with(OtherItems))
		Consumer<L10nFormatError> errHandler = Mock()

		def lmr = new L10nReader(l10n, 'english', errHandler)

		when:
		def on = lmr.readEnum('''
  Alpha:
    english: 1st key
  Beta:
    english: 2nd key
    german: ignored
''', Items)

		then:
		1 * l10n.withFormats(_) >> { EnumMap map ->
			assert map == [(Items.Alpha):'1st key', (Items.Beta): '2nd key']
		}
		0 * errHandler.accept(_)
	}
}
