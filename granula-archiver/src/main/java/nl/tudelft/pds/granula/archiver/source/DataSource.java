package nl.tudelft.pds.granula.archiver.source;

import java.util.List;

/**
 * Created by wing on 24-8-15.
 */
public abstract class DataSource extends Source {

    List<DataStream> dataStreams;

    public abstract void verify();
    public abstract void load();

    public List<DataStream> getDataStreams() {
        return dataStreams;
    }
}
