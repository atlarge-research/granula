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

package nl.tudelft.pds.granula.util.measurement;

import nl.tudelft.pds.granula.util.measurement.prefix.*;
import nl.tudelft.pds.granula.util.measurement.unit.*;
import nl.tudelft.pds.granula.util.measurement.unit.Byte;

import java.text.DecimalFormat;

/**
 * Created by wing on 14-1-15.
 */
public class Measurement {
    double baseValue;
    BaseUnit baseUnit;

    public static void main(String[] args) {
        Measurement m = new Measurement(1000.2903, new Mebi(), new Byte());
        System.out.println(m.toString());
    }

    public Measurement(double value, UnitPrefix unitPrefix, BaseUnit baseUnit) {
        this.baseValue = value * unitPrefix.multiplicationFactor;
        this.baseUnit = baseUnit;
    }

    @Override
    public String toString() {
        UnitPrefix unitPrefix = chooseDecimalUnitPrefix();


        return toString(unitPrefix, 2);
    }

    public String toString(UnitPrefix unitPrefix, int decimalPlace) {
        String pattern = "###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        decimalFormat.setMinimumFractionDigits(decimalPlace);
        decimalFormat.setMaximumFractionDigits(decimalPlace);
        String displayValue = decimalFormat.format(baseValue / unitPrefix.multiplicationFactor);
        return String.format("%s %s%s", displayValue, unitPrefix.label, baseUnit.symbol);
    }

    public UnitPrefix chooseDecimalUnitPrefix() {
        if(baseValue < (new Milli()).multiplicationFactor) {
            return new None();
        }
        else if(baseValue < (new None()).multiplicationFactor) {
            return new Milli();
        }
        else if(baseValue < (new Kilo()).multiplicationFactor) {
            return new None();
        }
        else if(baseValue < (new Mega()).multiplicationFactor) {
            return new Kilo();
        }
        else if(baseValue < (new Giga()).multiplicationFactor) {
            return new Mega();
        }
        else if(baseValue < (new Tera()).multiplicationFactor) {
            return new Giga();
        }
        else {
            return new Tera();
        }
    }



}
