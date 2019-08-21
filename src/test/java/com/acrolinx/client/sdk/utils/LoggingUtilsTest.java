/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.utils;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class LoggingUtilsTest
{
    @Test
    public void setupLogging() throws Exception
    {
        LoggingUtils.setupLogging("TEST");
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation != null);
        Assert.assertTrue(logFileLocation.contains("TEST"));
        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        Files.deleteIfExists(Paths.get(logFileLocation));
    }

    @Test
    public void defaultTestLevelIsINFO() throws Exception
    {
        LoggingUtils.setupLogging("TEST01");
        final Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                Logger.ROOT_LOGGER_NAME);
        final Level level = root.getLevel();
        assertTrue(level.toString().equalsIgnoreCase("INFO"));
        logger.debug("debug test");
        logger.warn("warning test");
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation != null);
        final List<String> strings = Files.readAllLines(Paths.get(logFileLocation), Charsets.UTF_8);
        for (String str : strings) {
            assertTrue(str.contains("WARN"));
            assertTrue(str.contains("warning test"));
        }

        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        Files.deleteIfExists(Paths.get(logFileLocation));
    }

    @Test
    public void setLevelToDEBUG() throws Exception
    {
        System.setProperty("acrolog.level", "debug");
        LoggingUtils.setupLogging("TEST02");
        final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                Logger.ROOT_LOGGER_NAME);
        final Level level = root.getLevel();
        assertTrue(level.toString().equalsIgnoreCase("DEBUG"));
        final Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        logger.debug("debug test1");
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation != null);
        final List<String> strings = Files.readAllLines(Paths.get(logFileLocation), Charsets.UTF_8);
        for (String str : strings) {
            assertTrue(str.contains("DEBUG"));
            assertTrue(str.contains("debug test1"));
        }
        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        Files.deleteIfExists(Paths.get(logFileLocation));
        System.clearProperty("acrolog.level");
    }

    @Test
    public void turnLoggingOff() throws Exception
    {
        System.setProperty("acrolog.level", "off");
        LoggingUtils.setupLogging("TEST03");
        final Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        logger.info("debug test112");
        logger.error("error test112");
        final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                Logger.ROOT_LOGGER_NAME);
        final Level level = root.getLevel();
        assertTrue(level.equals(Level.OFF));
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation != null);
        final List<String> strings = Files.readAllLines(Paths.get(logFileLocation), Charsets.UTF_8);
        for (String str : strings) {
            assertTrue("".equals(str));
        }
        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        Files.deleteIfExists(Paths.get(logFileLocation));
        System.clearProperty("acrolog.level");
    }

    @Test
    public void getLogFileWhenNoLoggingIsConfiguredReturnsNull()
    {
        LoggingUtils.resetLoggingContext();
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        assertTrue(logFileLocation == null);
    }

}
