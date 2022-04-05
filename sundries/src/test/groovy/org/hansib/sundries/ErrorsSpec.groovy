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

	def "can get IllegalState"() {

		when:
		def e = Errors.illegalState("Hello %s", "state")

		then:
		e instanceof IllegalStateException
		e.message == "Hello state"
	}

	def "can get IllegalState with cause"() {

		when:
		def cause = Errors.illegalArg("i am the cause")
		def e = Errors.illegalState(cause, "Hello %s", "with cause")

		then:
		e instanceof IllegalStateException
		e.message == "Hello with cause"
		e.cause == cause
	}
}
