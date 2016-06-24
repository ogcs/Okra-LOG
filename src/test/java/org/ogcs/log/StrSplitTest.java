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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.ogcs.utilities.StringUtil;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
public class StrSplitTest {


    private static final String str = "asdasd,a,sd,a,sd,a,sd,a,s,da,sd,,s,,s,s,d,as,d,as,d,a,sd,,";
    private static final String str2 = "asdasd|a|sd|a|sd|a|sd|a|s|da|sd||s||s|s|d|as|d|as|d|a|sd||";

    public static final String DEFAULT = str2;
    private static final char delim = '|';

    private static final String[] array = new String[]{

    };

    @Test
    public void splitByNetty() {
        String[] split = io.netty.util.internal.StringUtil.split(DEFAULT, delim);
        System.out.println();
    }

    @Test
    public void splitByJava() {
        String[] split = DEFAULT.split("[|]");
        System.out.println();
    }

    @Test
    public void splitByOgcs() {
        String[] split = StringUtil.split(DEFAULT, delim);
        System.out.println();
    }

    @Test
    public void splitByOgcsWithoutNull() {
        String[] split = StringUtil.splitWithoutEmpty(DEFAULT, delim);
        System.out.println();
    }

    @Test
    public void splitByApache() {
        String[] split = StringUtils.split(DEFAULT, delim);
        System.out.println();
    }

}
