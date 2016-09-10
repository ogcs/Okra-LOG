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

package org.ogcs.log.test;

import org.junit.Test;
import org.ogcs.log.util.TimeV8Util;

import java.time.LocalDateTime;

/**
 * @author TinyZ
 * @date 2016-09-09.
 */
public class TimeV8Test {

    @Test
    public void test(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        String s1 = TimeV8Util.dateTime(now);

//        now.with(TemporalAdjusters.firstInMonth(DayOfWeek.of()))

        LocalDateTime localDateTime = now.plusDays(1);
        String s2 = TimeV8Util.dateTime(localDateTime);
        System.out.println();
    }
}
