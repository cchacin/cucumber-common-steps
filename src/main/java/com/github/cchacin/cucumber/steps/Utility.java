package com.github.cchacin.cucumber.steps;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class Utility {

    static String fileContent(final String filePath) throws URISyntaxException, IOException {

        final URL url = Thread.currentThread().getContextClassLoader().getResource("./" + filePath);
        if (null == url) {
            throw new IOException(String.format("(%s) File not found in path (%s)",
                    filePath,
                    Thread.currentThread().getContextClassLoader().getResource("./")));
        }

        final byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));

        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded))
                .toString();
    }
}
