package org.hansib.sundries.l10n.yaml;

import org.hansib.sundries.l10n.yaml.MappingsReader.Mapping
import org.hansib.sundries.l10n.yaml.MappingsReader.KeyValue
import org.hansib.sundries.l10n.yaml.MappingsReader.KeyValueList

import com.fasterxml.jackson.core.JsonProcessingException

import spock.lang.Specification

public class MappingsReaderSpec extends Specification {

	def reader = new MappingsReader()

	def 'can read maps'() {

		when:
		def res = reader.read('''
FirstMap:
  Key: Value
  More: other
2ndMap:
  2: 42
  Key: Value
''')
		then:
		res == [
			new Mapping('FirstMap', [
				new KeyValue('Key', 'Value'),
				new KeyValue('More', 'other')
			]),
			new Mapping('2ndMap', [
				new KeyValue('2', '42'),
				new KeyValue('Key', 'Value')
			])
		]
	}

	def 'can read empty map'() {

		when:
		def res = reader.read('''
NoItems:
Andnone:
''')
		then:
		res == [
			new Mapping('NoItems', []),
			new Mapping('Andnone', [])
		]
	}

	def 'can read duplicate map entries'() {

		when:
		def res = reader.read('''
SingleItem:
  Open: Ouvrir
SingleItem:
  Close: Fermer
''')
		then:
		res == [
			new Mapping('SingleItem', [
				new KeyValue('Open', 'Ouvrir'),
			]),
			new Mapping('SingleItem', [
				new KeyValue('Close', 'Fermer'),
			])
		]
	}

	def 'throws error'() {

		when:
		def res = reader.read('''
SingleItem:
  - xaz
''')
		then:
		thrown(JsonProcessingException)
	}

	def 'can read duplicate keys'() {

		when:
		def res = reader.read('''
SingleItem:
  Open: Ouvrir
  Open: Fermer
''')
		then:
		res == [
			new Mapping('SingleItem', [
				new KeyValue('Open', 'Ouvrir'),
				new KeyValue('Open', 'Fermer')
			])
		]
	}
}
