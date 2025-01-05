/*-
 * MIT License
 *
 * for JavaSundries (https://github.com/hansi-b/JavaSundries)
 *
 * Copyright (c) 2022-2023 Hans Bering
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.hansib.sundries.typedprefs.store;

import java.util.EnumSet;
import java.util.prefs.Preferences;

import org.hansib.sundries.typedprefs.BooleanPref;
import org.hansib.sundries.typedprefs.EnumPref;
import org.hansib.sundries.typedprefs.PrefsPref;
import org.hansib.sundries.typedprefs.StringPref;
import org.hansib.sundries.typedprefs.TypedPref;
import org.hansib.sundries.typedprefs.TypedPrefs;

/**
 * A thin wrapper around java Preferences for storing {@link TypedPrefs}s
 *
 * @param <K> the key enum
 */
public class UserNodePrefsStore<K extends Enum<K>> implements PrefsStore<K> {

	private final Preferences node;

	UserNodePrefsStore(Preferences node) {
		this.node = node;
	}

	public static <L extends Enum<L>> UserNodePrefsStore<L> forApp(final Class<?> clazz) {
		return new UserNodePrefsStore<>(Preferences.userNodeForPackage(clazz).node(clazz.getSimpleName()));
	}

	@Override
	public void save(TypedPrefs<K> prefs) {
		EnumSet.allOf(prefs.keysClass()).forEach(k -> save(k, prefs));
	}

	private <V, S extends Enum<S>> void save(K k, TypedPrefs<K> prefs) {

		TypedPref<V> pref = prefs.pref(k);
		V val = pref.get();

		if (pref instanceof PrefsPref<?> prefsPref) {
			savePrefsPref(k, prefsPref);
		} else if (val == null)
			node.remove(k.name());
		else
			node.put(k.name(), val2str(val));
	}

	private <S extends Enum<S>> void savePrefsPref(K k, PrefsPref<S> prefsPref) {
		Preferences subNode = node.node(k.name());
		new UserNodePrefsStore<S>(subNode).save(prefsPref.get());
	}

	private static String val2str(Object val) {
		if (val.getClass().isEnum())
			return ((Enum<?>) val).name();

		return switch (val) {
		case String s -> s;
		case Boolean b -> b.toString();
		default -> throw new IllegalStateException("Unexpected value: " + val);
		};
	}

	@Override
	public void load(TypedPrefs<K> prefs) {
		EnumSet.allOf(prefs.keysClass()).forEach(k -> load(k, prefs.pref(k)));
	}

	private <V> void load(K key, TypedPref<V> pref) {
		if (pref instanceof PrefsPref<?> prefsPref) {
			loadPrefsPref(key, prefsPref);
		} else {
			loadBasicPref(key, pref);
		}
	}

	@SuppressWarnings("unchecked")
	private <V> void loadBasicPref(K key, TypedPref<V> pref) {
		String strVal = node.get(key.name(), null);
		if (strVal == null)
			pref.set(null);
		else {
			switch (pref) {
			case StringPref s -> s.set(strVal);
			case BooleanPref b -> b.set(Boolean.valueOf(strVal));
			case EnumPref<?> enumPref -> pref.set((V) Enum.valueOf(enumPref.enumClazz(), strVal));
			default -> throw new IllegalArgumentException("Unexpected preference type: " + pref.getClass());
			}
		}
	}

	private <S extends Enum<S>> void loadPrefsPref(K key, PrefsPref<S> prefsPref) {
		TypedPrefs<S> subPrefs = prefsPref.get();
		new UserNodePrefsStore<S>(node.node(key.name())).load(subPrefs);
		prefsPref.set(subPrefs);
	}
}
