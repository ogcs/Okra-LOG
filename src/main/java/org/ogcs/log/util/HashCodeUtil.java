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

package org.ogcs.log.util;

/**
 * @author TinyZ
 * @since 1.0
 */
public class HashCodeUtil {

    private static final int iConstant = 37;

    /**
     * @param objects
     * @return Return the hash code.
     */
    public static int hashCode(Object... objects) {
        int total = 17;
        for (Object obj : objects) {
            total = hashCode(total, iConstant, obj);
        }
        return total;
    }

    private static int hashCode(final int total, final int constant, final long value) {
        return total * constant + ((int) (value ^ (value >> 32)));
    }

    private static int hashCode(final int total, final int constant, final int value) {
        return total * constant + value;
    }

    private static int hashCode(final int total, final int constant, final float value) {
        return total * constant + Float.floatToIntBits(value);
    }

    private static int hashCode(final int total, final int constant, final double value) {
        return hashCode(total, constant, Double.doubleToLongBits(value));
    }

    private static int hashCode(final int total, final int constant, final short value) {
        return total * constant + value;
    }

    private static int hashCode(final int total, final int constant, final char value) {
        return total * constant + value;
    }

    private static int hashCode(final int total, final int constant, final byte value) {
        return total * constant + value;
    }

    private static int hashCode(final int total, final int constant, final boolean value) {
        return total * constant + (value ? 1 : 0);
    }

    private static int hashCode(int total, final int constant, final Object obj) {
        if (obj == null) {
            total = total * constant;
        } else if (obj.getClass().isArray()) {
            if (obj instanceof long[]) {
                for (long var : (long[]) obj) {
                    total = hashCode(total, constant, var);
                }
            } else if (obj instanceof int[]) {
                for (int var : (int[]) obj) {
                    total = hashCode(total, constant, var);
                }
            } else if (obj instanceof short[]) {
                for (short var : (short[]) obj) {
                    total = hashCode(total, constant, var);
                }
            } else if (obj instanceof char[]) {
                for (char var : (char[]) obj) {
                    total = hashCode(total, constant, var);
                }
            } else if (obj instanceof byte[]) {
                for (byte var : (byte[]) obj) {
                    total = hashCode(total, constant, var);
                }
            } else if (obj instanceof double[]) {
                for (double var : (double[]) obj) {
                    total = hashCode(total, constant, var);
                }
            } else if (obj instanceof float[]) {
                for (float var : (float[]) obj) {
                    total = hashCode(total, constant, var);
                }
            } else if (obj instanceof boolean[]) {
                for (boolean var : (boolean[]) obj) {
                    total = hashCode(total, constant, var);
                }
            } else {
                for (Object var : (Object[]) obj) {
                    total = hashCode(total, constant, var);
                }
            }
        } else {
            total = total * constant + obj.hashCode();
        }
        return total;
    }


    public static void main(String[] args) {

        int var1 = hashCode("xx", new int[]{1}, true);
        int var2 = hashCode("xx", new int[]{1}, true);
        System.out.println();
    }

}
