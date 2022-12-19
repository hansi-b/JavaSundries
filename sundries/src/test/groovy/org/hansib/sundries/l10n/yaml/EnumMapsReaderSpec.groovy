package org.hansib.sundries.l10n.yaml;

import org.hansib.sundries.l10n.yaml.EnumMapsReader.EnumMapRecord
import org.hansib.sundries.l10n.yaml.EnumMapsReader.Pair

import spock.lang.Specification

public class EnumMapsReaderSpec extends Specification {

	def 'can read map'() {

		given:
		def reader = new EnumMapsReader()

		when:
		def res = reader.read('''
SingleItem:
  Open: Ouvrir
''')
		then:
		res == [
			new EnumMapRecord('SingleItem', [
				new Pair('Open', 'Ouvrir')
			])
		]
	}
}
