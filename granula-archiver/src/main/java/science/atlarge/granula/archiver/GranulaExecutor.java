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
package science.atlarge.granula.archiver;

import science.atlarge.granula.modeller.entity.Execution;
import science.atlarge.granula.modeller.job.JobModel;
import science.atlarge.granula.visualizer.VisualizerManager;

public class GranulaExecutor {

	Execution execution;
	boolean isEnvEnabled = true;
	boolean isPlatEnabled = true;

	public void buildJobArchive(JobModel jobModel) {
		if(isPlatEnabled) {
//			System.out.println("building platform archive.");
			try {
				PlatformArchiver platformArchiver = new PlatformArchiver();
				platformArchiver.buildArchive(execution, jobModel);
			} catch (Exception e) {
				System.out.println("Failed to build performance archive.");
				e.printStackTrace();
			}
		}

		if(isEnvEnabled) {
			System.out.println("building environment archive.");
			try {
				EnvironmentArchiver environmentArchiver = new EnvironmentArchiver();
				environmentArchiver.buildArchive(execution);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		VisualizerManager.addVisualizerResource(execution);
	}

	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}


	public void setEnvEnabled(boolean envEnabled) {
		isEnvEnabled = envEnabled;
	}

	public void setPlatEnabled(boolean platEnabled) {
		isPlatEnabled = platEnabled;
	}
}
