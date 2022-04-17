package org.hansib.sundries;

import org.hansib.sundries.prefs.OptFile;
import org.hansib.sundries.prefs.ReqBoolean;
import org.hansib.sundries.prefs.Prefs;
import org.hansib.sundries.prefs.PrefsWithStore;
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
		Prefs<Keys> prefs = new PrefsWithStore<>(UserNodePrefsStore.<Keys>forApp(MyApp.class));
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