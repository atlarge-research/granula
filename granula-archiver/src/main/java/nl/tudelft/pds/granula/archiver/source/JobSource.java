package nl.tudelft.pds.granula.archiver.source;

/**
 * Created by wing on 24-8-15.
 */
public class JobSource extends Source {

    DataSource operationSource;
    DataSource utilSource;

    @Override
    public void verify() {
        if(operationSource == null || utilSource == null) {
            throw new IllegalStateException();
        } else {
            operationSource.verify();
            utilSource.verify();
        }
    }

    @Override
    public void load() {
        operationSource.load();
        utilSource.load();
    }

    public void setOperationSource(DataSource operationSource) {
        this.operationSource = operationSource;
    }

    public void setUtilSource(DataSource utilSource) {
        this.utilSource = utilSource;
    }

    public DataSource getOperationSource() {
        return operationSource;
    }

    public DataSource getUtilSource() {
        return utilSource;
    }

}
