/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import com.google.common.base.Preconditions;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class LoggingUtils {
    private LoggingUtils(){
        // This class just holds some static methods, you can't instantiate it.
    }


    private static Path getLogFilePathOSSpecific(String applicationName) {
        Path userTempDirLocation = getUserTempDirLocation();
        String s = System.getProperty("os.name").toLowerCase();
        if (s.contains("mac")) {
            return Paths.get(userTempDirLocation.toString(), "Logs", "Acrolinx", applicationName);
        }
        if (s.contains("win")) {
            return Paths.get(userTempDirLocation.toString(), "Acrolinx", "Logs", applicationName);
        }
        return Paths.get(userTempDirLocation.toString(), "acrolinx", "logs", applicationName);
    }

    private static Path getUserTempDirLocation() {
        final String s = System.getProperty("os.name").toLowerCase();
        String temDirProp;
        if (s.contains("mac")) {
            temDirProp = System.getProperty("user.home");
            return Paths.get(temDirProp, "Library");
        }
        return Paths.get(System.getProperty("java.io.tmpdir"));

    }

    private static void loadLogFileConfig(InputStream configStream, String applicationName)
            throws JoranException, IOException {
        if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) {
            if (configStream != null) {
                configStream.close();
            }
            return;
        }
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.putProperty("application-name", applicationName);
        loggerContext.putProperty("log-dir-path", getLogFilePathOSSpecific(applicationName).toString());
        configurator.doConfigure(configStream); // loads logback file
        configStream.close();
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.toLevel(System.getProperty("acrolog.level"), Level.INFO));
    }


    public static void setupLogging(String applicationName) throws IOException, JoranException {
        useDefaultLoggingConfig(applicationName);
    }

    private static void useDefaultLoggingConfig(String applicationName)
            throws IOException, JoranException {
        Preconditions.checkNotNull(applicationName, "application name should be set");
        InputStream configStream = LoggingUtils.class.getResourceAsStream("/logback_default.xml");
        loadLogFileConfig(configStream, applicationName);
    }

    public static String getLogFileLocation() {
        String logFileLocation = null;
        File clientLogFile;
        FileAppender<?> fileAppender = null;
        if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) {
            return null;
        }
        LoggerContext lContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : lContext.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext(); ) {
                Object enumElement = index.next();
                if (enumElement instanceof FileAppender) {
                    fileAppender = (FileAppender<?>) enumElement;
                }
            }
        }

        if (fileAppender != null) {
            clientLogFile = new File(fileAppender.getFile());
        } else {
            clientLogFile = null;
        }

        if (clientLogFile != null) {
            logFileLocation = clientLogFile.getPath();
        }

        return logFileLocation;
    }

    public static void resetLoggingContext() {
        if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) {
            return;
        }
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
    }
}

