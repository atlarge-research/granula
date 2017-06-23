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

package science.atlarge.granula.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by wing on 27-11-14.
 */
public class JsonUtil {

    public static String toJson(Object object) {
        // http://stackoverflow.com/questions/4802887/gson-how-to-exclude-specific-fields-from-serialization-without-annotations
        Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        return gson.toJson(object);
    }

    public static String toPrettyJson(Object object) {
        // http://stackoverflow.com/questions/4802887/gson-how-to-exclude-specific-fields-from-serialization-without-annotations
        Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).setPrettyPrinting().create();
        return gson.toJson(object);
    }


    public static Object fromJson(String jsonString, Class clazz) {
        return (new Gson()).fromJson(jsonString, clazz);
    }


    public static Object createNewInstance(Object object) {
        return JsonUtil.fromJson(JsonUtil.toJson(object), object.getClass());
    }



}
