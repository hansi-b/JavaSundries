package org.hansib.sundries;

import org.hansib.sundries.prefs.UserNodePrefsStore;
import org.hansib.sundries.typed_prefs.ReqBoolean;
import org.hansib.sundries.typed_prefs.OptFile;
import org.hansib.sundries.typed_prefs.TypedEnumPrefs;
import org.hansib.sundries.typed_prefs.TypedUserNodePrefs;

class MyApp {

}

class MyExamplePrefs {
	enum Keys {
		lastProject, wasAccepted
	}

	private OptFile<Keys> lastProject;
	private ReqBoolean<Keys> wasAccepted;

	MyExamplePrefs() {
		TypedEnumPrefs<Keys> prefs = new TypedUserNodePrefs<>(UserNodePrefsStore.<Keys>forApp(MyApp.class));
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