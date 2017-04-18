package com.giorgimode.subzero.translator;

@FunctionalInterface
public interface VoidFunction {
    /**
     * Inteface to pass void methods.
     * and avoid using Runnable
     */
    void run();
}
