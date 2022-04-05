package org.hansib.sundries;

import spock.lang.Specification

public class ErrorsSpec extends Specification {

	def "can get IllegalArg"() {

		when:
		def e = Errors.illegalArg("Hello %s", "world")

		then:
		e instanceof IllegalArgumentException
		e.message == "Hello world"
	}
}
