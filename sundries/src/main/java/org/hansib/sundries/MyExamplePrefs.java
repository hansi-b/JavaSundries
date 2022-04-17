package org.hansib.sundries;

import org.hansib.sundries.prefs.OptFile;
import org.hansib.sundries.prefs.ReqBoolean;
import org.hansib.sundries.prefs.TypedEnumPrefs;
import org.hansib.sundries.prefs.TypedEnumPrefsWithStore;
import org.hansib.sundries.prefs.store.UserNodePrefsStore;

class MyApp {

}

class MyExamplePrefs {
	enum Keys {
		lastProject, wasAccepted
	}

	private OptFile<Keys> lastProject;
	private ReqBoolean<Keys> wasAccepted;

	MyExamplePrefs() {
		TypedEnumPrefs<Keys> prefs = new TypedEnumPrefsWithStore<>(UserNodePrefsStore.<Keys>forApp(MyApp.class));
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