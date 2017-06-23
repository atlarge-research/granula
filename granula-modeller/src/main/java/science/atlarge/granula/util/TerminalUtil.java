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
package science.atlarge.granula.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This is a shortcut, when trying to execute commands on das4tud on my desktop,
 * assuming there is a private key configuration to the das4 headnodeAlias, and ssh configuration as das4tud
 */
public class TerminalUtil {

    public static boolean isVerbose = false;

    String username;
    String headnodeUrl;

    public TerminalUtil(String username, String headnodeUrl) {
        this.username = username;
        this.headnodeUrl = headnodeUrl;
    }


    public void checkRemovalSafety(String nodeName, String command) {
        if(nodeName.contains("localhost")) {
            if(command.contains("rm ")) {
                throw new IllegalStateException("Command is dangerous " + command);
            }
        }
    }

    public String runCommand(String nodeName, String command) {
        String scriptOutput = "";

        checkRemovalSafety(nodeName, command);

        try {
            String sshTarget = String.format("%s@%s", username, headnodeUrl);
            String headnodeCommand = String.format("ssh %s \"%s\"", nodeName, command);
            ProcessBuilder pb = new ProcessBuilder("ssh", sshTarget, headnodeCommand);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                scriptOutput += line + "\n";
            }
            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return scriptOutput;
    }



    public String runCommand(String command) {
        return runCommand("localhost", command);
    }

    public String runPrintCommand(String command) {
        return runPrintCommand("localhost", command);
    }

    public String runPrintCommand(String nodeName, String command) {
        String scriptOutput = "";

        checkRemovalSafety(nodeName, command);

        try {
            String sshTarget = String.format("%s@%s", username, headnodeUrl);
            String headnodeCommand = String.format("ssh %s \"%s\"", nodeName, command);
            ProcessBuilder pb = new ProcessBuilder("ssh", sshTarget, headnodeCommand);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                scriptOutput += line + "\n";
            }
            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return scriptOutput;
    }


}
