package org.hansib.sundries;

import java.util.prefs.Preferences;

/**
 * a thin wrapper around java Preferences with typed keys
 *
 * @param <K> the key enum
 */
public class UserNodePrefs<K extends Enum<K>> implements EnumPrefs<K> {

	private final Preferences node;

	UserNodePrefs(Preferences node) {
		this.node = node;
	}

	public static <L extends Enum<L>> UserNodePrefs<L> forApp(final Class<?> clazz) {
		return new UserNodePrefs<>(Preferences.userNodeForPackage(clazz).node(clazz.getSimpleName()));
	}

	public void put(final K key, final String value) {
		node.put(key.name(), value);
	}

	public String get(final K key) {
		return node.get(key.name(), null);
	}

	public boolean contains(final K key) throws PrefsException {
		return get(key) != null;
	}

	public void remove(final K key) {
		node.remove(key.name());
	}
}
