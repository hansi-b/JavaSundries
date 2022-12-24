package org.hansib.sundries.l10n.yaml

import java.util.function.Consumer

import org.hansib.sundries.l10n.Domain
import org.hansib.sundries.l10n.L10n
import org.hansib.sundries.l10n.yaml.errors.DuplicateEnum
import org.hansib.sundries.l10n.yaml.errors.DuplicateEnumValue
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError
import org.hansib.sundries.l10n.yaml.errors.ParseError
import org.hansib.sundries.l10n.yaml.errors.UnknownEnum
import org.hansib.sundries.l10n.yaml.errors.UnknownEnumKey

import spock.lang.Specification

public class L10nReaderSpec extends Specification {

	enum MenuItems {
		Open, Exit, Missing
	}
	enum OtherItems {
		Alpha, Beta, Gamma
	}

	def 'wrong root node creates ParseError'() {
		given:
		L10n l10n = Mock()
		def lmr = new L10nReader(l10n)

		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  - Open: Ouvrir
  - Exit: 'Sortir'
''', errHandler)

		then:
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof ParseError
			def loc = ((ParseError)err).error.location
			assert loc.lineNr == 2
			assert loc.columnNr == 3
		}
		0 * l10n.addAll(_)
	}

	def 'later parse exception skips all enums'() {
		given:
		L10n l10n = Mock()

		def lmr = new L10nReader(l10n)
		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  Open:
    Ouvrir: Yep
  MenuItems'
''', errHandler)

		then:
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof ParseError
			def loc = ((ParseError)err).error.location
			assert loc.lineNr == 3
			assert loc.columnNr == 16
		}
		0 * l10n.addAll(_)
	}

	def 'can read multiple enums'() {
		given:
		Domain domain = new Domain().with(MenuItems).with(OtherItems)
		L10n l10n = Spy(new L10n(domain))

		def lmr = new L10nReader(l10n)
		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  OtherItems:
    Beta: 2nd key
    Gamma: 3rd key
  MenuItems:
    Open: Ouvrir {0}
''', errHandler)

		then:
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 2
			assert map[OtherItems.Beta] == '2nd key'
			assert map[OtherItems.Gamma] == '3rd key'
		}
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
			assert map[MenuItems.Open] == 'Ouvrir {0}'
		}
		0 * errHandler.accept(_)
	}

	def 'unknown enum creates error, and reader continues'() {
		given:
		Domain domain = new Domain().with(MenuItems).with(OtherItems)
		L10n l10n = Spy(new L10n(domain))

		def lmr = new L10nReader(l10n)
		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  BlubberItems:
    Beta: 2nd key
    Gamma: 3rd key
  MenuItems:
    Open: Ouvrir {0}
''', errHandler)

		then:
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
			assert map[MenuItems.Open] == 'Ouvrir {0}'
		}
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof UnknownEnum
			assert ((UnknownEnum)err).enumName == 'BlubberItems'
		}
	}

	def 'duplicate enum creates error, is ignored, and reader continues'() {
		given:
		Domain domain = new Domain().with(MenuItems).with(OtherItems)
		L10n l10n = Spy(new L10n(domain))

		def lmr = new L10nReader(l10n)
		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  MenuItems:
    Open: First Entry
  MenuItems:
    Open: Ouvrir {0}
    Exit: Sortir
  OtherItems:
    Alpha: 1st
''', errHandler)

		then:
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
			assert map[MenuItems.Open] == 'First Entry'
		}
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
			assert map[OtherItems.Alpha] == '1st'
		}
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof DuplicateEnum
			assert ((DuplicateEnum)err).enumName == 'MenuItems'
		}
	}

	def 'unknown enum key creates error, is ignored, and reader continues'() {
		given:
		Domain domain = new Domain().with(MenuItems).with(OtherItems)
		L10n l10n = Spy(new L10n(domain))

		def lmr = new L10nReader(l10n)
		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  MenuItems:
    Wired: Ouvrir {0}
    Exit: Sortir
  OtherItems:
    Alpha: 1st
    Delta: non-existent
''', errHandler)

		then:
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
			assert map[MenuItems.Exit] == 'Sortir'
		}
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
			assert map[OtherItems.Alpha] == '1st'
		}
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof UnknownEnumKey
			assert ((UnknownEnumKey)err).enumClz == MenuItems
			assert ((UnknownEnumKey)err).keyStr == 'Wired'
		}
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof UnknownEnumKey
			assert ((UnknownEnumKey)err).enumClz == OtherItems
			assert ((UnknownEnumKey)err).keyStr == 'Delta'
		}
	}

	def 'duplicate enum value creates error, is ignored, and reader continues'() {
		given:
		Domain domain = new Domain().with(MenuItems).with(OtherItems)
		L10n l10n = Spy(new L10n(domain))

		def lmr = new L10nReader(l10n)
		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  MenuItems:
    Exit: Sortir 1
    Exit: Sortir 2
  OtherItems:
    Alpha: 1st
''', errHandler)

		then:
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
			assert map[MenuItems.Exit] == 'Sortir 1'
		}
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
			assert map[OtherItems.Alpha] == '1st'
		}
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof DuplicateEnumValue
			assert ((DuplicateEnumValue)err).key == MenuItems.Exit
			assert ((DuplicateEnumValue)err).activeValue == 'Sortir 1'
			assert ((DuplicateEnumValue)err).duplicateValue == 'Sortir 2'
		}
	}
}
