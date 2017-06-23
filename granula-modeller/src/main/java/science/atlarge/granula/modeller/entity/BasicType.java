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

package science.atlarge.granula.modeller.entity;

/**
 * Created by wing on 12-3-15.
 */
public class BasicType {

    public enum ArchiveFormat {JSON, JS};

    public static String Unique = "Id.Unique";
    public static String LocalUnique = "Id.LocalUnique";
    public static String Unknown = "Id.Unknown";
    public static String Equal = "Equal";
    public static String Any = "Any";

    public static String BasicInfo = "BasicInfo";
    public static String ActorInfo = "ActorInfo";
    public static String MissionInfo = "MissionInfo";
    public static String TimeSeriesInfo = "TimeSeriesInfo";
    public static String SummaryInfo = "SummaryInfo";

    public static String ByUuid = "ByUuid";
    public static String ByLoggingCloseness = "ByLoggingCloseness";
    public static String ByGlobalUniqueness = "ByGlobalUniqueness";

    public static String SummaryVisual = "SummaryVisual";
    public static String TableVisual = "TableVisual";
    public static String TimeSeriesVisual = "TimeSeriesVisual";

    public static String TopActor = "TopActor";
    public static String TopMission = "TopMission";

    public static String ColorBlue = "#3399FF";

    public static String ColorGrey = "#999999";

    public static String ComputeNode = "ComputeNode";
    public static String StartTime = "StartTime";
    public static String EndTime = "EndTime";


}
