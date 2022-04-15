package org.hansib.sundries.typed_prefs;

import java.util.Optional;

/**
 * a preference tied to an enum with a typed value
 */
interface TypedPref<K extends Enum<K>, V> extends Converter<V> {

	K key();

	void set(V value);
}

interface RequiredPref<K extends Enum<K>, V> extends TypedPref<K, V> {
	V get();
}

interface OptionalPref<K extends Enum<K>, V> extends TypedPref<K, V> {

	Optional<V> get();

	void remove();
}

abstract class PrefClz<K extends Enum<K>, V> implements TypedPref<K, V> {

	private final K key;
	protected final TypedEnumPrefs<K> store;

	PrefClz(K key, TypedEnumPrefs<K> store) {
		this.key = key;
		this.store = store;
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

	ReqPrefClz(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}

	@Override
	public V get() {
		return store.get(this);
	}
}

abstract class OptPrefClz<K extends Enum<K>, V> extends PrefClz<K, V> implements OptionalPref<K, V> {

	OptPrefClz(K key, TypedEnumPrefs<K> store) {
		super(key, store);
	}

	@Override
	public Optional<V> get() {
		return Optional.ofNullable(store.get(this));
	}

	@Override
	public void remove() {
		store.remove(this);
	}
}