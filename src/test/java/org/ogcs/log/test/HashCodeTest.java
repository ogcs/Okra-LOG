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

import org.junit.Assert;
import org.junit.Test;
import org.ogcs.log.util.HashCodeUtil;

/**
 * @author TinyZ
 * @date 2016-08-26.
 */
public class HashCodeTest {

    @Test
    public void testHashCode() {
        // String
        Assert.assertEquals(HashCodeUtil.hashCode("test"), HashCodeUtil.hashCode("test"));
        Assert.assertNotEquals(HashCodeUtil.hashCode("test"), HashCodeUtil.hashCode("test1"));
        // int
        Assert.assertEquals(HashCodeUtil.hashCode(1), HashCodeUtil.hashCode(1));
        Assert.assertNotEquals(HashCodeUtil.hashCode(1), HashCodeUtil.hashCode(2));
        // long
        Assert.assertEquals(HashCodeUtil.hashCode(1L), HashCodeUtil.hashCode(1L));
        Assert.assertNotEquals(HashCodeUtil.hashCode(1L), HashCodeUtil.hashCode(2L));

        // bean
        Assert.assertNotEquals(HashCodeUtil.hashCode("test", "1"), HashCodeUtil.hashCode("test", "2"));
        Assert.assertNotEquals(HashCodeUtil.hashCode("test", 1L), HashCodeUtil.hashCode("test", 2.01));
        Assert.assertNotEquals(HashCodeUtil.hashCode("test", new long[]{1L}), HashCodeUtil.hashCode("test", new double[]{1.0}));

        Assert.assertEquals(HashCodeUtil.hashCode("test", new int[]{1}), HashCodeUtil.hashCode("test", new int[]{1}));
        Assert.assertNotEquals(HashCodeUtil.hashCode("test", new int[]{1}), HashCodeUtil.hashCode("test", new int[]{1, 2}));
        Assert.assertEquals(HashCodeUtil.hashCode("test", new int[]{1}, true), HashCodeUtil.hashCode("test", new int[]{1}, true));
    }
}
