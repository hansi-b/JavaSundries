package org.hansib.sundries;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * A thin wrapper around the class loader's resource streaming.
 */
public class ResourceLoader {

	public String getResourceAsString(final String resourceName) throws IOException {
		try (InputStream resStream = getResourceStream(resourceName)) {
			return new String(resStream.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	/**
	 * @param resourceName the name of the required resource
	 * @return a non-null InputStream of the resource
	 * @throws IllegalStateException if the classloader returns a null stream
	 */
	public InputStream getResourceStream(String resourceName) {
		InputStream resStream = getClass().getClassLoader().getResourceAsStream(resourceName);
		if (resStream == null)
			throw Errors.illegalState("Could not find resource stream '%s'", resourceName);
		return resStream;
	}

	public URL getResourceUrl(String resName) {
		return getClass().getClassLoader().getResource(resName);
	}
}