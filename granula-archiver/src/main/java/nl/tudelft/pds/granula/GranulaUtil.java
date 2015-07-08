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

package nl.tudelft.pds.granula;

import java.util.ArrayList;
import java.util.List;

public class GranulaUtil {

    public static void main(String[] args) {
        GranulaUtil granulaUtil = new GranulaUtil();
        List<String> inputFileNames = new ArrayList<>();
        inputFileNames.add("/home/wing/Workstation/Dropbox/Projects/Result/archive/1.xml");
        inputFileNames.add("/home/wing/Workstation/Dropbox/Projects/Result/archive/2.xml");
        inputFileNames.add("/home/wing/Workstation/Dropbox/Projects/Result/archive/3.xml");
        String outputFileName = "/home/wing/Workstation/Dropbox/Projects/Result/archive/all.xml";

        //doesn''t work
//        granulaUtil.mergeFile(inputFileNames, outputFileName);

    }

    public GranulaUtil() {

    }

//    public void mergeFile(List<String> inputFileNames, String outputFileName) {
//        File outputFile = new File(outputFileName);
//
//        try {
//
//            String charset = "UTF-8";
//            String delete = "foo";
//
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), charset));
//
//            writer.print(String.format("<Workload uuid=\"%s\">", UuidGenerator.getRandomUUID()));
//            for (String inputFileName : inputFileNames) {
//                FileReader fin = new FileReader(inputFileName);
//                Scanner src = new Scanner(fin);
//
//                src.useDelimiter("(?<=(>))|(?<=(<))");
//                String line = null;
//                while (src.hasNext()) {
//                    line = src.next();
//                    if(!src.next().contains("<Workload") || !src.next().contains("</Workload>")) {
//                        writer.print(line);
//                    }
//                }
//                fin.close();
//            }
//            writer.print("</Workload>");
//            writer.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
