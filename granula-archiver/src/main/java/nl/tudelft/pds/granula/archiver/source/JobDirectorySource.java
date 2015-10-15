package nl.tudelft.pds.granula.archiver.source;

import java.io.File;

/**
 * Created by wing on 24-8-15.
 */
public class JobDirectorySource extends JobSource {

    String dirPath;

    public JobDirectorySource(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public void verify() {
        super.verify();
    }

    @Override
    public void load() {
        setOperationSource(new DirectorySource(new File(dirPath).getAbsolutePath() + "/OperationLog"));
        setUtilSource(new DirectorySource(new File(dirPath).getAbsolutePath() +  "/UtilizationLog"));
        super.load();
    }
}
