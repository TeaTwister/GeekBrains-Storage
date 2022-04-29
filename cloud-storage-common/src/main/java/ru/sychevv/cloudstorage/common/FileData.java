package ru.sychevv.cloudstorage.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileData {
    public enum FileType {
        FILE("F"),
        DIRECTORY("D");

        private final String TYPE;

        FileType(String type) {
            this.TYPE = type;
        }

        public String getType() {
            return TYPE;
        }
    }

    private String name;
    private FileType type;
    private long size;
    private LocalDateTime modified;

    public FileData(Path path) {
        try {
            this.name = path.getFileName().toString();
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            this.size = (this.type == FileType.DIRECTORY) ? -1L : Files.size(path);
            this.modified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public String getType() {
        return type.getType();
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getModified() {
        return modified;
    }
}
