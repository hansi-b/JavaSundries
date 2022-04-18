package org.hansib.sundries.prefs;

import java.util.Optional;

/**
 * a preference tied to an enum with a typed value
 */
interface Pref<K extends Enum<K>, V> extends Converter<V> {

	Prefs<K> prefs();

	K key();

	void set(V value);
}

interface RequiredPref<K extends Enum<K>, V> extends Pref<K, V> {
	V get();
}

interface OptionalPref<K extends Enum<K>, V> extends Pref<K, V> {

	Optional<V> get();

	void remove();
}

abstract class PrefClz<K extends Enum<K>, V> implements Pref<K, V> {

	private final K key;
	private final Prefs<K> prefs;

	PrefClz(K key, Prefs<K> prefs) {
		this.key = key;
		this.prefs = prefs;
	}

	@Override
	public Prefs<K> prefs() {
		return prefs;
	}

	public K key() {
		return key;
	}

	@Override
	public void set(V value) {
		prefs.set(this, value);
	}
}

abstract class ReqPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements RequiredPref<K, V> {

	ReqPrefClz(K key, Prefs<K> prefs) {
		super(key, prefs);
	}

	@Override
	public V get() {
		return prefs().get(this);
	}
}

abstract class OptPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements OptionalPref<K, V> {

	OptPrefClz(K key, Prefs<K> prefs) {
		super(key, prefs);
	}

	@Override
	public Optional<V> get() {
		return Optional.ofNullable(prefs().get(this));
	}

	@Override
	public void remove() {
		prefs().remove(this);
	}
}