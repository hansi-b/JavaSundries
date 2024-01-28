package org.hansib.sundries.l10n.yaml;

import spock.lang.Specification

public class MappingReaderSpec extends Specification {

	def reader = new MappingReader()

	def 'can read simple maps'() {

		when:
		def res = reader.read('''
FirstMap:
  bca: other
  abc: some
2ndMap:
  1: 42
  2: Value with spaces
''')
		then:
		res == [
			new Entry('FirstMap', [
				new Entry('bca', 'other'),
				new Entry('abc', 'some')
			]),
			new Entry('2ndMap', [
				new Entry('1', '42'),
				new Entry('2', 'Value with spaces')
			])
		]
	}

	def 'can read duplicate values'() {

		when:
		def res = reader.read('''
a Map:
  a: b
  a: c
  a: b
  a: c
''')
		then:
		res == [
			new Entry('a Map', [
				new Entry('a', 'b'),
				new Entry('a', 'c'),
				new Entry('a', 'b'),
				new Entry('a', 'c')
			])
		]
	}
}
