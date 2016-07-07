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

import org.junit.Assert;
import org.junit.Test;
import org.ogcs.log.util.XmlUtil;

/**
 * 检验XmlUtil是否正确
 *
 * @author TinyZ
 * @date 2016-07-07.
 */
public class XmlUtilTest {

    @Test
    public void testValidateXml() {
        boolean tf = XmlUtil.validateXml("./conf/aolog.xml");
        Assert.assertEquals(tf, true);
    }

    @Test
    public void testValidateXml2() {
        // okra-log.xsd文件默认在类路径下
        boolean tf = XmlUtil.validateXml(XmlUtil.class.getResource("/okra-log.xsd").getPath(), "./conf/aolog.xml");
        Assert.assertEquals(tf, true);
    }
}
