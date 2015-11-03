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

package nl.tudelft.pds.granula.modeller.giraph;

import nl.tudelft.pds.granula.archiver.entity.Identifier;

public class GiraphType extends Identifier {

    public static String MasterElected = "MasterElected";

    public static String TopActor = "TopActor";
    public static String AppMaster = "AppMaster";
    public static String BspMaster = "BspMaster";
    public static String BspWorker = "BspWorker";
    public static String UnknownActor = "ActorType.Unknown";
    public static String GlobalCoordinator = "GlobalCoordinator";

    public static String TopMission = "TopMission";
    public static String UnknownCleanup1 = "UnknownCleanup1";
    public static String Deployment = "Deployment";
    public static String ContainerLoad = "ContainerLoad";
    public static String ContainerOffload = "ContainerOffload";
    public static String Decommission = "Decommission";
    public static String ContainerAssignment = "ContainerAssignment";
    public static String AppStartup = "AppStartup";
    public static String AppTermination = "AppTermination";
    public static String BspExecution = "BspExecution";
    public static String PostApplication = "PostApplication";
    public static String BspSetup = "BspSetup";
    public static String GlobalStartup = "GlobalStartup";
    public static String Setup = "Setup";
    public static String PostSetup = "PostSetup";
    public static String ZookeeperSetup = "ZookeeperSetup";
    public static String GlobalDataload = "GlobalDataload";
    public static String GlobalSuperstep = "GlobalSuperstep";
    public static String BspCleanup = "BspCleanup";
    public static String FinalCleanup = "FinalCleanup";
    public static String ZookeeperCleanup = "ZookeeperCleanup";
    public static String ClientCleanup = "ClientCleanup";
    public static String ServerCleanup = "ServerCleanup";
    public static String OutputMerge = "OutputMerge";
    public static String ZookeeperOfflining = "ZookeeperOfflining";
    public static String GlobalDataLoad = "GlobalDataLoad";

    public static String DataLoad = "DataLoad";
    public static String PostDataLoad = "PostDataLoad";
    public static String Superstep = "Superstep";
    public static String DataOffload = "DataOffload";
    public static String Cleanup = "Cleanup";
    public static String WorkerTask = "WorkerTask";
    public static String MasterTask = "MasterTask";
    public static String BspIteration = "BspIteration";


    public static String PrepSuperstep = "PrepSuperstep";
    public static String Computation = "Computation";
    public static String MsgSend = "MsgSend";
    public static String PostSuperstep = "PostSuperstep";

    public static String UnknownMission = "MissionType.Unknown";


    public static String ComputeNode = "ComputeNode";

    public static String pkts_in = "pkts_in";
    public static String pkts_out = "pkts_out";
    public static String bytes_in = "bytes_in";
    public static String bytes_out = "bytes_out";


    public static String cpu_user = "cpu_user";
    public static String cpu_system = "cpu_system";
    public static String cpu_aidle = "cpu_aidle";
    public static String cpu_idle = "cpu_idle";
    public static String cpu_steal = "cpu_steal";
    public static String cpu_nice = "cpu_nice";
    public static String cpu_wio = "cpu_wio";

    public static String mem_free = "mem_free";
    public static String mem_shared = "mem_shared";
    public static String mem_total = "mem_total";
    public static String mem_buffers = "mem_buffers";
    public static String mem_cached = "mem_cached";

    public static String StartTime = "StartTime";
    public static String EndTime = "EndTime";




}
