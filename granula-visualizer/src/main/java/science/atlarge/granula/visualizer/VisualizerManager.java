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
package science.atlarge.granula.visualizer;

import science.atlarge.granula.modeller.entity.Execution;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by wlngai on 6/30/16.
 */
public class VisualizerManager {

    public static final String STATIC_RESOURCES[] = new String[]{
            "css/granula.css",
            "js/chart.js",
            "js/data.js",
            "js/environmentview.js",
            "js/job.js",
            "js/operation-chart.js",
            "js/operationview.js",
            "js/view.js",
            "js/util.js",
            "js/overview.js",
            "lib/bootstrap.css",
            "lib/bootstrap.js",
            "lib/jquery.js",
            "lib/d3.min.js",
            "lib/nv.d3.css",
            "lib/nv.d3.js",
            "lib/snap.svg-min.js",
            "lib/underscore-min.js",
            "visualizer.htm"
    };


    public static void addVisualizerResource(Execution execution) {
        for (String resource : VisualizerManager.STATIC_RESOURCES) {
            URL resourceUrl = VisualizerManager.class.getResource("/granula/" + resource);

            Path resArcPath = Paths.get(execution.getArcPath()).resolve(resource);
            try {
                if (!resArcPath.getParent().toFile().exists()) {
                    Files.createDirectories(resArcPath.getParent());
                } else if (!resArcPath.getParent().toFile().isDirectory()) {
                    throw new IOException("Could not write static resource to \"" + resArcPath + "\": parent is not a directory.");
                }
                FileUtils.copyInputStreamToFile(resourceUrl.openStream(), resArcPath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
