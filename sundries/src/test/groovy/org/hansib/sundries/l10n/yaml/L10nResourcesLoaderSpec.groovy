package org.hansib.sundries.l10n.yaml;

import java.util.function.Consumer

import org.hansib.sundries.l10n.yaml.errors.EnumYamlNotFound
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError

import spock.lang.Specification

public class L10nResourcesLoaderSpec extends Specification {

	def 'can load yaml'() {
		given:
		Consumer<L10nFormatError> errHandler = Mock()
		L10nResourcesLoader loader = new L10nResourcesLoader('l10n', errHandler)

		when:
		def yaml = loader.load('YamlIsFound')

		then:
		yaml != null
		0 * errHandler.accept(_)
	}

	def 'does not load yml'() {
		given:
		Consumer<L10nFormatError> errHandler = Mock()
		L10nResourcesLoader loader = new L10nResourcesLoader('l10n', errHandler)

		when:
		def yaml = loader.load('YmlNotFound')

		then:
		yaml == null
		1 * errHandler.accept(_) >> { L10nFormatError err ->
			assert err instanceof EnumYamlNotFound
		}
	}
}
