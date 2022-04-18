package org.hansib.example;

import org.hansib.sundries.prefs.OptEnum;
import org.hansib.sundries.prefs.OptFile;
import org.hansib.sundries.prefs.Prefs;
import org.hansib.sundries.prefs.PrefsWithStore;
import org.hansib.sundries.prefs.ReqBoolean;
import org.hansib.sundries.prefs.store.InMemoryPrefsStore;

class PrefsVersioning<K extends Enum<K>, V extends Enum<V>> {

	PrefsVersioning(Prefs<K> prefs, K version, Class<V> versionEnum) {
		OptEnum<K, V> versionPref = prefs.optionalEnum(version, versionEnum);
		
		versionPref.get();
	}
}

class MyExamplePrefs {

	enum Versions {
		v1, v2
	}

	enum Keys {
		_version, lastProject, wasAccepted;
	}

	private OptFile<Keys> lastProject;
	private ReqBoolean<Keys> wasAccepted;

	MyExamplePrefs() {
		Prefs<Keys> prefs = new PrefsWithStore<>(new InMemoryPrefsStore<Keys>());
		new PrefsVersioning<>(prefs, Keys._version, Versions.class);

		lastProject = prefs.optionalFile(Keys.lastProject);
		wasAccepted = prefs.requiredBoolean(Keys.wasAccepted, false);
	}

	OptFile<Keys> lastProject() {
		return lastProject;
	}

	ReqBoolean<Keys> wasAccepted() {
		return wasAccepted;
	}
}

public class MyExApp {

	static final MyExamplePrefs prefs = new MyExamplePrefs();

	public static void main(String[] args) {

	}
}