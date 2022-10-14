package org.hansib.sundries;

import spock.lang.Specification

public class ResourceLoaderSpec extends Specification {

	def 'can load resource as String'() {
		expect:
		new ResourceLoader().getResourceAsString('resourceLoader_test.txt') == 'content checked in test\n'
	}

	def 'missing resource throws IllegalState'() {

		when:
		new ResourceLoader().getResourceStream('does_not_exist.txt')
		then:
		def ex = thrown(IllegalStateException)
		ex.message == "Could not find resource stream 'does_not_exist.txt'"
	}

	def 'can get resource url'() {
		expect:
		URL
		new ResourceLoader().getResourceUrl('resourceLoader_test.txt') == new URL('file:/home/cb/Projects/Java/Sundries/sundries/bin/test/resourceLoader_test.txt')
		new ResourceLoader().getResourceUrl('does_not_exist.txt') == null
	}
}
