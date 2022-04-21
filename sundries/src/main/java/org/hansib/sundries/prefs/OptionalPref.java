package org.hansib.sundries.prefs;

import java.util.Optional;

public interface OptionalPref<K extends Enum<K>, V> extends Pref<K, V> {

	default Optional<V> get() {
		return Optional.ofNullable(prefs().get(this));
	}

	/**
	 * @return whether this preference is currently set (to a non-null value); same
	 *         as {@code get().isPresent()}
	 */
	default boolean isSet() {
		return prefs().get(this) != null;
	}

	/**
	 * Unsets this preference
	 */
	default void unset() {
		prefs().remove(this);
	}
}