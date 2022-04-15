package org.hansib.sundries.typed_prefs;

class Initializer {

	<V, K extends Enum<K>, P extends PrefClz<K, V>> P withInitial(P pref, V initialValue) {
		if (pref.store.get(pref) == null)
			pref.store.set(pref, initialValue);
		return pref;
	}
}

public interface TypedEnumPrefs<K extends Enum<K>> {

	<V> V get(TypedPref<K, V> pref);

	<V> void set(TypedPref<K, V> pref, V value);

	void remove(OptionalPref<K, ?> pref);

	/*
	 * factory methods
	 */

	default OptFile<K> optionalFile(K key) {
		return new OptFile<>(key, this);
	}

	default ReqBoolean<K> optionalBoolean(K key) {
		return new ReqBoolean<>(key, this);
	}

	default ReqBoolean<K> requiredBoolean(K key, boolean initialValue) {
		return new Initializer().withInitial(new ReqBoolean<>(key, this), initialValue);
	}
}
