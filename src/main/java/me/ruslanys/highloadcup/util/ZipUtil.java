package me.ruslanys.highloadcup.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class ZipUtil {

    private static final String INPUT_ZIP_FILE = "/tmp/data/data.zip";

    public static void main(String[] args) {
        Map<String, String> files = unzip(INPUT_ZIP_FILE);
        System.out.println();
    }

    public static Map<String, String> unzip(String zipFile) {
        Map<String, String> files = new HashMap<>();

        byte[] buffer = new byte[1024];

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }

                files.put(fileName, baos.toString());

                baos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            log.info("Unzip is done for: {}", files.keySet());
        } catch (IOException e) {
            log.error("Somethings wrong due the unzipping process", e);
        }

        return files;
    }
}