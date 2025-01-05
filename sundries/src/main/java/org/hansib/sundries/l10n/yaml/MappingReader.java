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
package org.hansib.sundries.l10n.yaml;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Reads YAML into a nested structure of two mappings.
 */
class MappingReader {
	private static class EntryList<V> {

		private final List<Entry<V>> entries;

		@SuppressWarnings("unused")
		EntryList() {
			entries = new ArrayList<>();
		}

		@JsonAnySetter
		public void entry(String key, V value) {
			entries.add(new Entry<>(key, value));
		}
	}

	private static class MappingList {
		private final List<Entry<EntryList<String>>> mappings;

		@SuppressWarnings("unused")
		MappingList() {
			mappings = new ArrayList<>();
		}

		@JsonAnySetter
		public void mapping(String key, EntryList<String> entries) {
			mappings.add(new Entry<>(key, entries));
		}

		private List<Entry<List<Entry<String>>>> toListOfEntries() {
			return mappings.stream().map(e -> new Entry<>(e.key(), e.value().entries)).toList();
		}
	}

	private final ObjectMapper om;

	MappingReader() {
		om = new ObjectMapper(new YAMLFactory());
	}

	List<Entry<List<Entry<String>>>> read(String yaml) throws JsonProcessingException {
		return om.readValue(yaml, MappingList.class).toListOfEntries();
	}
}
