package nl.tudelft.pds.granula;

import nl.tudelft.pds.granula.archiver.source.JobDirectorySource;
import nl.tudelft.pds.granula.archiver.source.JobSource;
import nl.tudelft.pds.granula.archiver.source.WorkloadFileSource;
import nl.tudelft.pds.granula.modeller.graphx.job.GraphX;
import nl.tudelft.pds.granula.util.JobListGenerator;
import nl.tudelft.pds.granula.util.UuidGenerator;

import java.io.File;

/**
 * Created by wing on 21-8-15.
 */
public class GraphXArchiver {
    public static void main(String[] args) {

        // output
        String outputPath = Configuration.repoPath + "/data/archive/"+ UuidGenerator.getRandomUUID() + ".xml";
//        String outputPath = String.format(\"/home/wing/Workstation/Dropbox/Repo/granula/data/output/graphx.xml\", workloadLog.getName());



        // workload
//        String workloadDirPath = Configuration.repoPath + "/data/input/";
//        File workloadFile = new File(workloadDirPath + "/graphx.tar.gz");
//        WorkloadFileSource workloadSource = new WorkloadFileSource(workloadFile.getAbsolutePath());
//        workloadSource.load();
//
//        for (JobSource jobSource : workloadSource.getEmbeddedJobSources()) {
//            GranulaArchiver granulaArchiver = new GranulaArchiver(jobSource, new GraphX(), outputPath);
//            granulaArchiver.archive();
//        }

        // job
        JobDirectorySource jobDirSource = new JobDirectorySource(Configuration.repoPath + "/data/log/graphx");
        jobDirSource.load();

        GranulaArchiver granulaArchiver = new GranulaArchiver(jobDirSource, new GraphX(), outputPath);
        granulaArchiver.archive();

        // generate list
        (new JobListGenerator()).generateRecentJobsList();

    }
}
