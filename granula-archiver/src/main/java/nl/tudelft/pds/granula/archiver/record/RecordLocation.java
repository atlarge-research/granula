/*
 * Copyright 2015 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.tudelft.pds.granula.archiver.record;

/**
 * Created by wing on 2-2-15.
 */
public class RecordLocation {
    String location;
    String lineNumber;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLocation(String filePath, int lineNumber) {
        this.location = String.format("%s(%s)", filePath, lineNumber);
        this.lineNumber = String.valueOf(lineNumber);
    }

    public void setLocation(String filePath, int lineNumber, String codeLocation) {
        this.location = String.format("%s - %s(%s)", codeLocation, filePath, lineNumber);
        this.lineNumber = String.valueOf(lineNumber);
    }

    public String getLineNumber() {
        return lineNumber;
    }
}
