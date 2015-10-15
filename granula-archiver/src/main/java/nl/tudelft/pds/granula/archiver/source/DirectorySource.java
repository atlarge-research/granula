package nl.tudelft.pds.granula.archiver.source;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 24-8-15.
 */
public class DirectorySource extends DataSource {

    String filepath;


    public DirectorySource(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public void verify() {
        if(!(new File(filepath).exists())) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void load() {

        inputStreams = new ArrayList<>();
        List<File> files = new ArrayList<>(
                FileUtils.listFilesAndDirs(new File(filepath), TrueFileFilter.TRUE, TrueFileFilter.TRUE));

        for (File file : files) {
            if(file.isFile()) {
                try {
                    inputStreams.add(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    System.out.println(String.format("Log file %s was specfified in the data source, but it cannot be found.", file));
                    throw new IllegalStateException();
                }
            }
        }
    }


    public String getFilepath() {
        return filepath;
    }
}
