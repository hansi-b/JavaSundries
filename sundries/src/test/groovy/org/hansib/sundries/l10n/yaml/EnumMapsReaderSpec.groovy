package org.hansib.sundries.l10n.yaml;

import org.hansib.sundries.l10n.yaml.EnumMapsReader.EnumMapRecord
import org.hansib.sundries.l10n.yaml.EnumMapsReader.Pair
import org.hansib.sundries.l10n.yaml.EnumMapsReader.PairList

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

	def 'can read empty map'() {

		given:
		def reader = new EnumMapsReader()

		when:
		def res = reader.read('''
SingleItem:
''')
		then:
		res == [
			new EnumMapRecord('SingleItem', (PairList)null)
		]
	}

	def 'can read duplicate map entries'() {

		given:
		def reader = new EnumMapsReader()

		when:
		def res = reader.read('''
SingleItem:
  Open: Ouvrir
SingleItem:
  Close: Fermer
''')
		then:
		res == [
			new EnumMapRecord('SingleItem', [
				new Pair('Open', 'Ouvrir'),
			]),
			new EnumMapRecord('SingleItem', [
				new Pair('Close', 'Fermer'),
			])
		]
	}
	def 'can read duplicate keys'() {

		given:
		def reader = new EnumMapsReader()

		when:
		def res = reader.read('''
SingleItem:
  Open: Ouvrir
  Open: Fermer
''')
		then:
		res == [
			new EnumMapRecord('SingleItem', [
				new Pair('Open', 'Ouvrir'),
				new Pair('Open', 'Fermer')
			])
		]
	}
}
