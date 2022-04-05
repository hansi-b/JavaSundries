package org.hansib.sundries;

/**
 * a thin wrapper around java Preferences with typed keys
 *
 * @param <K> the key enum
 */
public interface EnumPrefs<K extends Enum<K>> {

	static class PrefsException extends RuntimeException {

		private static final long serialVersionUID = -554364308103429180L;

		PrefsException(final Exception cause) {
			super(cause);
		}
	}

	public void put(final K key, final String value) throws PrefsException;

	public String get(final K key) throws PrefsException;

	public boolean contains(final K key) throws PrefsException;

	public void remove(final K key) throws PrefsException;
}
