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
package science.atlarge.granula.monitor.client;


import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigValueFactory;
import science.atlarge.granula.monitor.executor.MonitorService;
import science.atlarge.granula.monitor.type.EndpointType;
import science.atlarge.granula.monitor.info.ProcessInfo;
import science.atlarge.granula.monitor.info.MasterInfo;

import java.util.Random;
import java.util.Scanner;

public class MonitorClient extends MonitorService {

    EndpointType endpointType = EndpointType.Socket;

    MasterInfo masterInfo;
    ClientEndpoint cAssistant;

    public MonitorClient() {
        super();
        masterInfo = new MasterInfo();
    }

    public void init() {

        int port = config.getInt("akka.profiler.client.port");

        int masterPort = config.getInt("akka.profiler.master.port");
        String masterIp = config.getString("akka.profiler.master.ip");
        masterInfo.setPath(String.format("akka.tcp://profiler-master@%s:%s/user/profiler-master", masterIp, masterPort));
        masterInfo.setIp(ProcessInfo.Path2IpAddress(masterInfo.getPath()));

        port = config.getInt("akka.profiler.client.port");
        port = (new Random()).nextInt(4000)+4000;
        config = config.withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(port));
        final ActorSystem system = ActorSystem.create("profiler-client", config);
        system.actorOf(Props.create(ClientEndpoint.class, this), "profiler-client");
        getUserInput();
    }

    public void getUserInput() {

        //        system.scheduler().schedule(Duration.create(1, SECONDS),
//                Duration.create(10, SECONDS), new Runnable() {
//                    @Override
//                    public void run() {
//                        cAssistant.sendHealthRequest();
//                    }
//                }, system.dispatcher());

        boolean stop = false;
        System.out.println("Granula Profiler Commandline Client");
        System.out.println("Enter a command (health, xx)");
        while (!stop) {
            Scanner in = new Scanner(System.in);
            String[] inputCommands = in.nextLine().trim().split("\\s+");
            switch (inputCommands[0]) {
                case "health":
                    System.out.println("Monitoring Health");
                    cAssistant.sendHealthRequest();
                    break;
                case "xx":
                    System.out.println("XX is useless command");
                    break;
                case "exit":
                    System.out.println("Exiting");
                    System.exit(0);
                case "mp":
                    String processId = inputCommands[1];
                    monitorProcess(Integer.parseInt(processId));
                    break;
                case "rg":
                    String jobId = inputCommands[1];
                    registerJob(jobId);
                    break;
                case "lkp":
                    processId = inputCommands[1];
                    jobId = inputCommands[2];
                    linkProcessToJob(processId, jobId);
                    break;
                case "lkm":
                    String machineId = inputCommands[1];
                    jobId = inputCommands[2];
                    linkMachineToJob(machineId, jobId);
                    break;
                case "collect":
                    jobId = inputCommands[1];
                    String outPath = inputCommands[2];
                    collectJob(jobId, outPath);
                    break;
                case "":
                    break;
                default:
                    System.out.println("\"" + inputCommands + "\" is not a valid command");
                    break;
            }
        }
    }

    private void collectJob(String jobId, String outPath) {
        if(endpointType == EndpointType.REST) {
        } else  if(endpointType == EndpointType.Socket) {
            (new SocketClient()).collectJob(jobId, outPath);
        }
    }

    private void linkMachineToJob(String machineId, String jobId) {
        if(endpointType == EndpointType.REST) {
        } else  if(endpointType == EndpointType.Socket) {
            (new SocketClient()).linkMachineToJob(machineId, jobId);
        }
    }

    private void linkProcessToJob(String processId, String jobId) {
        if(endpointType == EndpointType.REST) {
        } else  if(endpointType == EndpointType.Socket) {
            (new SocketClient()).linkProcessToJob(processId, jobId);
        }
    }

    private void registerJob(String jobId) {
        if(endpointType == EndpointType.REST) {
        } else  if(endpointType == EndpointType.Socket) {
            (new SocketClient()).registerJob(jobId);
        }
    }

    public void setClientEndpoint(ClientEndpoint cAssistant) {
        this.cAssistant = cAssistant;
    }

    public MasterInfo getMasterInfo() {
        return masterInfo;
    }

    public void monitorProcess(int processId) {
        if(endpointType == EndpointType.REST) {
            (new RestfulClient()).monitorProcess(processId);
        } else  if(endpointType == EndpointType.Socket) {
            (new SocketClient()).monitorProcess(processId);
        }
    }
}
