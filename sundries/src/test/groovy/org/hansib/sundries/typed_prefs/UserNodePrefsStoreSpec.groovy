package org.hansib.sundries.typed_prefs;

import java.util.prefs.Preferences

import org.hansib.sundries.prefs.UserNodePrefsStore

import spock.lang.Specification

public class UserNodePrefsStoreSpec extends Specification {

	Preferences prefs = Mock()

	enum Props {
		aProp, bProp
	}

	def "new prefs are empty"() {
		when:
		new UserNodePrefsStore<Props>(prefs).contains(Props.bProp)

		then:
		1 * prefs.get("bProp", null)
	}

	def "can put value"() {
		when:
		new UserNodePrefsStore<Props>(prefs).put(Props.bProp, "hello world")

		then:
		1 * prefs.put("bProp", "hello world")
	}

	def "can get value"() {
		when:
		prefs.get("bProp", null) >> "a value"
		def p = new UserNodePrefsStore<Props>(prefs)

		then:
		p.get(Props.bProp) == "a value"
	}

	def "can remove value"() {
		when:
		new UserNodePrefsStore<Props>(prefs).remove(Props.aProp)

		then:
		1 * prefs.remove("aProp")
	}
}
