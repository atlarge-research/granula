/*
 * Copyright 2015 - 2017 Atlarge Research Team,
 * operating at Technische Universiteit Delft
 * and Vrije Universiteit Amsterdam, the Netherlands.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package science.atlarge.granula.monitor.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.readAllBytes;

/**
 * Created by wlngai on 14-1-16.
 */
public class FileUtil {

    public static boolean fileExists(Path path) {
        return path.toFile().exists();
    }

    public static void writeFile(String content, Path path) {
        try {
            path.getParent().toFile().mkdirs();
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Path path) {
        try {
            return new String(readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't read file at " + path);
        }
    }
}
