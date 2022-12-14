package org.hansib.sundries.l10n.yaml

import java.util.function.Consumer

import org.hansib.sundries.l10n.Domain
import org.hansib.sundries.l10n.L10n
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError
import org.hansib.sundries.l10n.yaml.errors.ParseError
import org.hansib.sundries.l10n.yaml.errors.UnexpectedRootNode
import org.hansib.sundries.l10n.yaml.errors.UnknownEnum

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import spock.lang.Specification

public class L10nMapReaderSpec extends Specification {

	enum MenuItems {
		Open, Exit, Missing
	}
	enum OtherItems {
		Alpha, Beta, Gamma
	}

	def 'can read enum values'() {
		given:
		def om = new ObjectMapper(new YAMLFactory())

		when:
		def on = om.readTree('''
  Open: Ouvrir
  Exit: 'Sortir'
''')
		then:
		def m = L10nMapReader.readEnumValues(MenuItems, on, e ->{})
		m.size() == 2
		m.get(MenuItems.Open) == 'Ouvrir'
		m.get(MenuItems.Exit) == 'Sortir'
		m.get(MenuItems.Missing) == null
	}

	def 'wrong root node creates UnexpectedRootNode'() {
		given:
		L10n l10n = Mock()
		def lmr = new L10nMapReader(l10n)

		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  - Open: Ouvrir
  - Exit: 'Sortir'
''', errHandler)

		then:
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof UnexpectedRootNode
			assert ((UnexpectedRootNode)err).rootNode().getNodeType() == JsonNodeType.ARRAY
		}
		0 * l10n.addAll(_)
	}

	def 'parse exception creates ParseError'() {
		given:
		L10n l10n = Mock()

		def lmr = new L10nMapReader(l10n)
		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  Open: Ouvrir
  MenuItems'
''', errHandler)

		then:
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof ParseError
		}
		0 * l10n.addAll(_)
	}

	def 'Can read correct values'() {
		given:
		Domain domain = new Domain().with(MenuItems).with(OtherItems)
		L10n l10n = Spy(new L10n(domain))

		def lmr = new L10nMapReader(l10n)
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

	def 'Unknown enum name creates UnknownEnum and continues'() {
		given:
		Domain domain = new Domain().with(MenuItems)
		L10n l10n = Spy(new L10n(domain))

		def lmr = new L10nMapReader(l10n)
		Consumer<L10nFormatError> errHandler = Mock()

		when:
		def on = lmr.read('''
  Ooops: Ouvrir
  MenuItems:
    Open: Ouvrir
''', errHandler)

		then:
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof UnknownEnum
			assert ((UnknownEnum)err).enumName() == 'Ooops'
		}
		1 * l10n.addAll(_) >> { EnumMap map ->
			assert map.size() == 1
		}
	}
}
