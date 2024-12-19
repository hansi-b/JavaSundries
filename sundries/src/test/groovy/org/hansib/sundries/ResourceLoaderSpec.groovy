package org.hansib.sundries

import spock.lang.Specification

import java.util.function.Function

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
		def resUrl = new ResourceLoader().getResourceUrl('resourceLoader_test.txt').toString()
		resUrl.contains('file') && resUrl.contains('resourceLoader_test.txt')
		new ResourceLoader().getResourceUrl('does_not_exist.txt') == null
	}

	def 'can transform resource'() {
		given:
		Function<InputStream, Integer> firstChar = s -> s.read();

		expect:
		new ResourceLoader().loadResourceStream('resourceLoader_test.txt', firstChar) == (int) 'c'
	}
}
