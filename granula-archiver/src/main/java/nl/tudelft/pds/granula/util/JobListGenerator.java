package nl.tudelft.pds.granula.util;

import nl.tudelft.pds.granula.Configuration;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by wing on 26-8-15.
 */
public class JobListGenerator {

    public static void main(String[] args) {
        JobListGenerator jobListGenerator  = new JobListGenerator();
        jobListGenerator.generateRecentJobsList();
    }

    public void generateRecentJobsList() {
        generateForEntireDirectory(
                Configuration.repoPath + "/data/archive/",
                Configuration.repoPath + "/data/list" + "/recent-jobs.xml");
    }

    public void generateForEntireDirectory(String arcFileDir, String outputPath) {

        StringBuilder xmlNodeBuilder = new StringBuilder();

        xmlNodeBuilder.append("<Jobs>");

        File[] jobArcFiles = new File(arcFileDir).listFiles();
        Arrays.sort(jobArcFiles, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        });

        for (File arcFile : jobArcFiles) {
            if(arcFile.isFile()) {
                System.out.println(arcFile.getAbsolutePath());
                String arcFileUrl = "../data/archive/" + new File(arcFile.getAbsolutePath()).getName();
                xmlNodeBuilder.append(generateJobXmlNode(arcFile.getAbsolutePath(), arcFileUrl));
            }
        }

        xmlNodeBuilder.append("</Jobs>");

        try {
            PrintWriter writer;
            writer = new PrintWriter(outputPath, "UTF-8");
            writer.print(XMLFormatter.format(xmlNodeBuilder.toString()));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateJobXmlNode(String jobArcFilePath) {
        return generateJobXmlNode(jobArcFilePath, new File(jobArcFilePath).getName());
    }

    public String generateJobXmlNode(String jobArcFilePath, String renamedfilePath) {

        String jobXmlNode = null;

        SAXBuilder builder = new SAXBuilder();
        File jobArcFile = new File(jobArcFilePath);

        try {

            Document document = (Document) builder.build(jobArcFile);
            Element rootNode = document.getRootElement();

            String jobName = rootNode.getAttribute("name").getValue();
            String jobType = rootNode.getAttribute("type").getValue();
            String jobUuid = rootNode.getAttribute("uuid").getValue();

            String jobDescription = jobName;

            StringBuilder xmlNodeBuilder = new StringBuilder();
            xmlNodeBuilder.append(String.format("<Job uuid=\"%s\" name=\"%s\" type=\"%s\">", jobUuid, jobName, jobType));
            xmlNodeBuilder.append(String.format("<Url>%s</Url>", renamedfilePath));
            xmlNodeBuilder.append(String.format("<Description>%s</Description>", jobDescription));
            xmlNodeBuilder.append(String.format("</Job>"));
            jobXmlNode = xmlNodeBuilder.toString();


        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }

        if(jobXmlNode == null) {
            System.out.println(String.format("Warning: failed to read job archive at %s.", jobArcFilePath));
        }

        return jobXmlNode;
    }


}
