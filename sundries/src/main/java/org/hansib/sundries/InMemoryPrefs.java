package org.hansib.sundries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * keeps preferences in a simple map
 */
public class InMemoryPrefs<K extends Enum<K>> implements EnumPrefs<K> {

	private final Map<K, String> prefs;

	public InMemoryPrefs() {
		this.prefs = new ConcurrentHashMap<>();
	}

	public void put(final K key, final String value) {
		prefs.put(key, value);
	}

	public String get(final K key) {
		return prefs.getOrDefault(key, null);
	}

	public boolean contains(final K key) throws PrefsException {
		return get(key) != null;
	}

	public void remove(final K key) {
		prefs.remove(key);
	}
}
