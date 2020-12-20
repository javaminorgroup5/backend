package nl.hro.cookbook.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ImageHelper {

    static Resource createTempFileResource(byte [] content) throws IOException {
        Path tempFile = Files.createTempFile("upload-file", ".txt");
        Files.write(tempFile, content);
        return new FileSystemResource(tempFile.toFile());
    }
}
