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

package science.atlarge.granula.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by wing on 27-11-14.
 */
public class ExecutorUtil {

    ExecutorService executor;
    List<Future<Object>> futures = new ArrayList<>();

    public ExecutorUtil() {
        executor = Executors.newFixedThreadPool(10);
    }

    public ExecutorUtil(ExecutorService executor) {
        this.executor = executor;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public void submit(Callable callable) {
        futures.add(executor.submit(callable));
    }

    public List<Object> retrieve() {
        List<Object> results = new ArrayList<>();
        for (Future<Object> future : futures) {
            try {
                if(future.get() != null) {
                     results.add(future.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public void setBarrier() {
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
