package nl.tudelft.pds.granula.modeller.rule.extraction;

import nl.tudelft.pds.granula.archiver.source.record.Record;
import nl.tudelft.pds.granula.modeller.rule.Rule;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by wing on 21-8-15.
 */
public abstract class ExtractionRule extends Rule {

    public ExtractionRule(int level) {
        super(level);
    }

    public abstract List<Record> extractRecordFromInputStream(InputStream fis);

}
