package org.hansib.sundries.prefs.store;

import java.util.prefs.Preferences

import spock.lang.Specification

public class UserNodePrefsStoreSpec extends Specification {

	Preferences node = Mock()

	def "new prefs are empty"() {
		when:
		new UserNodePrefsStore(node).contains("bProp")

		then:
		1 * node.get("bProp", null)
	}

	def "can put value"() {
		when:
		new UserNodePrefsStore(node).put("bProp", "hello world")

		then:
		1 * node.put("bProp", "hello world")
	}

	def "can get value"() {
		when:
		node.get("bProp", null) >> "a value"
		def p = new UserNodePrefsStore(node)

		then:
		p.get("bProp") == "a value"
	}

	def "can remove value"() {
		when:
		new UserNodePrefsStore(node).remove("aProp")

		then:
		1 * node.remove("aProp")
	}
}
