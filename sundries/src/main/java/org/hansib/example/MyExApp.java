/**
 * MIT License
 *
 * for JavaSundries (https://github.com/hansi-b/JavaSundries)
 *
 * Copyright (c) 2022 Hans Bering
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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