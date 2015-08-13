package nl.tudelft.pds.granula.util;

import java.io.File;
import java.util.Arrays;

/**
 * Created by wlngai on 4-9-15.
 */
public class FileUtil {

    public static File lastFile(String dirPath) {

        File workloadDir = new File(dirPath);
        File[] workloadFiles = workloadDir.listFiles();

        if(workloadFiles == null || workloadFiles.length < 1) {
            throw new IllegalStateException("No files found.");
        }

        Arrays.sort(workloadFiles);
        return workloadFiles[workloadFiles.length - 1];
    }
}
