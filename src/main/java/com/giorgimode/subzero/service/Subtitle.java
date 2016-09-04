package com.giorgimode.subzero.service;

import java.io.File;
import java.util.Map;

/**
 * Created by modeg on 8/25/2016.
 */
public class Subtitle {
    private int numberOfLines;
    private String raw;
    private Map<String,String> subtitleLinesMap;
    private File subtitleFile;

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public Map<String, String> getSubtitleLinesMap() {
        return subtitleLinesMap;
    }

    public void setSubtitleLinesMap(Map<String, String> subtitleLinesMap) {
        this.subtitleLinesMap = subtitleLinesMap;
    }

    public File getSubtitleFile() {
        return subtitleFile;
    }

    public void setSubtitleFile(File subtitleFile) {
        this.subtitleFile = subtitleFile;
    }
}
