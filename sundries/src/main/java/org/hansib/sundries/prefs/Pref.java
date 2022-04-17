package org.hansib.sundries.prefs;

import java.util.Optional;

/**
 * a preference tied to an enum with a typed value
 */
interface Pref<K extends Enum<K>, V> extends Converter<V> {

	Prefs<K> store();

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
	private final Prefs<K> store;

	PrefClz(K key, Prefs<K> store) {
		this.key = key;
		this.store = store;
	}

	@Override
	public Prefs<K> store() {
		return store;
	}

	public K key() {
		return key;
	}

	@Override
	public void set(V value) {
		store.set(this, value);
	}
}

abstract class ReqPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements RequiredPref<K, V> {

	ReqPrefClz(K key, Prefs<K> store) {
		super(key, store);
	}

	@Override
	public V get() {
		return store().get(this);
	}
}

abstract class OptPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements OptionalPref<K, V> {

	OptPrefClz(K key, Prefs<K> store) {
		super(key, store);
	}

	@Override
	public Optional<V> get() {
		return Optional.ofNullable(store().get(this));
	}

	@Override
	public void remove() {
		store().remove(this);
	}
}