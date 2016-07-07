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

package org.ogcs.log.config;

/**
 * @author TinyZ
 * @date 2016-07-07.
 */
public class OkraConfig {

    /**
     * The server listen port.
     */
    private int port;
    private String hikariCPConfigPath;
    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;
    /**
     * The log struct file path.
     */
    private String logPath;
    /**
     * The xml xsd file path.
     */
    private String xsdPath;
    /**
     * The log string split's separator.
     */
    private char logSeparator;
    /**
     * Disruptor ringBuffer's size
     */
    private int ringBufferSize;

    public OkraConfig(int port, String hikariCPConfigPath, String databaseUrl, String databaseUsername, String databasePassword, int ringBufferSize, String xsdPath, String logPath, char logSeparator) {
        this.port = port;
        this.hikariCPConfigPath = hikariCPConfigPath;
        this.databaseUrl = databaseUrl;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.ringBufferSize = ringBufferSize;
        this.xsdPath = xsdPath;
        this.logPath = logPath;
        this.logSeparator = logSeparator;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHikariCPConfigPath() {
        return hikariCPConfigPath;
    }

    public void setHikariCPConfigPath(String hikariCPConfigPath) {
        this.hikariCPConfigPath = hikariCPConfigPath;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public int getRingBufferSize() {
        return ringBufferSize;
    }

    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    public String getXsdPath() {
        return xsdPath;
    }

    public void setXsdPath(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public char getLogSeparator() {
        return logSeparator;
    }

    public void setLogSeparator(char logSeparator) {
        this.logSeparator = logSeparator;
    }
}
