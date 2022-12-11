package org.hansib.sundries.l10n.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import org.hansib.sundries.l10n.yaml.L10nMapReader

import spock.lang.Specification

public class L10nMapReaderSpec extends Specification {

	enum MenuItems {
		Open, Exit, Missing
	}
	
	def om = new ObjectMapper(new YAMLFactory())

	def 'can read enum values'() {
		given:
		def r = new L10nMapReader(null)

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
}
