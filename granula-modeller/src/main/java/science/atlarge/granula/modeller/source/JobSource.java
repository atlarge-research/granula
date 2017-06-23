/*
 * Copyright 2015 - 2017 Atlarge Research Team,
 * operating at Technische Universiteit Delft
 * and Vrije Universiteit Amsterdam, the Netherlands.
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
package science.atlarge.granula.modeller.source;

/**
 * Created by wing on 24-8-15.
 */
public class JobSource extends Source {

    DataSource sysSource;
    DataSource envSource;

    @Override
    public void verify() {
        if(sysSource == null || envSource == null) {
            throw new IllegalStateException();
        } else {
            sysSource.verify();
            envSource.verify();
        }
    }

    @Override
    public void load() {
        sysSource.load();
        //envSource.load();
    }

    public void setSysSource(DataSource sysSource) {
        this.sysSource = sysSource;
    }

    public void setEnvSource(DataSource envSource) {
        this.envSource = envSource;
    }

    public DataSource getSysSource() {
        return sysSource;
    }

    public DataSource getEnvSource() {
        return envSource;
    }

}
