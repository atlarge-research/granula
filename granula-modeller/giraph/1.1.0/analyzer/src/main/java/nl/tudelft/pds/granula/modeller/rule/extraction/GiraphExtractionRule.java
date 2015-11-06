package nl.tudelft.pds.granula.modeller.rule.extraction;

import nl.tudelft.pds.granula.archiver.source.DataStream;
import nl.tudelft.pds.granula.archiver.source.record.Record;
import nl.tudelft.pds.granula.archiver.source.record.RecordLocation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 21-8-15.
 */
public class GiraphExtractionRule extends ExtractionRule {

    public GiraphExtractionRule(int level) {
        super(level);
    }

    @Override
    public boolean execute() {
        return false;
    }

    public List<Record> extractRecordFromInputStream(DataStream dataStream) {

        List<Record> granulalogList = new ArrayList<>();

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(dataStream.getInputStream()));

            String line = null;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                if(line.contains("GRANULA")) {
                    Record record = extractRecord(line);

                    RecordLocation trace = new RecordLocation();
                    String codeLocation = "unspecified";

                    String[] lineParts = line.split("GRANULA");
                    if(lineParts.length > 1) {
                        String[] prefixParts = lineParts[0].split("\\s+");
                        if(dataStream.getPath().contains("driver")) {
                            codeLocation = prefixParts[prefixParts.length - 2] + " " + prefixParts[prefixParts.length - 1];
                        } else if(dataStream.getPath().contains("yarn")) {
                            codeLocation = prefixParts[prefixParts.length - 1].replace(":", "");
                        }

                    }

                    String logFilePath = dataStream.getPath();
                    trace.setLocation(logFilePath, lineCount, codeLocation);
                    record.setRecordLocation(trace);

                    granulalogList.add(record);
                }
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return granulalogList;
    }

    public Record extractRecord(String line) {
        Record record = new Record();

        String granulaLog = line.split("GRANULA")[1];
        String[] recordAttrs = granulaLog.split("\\s+");

        for (String recordAttr : recordAttrs) {
            if (recordAttr.contains(":")) {
                String[] attrKeyValue = recordAttr.split(":");
                if (attrKeyValue.length == 2) {

                    String name = attrKeyValue[0];
                    String value = attrKeyValue[1];
                    String unescapedValue = value.replaceAll("\\[COLON\\]", ":");

                    record.addRecordInfo(name, unescapedValue);
                } else {
                    record.addRecordInfo(attrKeyValue[0], "");
                }
            }
        }
        return record;
    }
}
