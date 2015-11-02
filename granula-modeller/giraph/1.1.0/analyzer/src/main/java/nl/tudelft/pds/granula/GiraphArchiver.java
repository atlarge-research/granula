package nl.tudelft.pds.granula;

import nl.tudelft.pds.granula.archiver.source.JobDirectorySource;
import nl.tudelft.pds.granula.archiver.source.JobSource;
import nl.tudelft.pds.granula.archiver.source.WorkloadFileSource;
import nl.tudelft.pds.granula.modeller.giraph.job.Giraph;
import nl.tudelft.pds.granula.util.JobListGenerator;
import nl.tudelft.pds.granula.util.UuidGenerator;

import java.io.File;
import java.util.Arrays;

/**
 * Created by wing on 21-8-15.
 */
public class GiraphArchiver {
    public static void main(String[] args) {

        // output
        String outputPath = Configuration.repoPath + "/data/archive/"+ UuidGenerator.getRandomUUID() + ".xml";
//        String outputPath = String.format(\"/home/wing/Workstation/Dropbox/Repo/granula/data/output/giraph.xml\", workloadLog.getName());



        // workload
//        String workloadDirPath = Configuration.repoPath + "/data/input/";
//        File workloadFile = new File(workloadDirPath + "/giraph.tar.gz");
//        WorkloadFileSource workloadSource = new WorkloadFileSource(workloadFile.getAbsolutePath());
//        workloadSource.load();
//
//        for (JobSource jobSource : workloadSource.getEmbeddedJobSources()) {
//            GranulaArchiver granulaArchiver = new GranulaArchiver(jobSource, new Giraph(), outputPath);
//            granulaArchiver.archive();
//        }

        // job
        JobDirectorySource jobDirSource = new JobDirectorySource(Configuration.repoPath + "/data/log/giraph-tmp");
        jobDirSource.load();

        GranulaArchiver granulaArchiver = new GranulaArchiver(jobDirSource, new Giraph(), outputPath);
        granulaArchiver.archive();



        // generate list
        (new JobListGenerator()).generateRecentJobsList();
    }
}
