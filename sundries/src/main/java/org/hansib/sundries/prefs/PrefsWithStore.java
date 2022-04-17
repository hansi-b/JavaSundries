package org.hansib.sundries.prefs;

import org.hansib.sundries.prefs.store.PrefsStore;

public class PrefsWithStore<K extends Enum<K>> implements Prefs<K> {

	private final PrefsStore<K> store;

	public PrefsWithStore(PrefsStore<K> store) {
		this.store = store;
	}

	@Override
	public <V> V get(Pref<K, V> pref) {
		return pref.str2val(store.get(pref.key()));
	}

	@Override
	public <V> void set(Pref<K, V> pref, V value) {
		store.put(pref.key(), pref.val2str(value));
	}

	@Override
	public void remove(OptionalPref<K, ?> pref) {
		store.remove(pref.key());
	}
}