/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
      throw new IOException(String.format("(%s) File not found in path (%s)", filePath, Thread
          .currentThread().getContextClassLoader().getResource("./")));
    }

    final byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));

    return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
  }
}
