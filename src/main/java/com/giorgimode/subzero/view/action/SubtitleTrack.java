package com.giorgimode.subzero.view.action;

import lombok.Data;

/**
 * Created by modeg on 4/6/2017.
 */

@Data
public class SubtitleTrack {
    private final int id;
    private final String subtitlePath;
    private final String description;

    public SubtitleTrack(int id, String subtitlePath) {
        this.id = id;
        this.subtitlePath = subtitlePath;
        this.description = extractDescription(subtitlePath);
    }

    private String extractDescription(String subtitlePath) {
        if (!subtitlePath.endsWith(".srt")) {
            return subtitlePath;
        }
        int endIndex = subtitlePath.lastIndexOf(".");
        int startIndex = subtitlePath.substring(0, endIndex).lastIndexOf(".");
        String descr = subtitlePath.substring(startIndex + 1, endIndex);

        return String.format("%s%s [%s]", "Track ", id, descr);
    }
}
