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


import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author TinyZ
 * @date 2016-10-13.
 */
public class Pair<L, R> implements Map.Entry<L, R>, Serializable {

    /**
     * Serialization version
     */
    private static final long serialVersionUID = 4954918890077093841L;

    private L left;
    private R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * <p>Obtains an immutable pair of from two objects inferring the generic types.</p>
     * <p>
     * <p>This factory allows the pair to be created using inference to
     * obtain the generic types.</p>
     *
     * @param <L>   the left element type
     * @param <R>   the right element type
     * @param left  the left element, may be null
     * @param right the right element, may be null
     * @return a pair formed from the two parameters, not null
     */
    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }

    /**
     * <p>Gets the left element from this pair.</p>
     * <p>
     * <p>When treated as a key-value pair, this is the key.</p>
     *
     * @return the left element, may be null
     */
    public L getLeft() {
        return left;
    }

    /**
     * Sets the left element of the pair.
     *
     * @param left the new value of the left element, may be null
     */
    public void setLeft(final L left) {
        this.left = left;
    }

    /**
     * <p>Gets the right element from this pair.</p>
     * <p>
     * <p>When treated as a key-value pair, this is the value.</p>
     *
     * @return the right element, may be null
     */
    public R getRight() {
        return right;
    }

    /**
     * Sets the right element of the pair.
     *
     * @param right the new value of the right element, may be null
     */
    public void setRight(final R right) {
        this.right = right;
    }

    /**
     * <p>Gets the key from this pair.</p>
     * <p>
     * <p>This method implements the {@code Map.Entry} interface returning the
     * left element as the key.</p>
     *
     * @return the left element as the key, may be null
     */
    @Override
    public final L getKey() {
        return getLeft();
    }

    /**
     * <p>Gets the value from this pair.</p>
     * <p>
     * <p>This method implements the {@code Map.Entry} interface returning the
     * right element as the value.</p>
     *
     * @return the right element as the value, may be null
     */
    @Override
    public R getValue() {
        return getRight();
    }

    /**
     * Sets the {@code Map.Entry} value.
     * This sets the right element of the pair.
     *
     * @param value the right value to set, not null
     * @return the old value for the right element
     */
    @Override
    public R setValue(final R value) {
        final R result = getRight();
        setRight(value);
        return result;
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Compares this pair to another based on the two elements.</p>
     *
     * @param obj the object to compare to, null returns false
     * @return true if the elements of the pair are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry<?, ?>) {
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            return Objects.equals(getKey(), other.getKey())
                    && Objects.equals(getValue(), other.getValue());
        }
        return false;
    }

    /**
     * <p>Returns a suitable hash code.
     * The hash code follows the definition in {@code Map.Entry}.</p>
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        // see Map.Entry API specification
        return (getKey() == null ? 0 : getKey().hashCode()) ^
                (getValue() == null ? 0 : getValue().hashCode());
    }

    /**
     * <p>Returns a String representation of this pair using the format {@code ($left,$right)}.</p>
     *
     * @return a string describing this object, not null
     */
    @Override
    public String toString() {
        return new StringBuilder().append('(').append(getLeft()).append(',').append(getRight()).append(')').toString();
    }

    /**
     * <p>Formats the receiver using the given format.</p>
     * <p>
     * <p>This uses {@link java.util.Formattable} to perform the formatting. Two variables may
     * be used to embed the left and right elements. Use {@code %1$s} for the left
     * element (key) and {@code %2$s} for the right element (value).
     * The default format used by {@code toString()} is {@code (%1$s,%2$s)}.</p>
     *
     * @param format the format string, optionally containing {@code %1$s} and {@code %2$s}, not null
     * @return the formatted string, not null
     */
    public String toString(final String format) {
        return String.format(format, getLeft(), getRight());
    }
}
