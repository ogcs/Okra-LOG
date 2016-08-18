package org.ogcs.log.util;

/**
 * @author TinyZ
 * @date 2016-08-13.
 */
public class RuntimeUtil {

    private RuntimeUtil() {
        //no-op
    }

    public static void addShutdownHook(Runnable task) {
        if (task == null)
            throw new NullPointerException("task");
        Runtime.getRuntime().addShutdownHook(new Thread(task));
    }
}
