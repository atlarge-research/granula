/*
 * Copyright 2015 Delft University of Technology
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

package nl.tudelft.pds.granula.util;

import nl.tudelft.pds.granula.archiver.entity.info.TimeSeries;
import nl.tudelft.pds.granula.archiver.source.DataStream;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.*;

import java.io.IOException;

public class RrdManager {

    public static void main(String[] args) {
        RrdManager rrdManager = new RrdManager();

    }

    public RrdManager() {




    }

    public static TimeSeries extract(DataStream dataStream, long startTime, long endTime) {
        try {

            String rrdFilePath=dataStream.getPath();
            TimeSeries timeSeries = new TimeSeries();

            RrdDb rrdDb = new RrdDb("/tmp/tmp-" + UuidGenerator.getRandomUUID() + ".rrd",
                    String.format("rrdtool:/%s", rrdFilePath),
                    RrdBackendFactory.getFactory("MEMORY"));

            FetchRequest fetchRequest = rrdDb.createFetchRequest(ConsolFun.AVERAGE, startTime / 1000, endTime/ 1000);
            FetchData fetchData = fetchRequest.fetchData();
            rrdDb.close();

//            System.out.println(fetchData.dump());
//            System.out.println(fetchData.getArcEndTime());
//            System.out.println(fetchData.getArcStep());
//            System.out.println(fetchData.getColumnCount());
//            System.out.println(fetchData.getRowCount());

            for (String line : fetchData.dump().split("\n")) {
                String lineValues[] = line.split("\\s+");
                if(!(lineValues[1].equals("nan") || lineValues[1].equals("NaN"))) {
                    long timestamp = Long.parseLong(lineValues[0].replace(":", ""));
                    double value = quick_fix_mem(dataStream, Double.parseDouble(lineValues[1]));
                    timeSeries.addDatapoint(timestamp * 1000, value);
                }
            }
            return timeSeries;

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    public static double quick_fix_mem(DataStream dataStream, double value) {
        if(dataStream.getPath().contains("mem_")) {
            return value * 1000;
        } else
            return value;
    }
}
