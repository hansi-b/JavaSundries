/**
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
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

class EnumMapsReader {

	record Pair(String key, String value) {
	}

	static class PairList {
		final List<Pair> pairs;

		PairList() {
			this(new ArrayList<>());
		}

		PairList(List<Pair> pairs) {
			this.pairs = pairs;
		}

		@JsonAnySetter
		public void pair(String key, String value) {
			pairs.add(new Pair(key, value));
		}

		@Override
		public int hashCode() {
			return Objects.hash(pairs);
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof PairList pl) && pairs.equals(pl.pairs);
		}

		@Override
		public String toString() {
			return String.format("PairList %s", pairs);
		}
	}

	record EnumMapRecord(String name, PairList values) {
		EnumMapRecord(String name, List<Pair> values) {
			this(name, new PairList(values));
		}
	}

	static class EnumMaps {
		final List<EnumMapRecord> maps;

		EnumMaps() {
			this(new ArrayList<>());
		}

		EnumMaps(List<EnumMapRecord> maps) {
			this.maps = maps;
		}

		@JsonAnySetter
		public void enumMap(String key, PairList values) {
			maps.add(new EnumMapRecord(key, values));
		}
	}

	private final ObjectMapper om;

	EnumMapsReader() {
		this.om = new ObjectMapper(new YAMLFactory());
	}

	List<EnumMapRecord> read(String yaml) throws JsonProcessingException {
		return om.readValue(yaml, EnumMaps.class).maps;
	}
}
