package nl.tudelft.pds.granula.archiver.source;

import java.io.InputStream;

/**
 * Created by wlngai on 27-10-15.
 */
public class DataStream {
    private String path;
    private InputStream inputStream;

    public DataStream(String path, InputStream inputStream) {
        this.path = path;
        this.inputStream = inputStream;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
