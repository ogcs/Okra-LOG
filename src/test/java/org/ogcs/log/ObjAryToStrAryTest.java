/*
 *     Copyright 2016-2026 TinyZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ogcs.log;

import org.junit.Test;
import org.ogcs.log.parser.Field;
import org.ogcs.log.parser.Table;
import org.ogcs.log.parser.W3cDomParser;
import org.ogcs.log.util.MySQL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
public class ObjAryToStrAryTest {

    private static final Field[] fields;

    static {
        W3cDomParser w3c = new W3cDomParser("conf/aolog.xml");
        Table logMoney = w3c.getTable("log_money");

        System.out.println(MySQL.prepareQuery(logMoney));

        fields = logMoney.getFields();
    }

    @Test
    public void toAryByStream() {
        List<String> collect = Stream.of(fields).flatMap((field) -> {
            return Stream.of(field.getName());
        }).collect(Collectors.toList());
        String[] ary = new String[collect.size()];
        String[] strings = collect.toArray(ary);
        System.out.println();
    }

    @Test
    public void toArrayByFor() {
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            list.add(field.getName());
        }
        String[] ary = new String[list.size()];
        String[] strings = list.toArray(ary);
        System.out.println();
    }

}
