package org.hansib.sundries.prefs;

import org.hansib.sundries.Errors;
import org.hansib.sundries.prefs.store.PrefsStore;

public class PrefsWithStore<K extends Enum<K>> implements Prefs<K> {

	private final PrefsStore<K> store;

	public PrefsWithStore(PrefsStore<K> store) {
		this.store = store;
	}

	@Override
	public <V> V get(Pref<K, V> pref) {
		String val = store.get(pref.key());
		return val == null ? null : pref.str2val(val);
	}

	@Override
	public <V> void set(Pref<K, V> pref, V val) {
		if (val == null)
			throw Errors.illegalArg("Cannot set null value (%s)", pref);
		store.put(pref.key(), pref.val2str(val));
	}

	@Override
	public void remove(OptionalPref<K, ?> pref) {
		store.remove(pref.key());
	}
}