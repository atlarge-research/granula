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
package science.atlarge.granula.monitor.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import science.atlarge.granula.monitor.comm.request.Request;
//import java.lang.annotation.Target;
//
/**
 * Created by wing on 27-11-14.
 */
public class JsonUtil {

    public static String toJson(Request request) {
        Gson gson = new Gson();
        return gson.toJson(request);
    }

    public static String toJson(Object object) {
        // http://stackoverflow.com/questions/4802887/gson-how-to-exclude-specific-fields-from-serialization-without-annotations
        Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        return gson.toJson(object);
    }

    public static Object fromJson(String jsonString, Class clazz) {
        return (new Gson()).fromJson(jsonString, clazz);
    }





}
