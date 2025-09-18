package org.hansib.sundries.typedprefs;

import java.util.Objects;

/**
 * @param <K> the enum class of the value preferences
 */
public final class PrefsPref<K extends Enum<K>> extends TypedPref<TypedPrefs<K>> {

	PrefsPref(TypedPrefs<K> prefs) {
		set(prefs);
	}

	/**
	 * The value of a PrefsPref must never be unset (i.e., must always be non-null)
	 * 
	 * @throws NullPointerException if the argument value is null
	 */
	@Override
	public void set(TypedPrefs<K> newValue) {
		super.set(Objects.requireNonNull(newValue));
	}

	public Class<K> prefsEnum() {
		return get().keysClass();
	}
}