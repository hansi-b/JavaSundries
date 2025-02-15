package org.hansib.sundries.preferences;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.AbstractPreferences;

/**
 * A simple in-memory implementation of the {@link java.util.prefs.Preferences} API. Mostly useful for testing.
 */
public class InMemoryPreferences extends AbstractPreferences {

	private final Map<String, String> prefs = new HashMap<>();
	private final Map<String, AbstractPreferences> children = new HashMap<>();

	protected InMemoryPreferences(AbstractPreferences parent, String name) {
		super(parent, name);
	}

	/**
	 * @return a root node
	 */
	public static InMemoryPreferences createRoot() {
		return new InMemoryPreferences(null, "");
	}

	@Override
	protected void putSpi(String key, String value) {
		prefs.put(key, value);
	}

	@Override
	protected String getSpi(String key) {
		return prefs.get(key);
	}

	@Override
	protected void removeSpi(String key) {
		prefs.remove(key);
	}

	@Override
	protected void removeNodeSpi() {
		prefs.clear();
		children.clear();
	}

	@Override
	protected String[] keysSpi() {
		return prefs.keySet().toArray(new String[0]);
	}

	@Override
	protected String[] childrenNamesSpi() {
		return children.keySet().toArray(new String[0]);
	}

	@Override
	protected AbstractPreferences childSpi(String name) {
		return children.computeIfAbsent(name, k -> new InMemoryPreferences(this, k));
	}

	@Override
	protected void syncSpi() {
		// No-op for in-memory implementation
	}

	@Override
	protected void flushSpi() {
		// No-op for in-memory implementation
	}
}