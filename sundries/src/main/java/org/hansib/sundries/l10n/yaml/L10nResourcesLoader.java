/**
 * MIT License
 *
 * for JavaSundries (https://github.com/hansi-b/JavaSundries)
 *
 * Copyright (c) 2022-2024 Hans Bering
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

import java.io.IOException;
import java.util.function.Consumer;

import org.hansib.sundries.ResourceLoader;
import org.hansib.sundries.l10n.yaml.errors.EnumYamlLoadError;
import org.hansib.sundries.l10n.yaml.errors.EnumYamlNotFound;
import org.hansib.sundries.l10n.yaml.errors.L10nFormatError;

class L10nResourcesLoader {

	private final String resourcesPath;
	private final Consumer<L10nFormatError> errorHandler;

	L10nResourcesLoader(String resourcesPath, Consumer<L10nFormatError> errorHandler) {
		this.resourcesPath = resourcesPath;
		this.errorHandler = errorHandler;
	}

	String load(String clzName) {
		String fullPath = "%s/%s.yaml".formatted(resourcesPath, clzName);
		try {
			return new ResourceLoader().getResourceAsString(fullPath);
		} catch (IllegalStateException ex) {
			errorHandler.accept(new EnumYamlNotFound(clzName, fullPath));
			return null;
		} catch (IOException ex) {
			errorHandler.accept(new EnumYamlLoadError(clzName, ex));
			return null;
		}
	}
}