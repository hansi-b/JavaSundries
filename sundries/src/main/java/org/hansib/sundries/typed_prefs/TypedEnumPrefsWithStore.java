package org.hansib.sundries.typed_prefs;

import org.hansib.sundries.store.PrefsStore;

public class TypedEnumPrefsWithStore<K extends Enum<K>> implements TypedEnumPrefs<K> {

	private final PrefsStore<K> store;

	public TypedEnumPrefsWithStore(PrefsStore<K> store) {
		this.store = store;
	}

	@Override
	public <V> V get(TypedPref<K, V> pref) {
		return pref.str2val(store.get(pref.key()));
	}

	@Override
	public <V> void set(TypedPref<K, V> pref, V value) {
		store.put(pref.key(), pref.val2str(value));
	}

	@Override
	public void remove(OptionalPref<K, ?> pref) {
		store.remove(pref.key());
	}
}