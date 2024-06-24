/*
 * Copyright 2017-2018 Youcef DEBBAH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsoftware95.toolkit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Objects;

/**
 * This is a utility class can map standard system output streams ({@code System.out} and {@code System.err}) to a
 * log4j2 Logger, which permit a proper logging to invocation such as:
 * <pre><code>
 *     SystemLogger.linkWithSystem();
 *     System.out.println("string"); // this will behave as if written: LogManager.getLogger(SystemLogger.class).info("string");
 * </code></pre>
 * This mapping has some limitation:
 * <ul>
 * <li>invoking {@code print}/{@code append} will have the same effect as {@code println} (both will write a new
 * line)</li>
 * <li>calling a low level {@code write} method will cause the cause the input to get decoded before logged (then the
 * logger will encode it again), which may cause a performance hit (the {@link Charset#defaultCharset()} used my even
 * lead to some encoding issues)</li>
 * </ul>
 */
public class SystemLogger {

    // God, should I comment these too? nah...
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static final PrintStream sysout = System.out;
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static final PrintStream syserr = System.err;

    /**
     * Links the standard system output streams ({@code System.out} and {@code System.err}) to a new logger using the
     * fully qualified name of this class as the Logger name. The {@link Level#INFO} will be used to log output sent to
     * {@code System.out} and {@link Level#ERROR} for output sent to {@code System.err}
     *
     * @param addDebugInfo if {@code true} then additional information will be prefixed to the output
     */
    public static void linkWithSystem(final boolean addDebugInfo) {
        linkWithSystem(Level.INFO, Level.ERROR, addDebugInfo);
    }

    /**
     * Links the default system output streams ({@code System.out} and {@code System.err}) to a new logger using the
     * fully qualified name of this class as the Logger name. The {@code outLevel} will be used to log output sent to
     * {@code System.out} and {@code errLevel} for output sent to {@code System.err}
     *
     * @param outLevel     the logging level for output sent to {@code System.out}
     * @param errLevel     the logging level for output sent to {@code System.err}
     * @param addDebugInfo if {@code true} then additional information will be prefixed to the output
     */
    public static void linkWithSystem(final Level outLevel, final Level errLevel, final boolean addDebugInfo) {
        linkWithSystem(SystemLogger.class, outLevel, errLevel, addDebugInfo);
    }

    /**
     * Links the standard system output streams ({@code System.out} and {@code System.err}) to a new logger using the
     * fully qualified name of the given class ({@code loggerName}) as the Logger name. The {@code outLevel} will be
     * used to log output sent to {@code System.out} and {@code errLevel} for output sent to {@code System.err}<p>
     * if {@code addDebugInfo} is {@code true} then an additional information will be added to the output (by analysing
     * the stack track) in the following syntax:
     * <pre>
     *     [{caller class}#{calling method name} at {class file name}:{line number}] - {output}
     * </pre>
     * in case no such information are available in the stack track then the following will be prefixed instead
     * <pre>
     *     [no info available] - {output}
     * </pre>
     *
     * @param loggerName   The class that its fully qualified name will be used as name for the new logger
     * @param outLevel     the logging level for output sent to {@code System.out}
     * @param errLevel     the logging level for output sent to {@code System.err}
     * @param addDebugInfo if {@code true} then additional information will be prefixed to the output
     *
     * @throws NullPointerException if {@code loggerName} is {@code null}
     */
    public static void linkWithSystem(final Class<?> loggerName, final Level outLevel, final Level errLevel, final boolean addDebugInfo) {
        Objects.requireNonNull(loggerName.getName());
        final Logger logger = LogManager.getLogger(loggerName);

        System.setOut(new logAdapter(sysout, logger, outLevel, addDebugInfo));
        System.setErr(new logAdapter(syserr, logger, errLevel, addDebugInfo));
    }

    /**
     * Sets the standard output streams to the originals (the ones that were set to {@code System} when this class was
     * loaded).
     */
    public static void unlink() {
        System.setOut(sysout);
        System.setErr(syserr);
    }

    // a print stream that write the output to a logger
    private static final class logAdapter extends PrintStream {

        private final Logger logger;
        private final Level level;
        private final boolean addDebugInfo;

        private logAdapter(final @NotNull OutputStream out, final @NotNull Logger logger, final @NotNull Level level, final boolean addDebugInfo) {
            super(out, true);
            this.logger = Objects.requireNonNull(logger);
            this.level = Objects.requireNonNull(level);
            this.addDebugInfo = addDebugInfo;
        }

        @Override
        public void println(final String s) {
            logger.log(level, prefixDebugInfo(s));
        }

        private String prefixDebugInfo(final String s) {
            final StringBuilder buff = new StringBuilder();

            if (addDebugInfo) {
                buff.append('[');

                final StackTraceElement caller = getCaller();
                if (caller == null) {
                    buff.append("no info available");
                } else {
                    buff.append(caller.getClassName());
                    buff.append('#');
                    buff.append(caller.getMethodName());
                    buff.append(" at ");
                    buff.append(caller.getFileName());
                    buff.append(":");
                    buff.append(caller.getLineNumber());
                }

                buff.append("] - ");
            }

            buff.append(s);
            return buff.toString();
        }

        private StackTraceElement getCaller() {
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            final String thisClassName = this.getClass().getName();

            for (int i = 1; i < stackTrace.length; i++) {
                final StackTraceElement element = stackTrace[i];
                if (!thisClassName.equals(element.getClassName()))
                    return element;
            }

            return null;
        }

        @Override
        public void println(final @NotNull char[] array) {
            println(new String(array));
        }

        @Override
        public void write(final int oneByte) {
            final byte[] buffer = {(byte) oneByte};
            println(new String(buffer));
        }

        @Override
        public void write(final @NotNull byte[] buffer, final int offSet, final int length) {
            println(new String(buffer, offSet, length));
        }

        @Override
        public void println(final boolean x) {
            println(String.valueOf(x));
        }

        @Override
        public void println(final char x) {
            println(String.valueOf(x));
        }

        @Override
        public void println(final int x) {
            println(String.valueOf(x));
        }

        @Override
        public void println(final long x) {
            println(String.valueOf(x));
        }

        @Override
        public void println(final float x) {
            println(String.valueOf(x));
        }

        @Override
        public void println(final double x) {
            println(String.valueOf(x));
        }

        @Override
        public void println(final Object x) {
            println(String.valueOf(x));
        }

        @Override
        public void println() {
            println("");
        }

        @Override
        public void print(final boolean x) {
            println(x);
        }

        @Override
        public void print(final char x) {
            println(x);
        }

        @Override
        public void print(final int x) {
            println(x);
        }

        @Override
        public void print(final long x) {
            println(x);
        }

        @Override
        public void print(final float x) {
            println(x);
        }

        @Override
        public void print(final double x) {
            println(x);
        }

        @Override
        public void print(final @NotNull char[] x) {
            println(x);
        }

        @Override
        public void print(final String x) {
            println(x);
        }

        @Override
        public void print(final Object x) {
            println(x);
        }

        @Override
        public PrintStream format(final @NotNull String format, final Object... args) {
            final String output = String.format(format, args);
            logger.log(level, output);
            return this;
        }

        @Override
        public PrintStream format(final Locale locale, final @NotNull String format, final Object... args) {
            final String output = String.format(locale, format, args);
            logger.log(level, output);
            return this;
        }

    }
}
