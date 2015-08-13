package nl.tudelft.pds.granula;

import nl.tudelft.pds.granula.archiver.source.JobDirectorySource;
import nl.tudelft.pds.granula.modeller.mapreducev2.job.MapReduceV2;
import nl.tudelft.pds.granula.util.JobListGenerator;
import nl.tudelft.pds.granula.util.UuidGenerator;

/**
 * Created by wing on 21-8-15.
 */
public class MapReduceV2Archiver {
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
        JobDirectorySource jobDirSource = new JobDirectorySource(Configuration.repoPath + "/data/log/mapreducev2");
        jobDirSource.load();

        GranulaArchiver granulaArchiver = new GranulaArchiver(jobDirSource, new MapReduceV2(), outputPath);
        granulaArchiver.archive();

        // generate list
        (new JobListGenerator()).generateRecentJobsList();

    }
}
