package me.ruslanys.highloadcup.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.ruslanys.highloadcup.DI;
import me.ruslanys.highloadcup.dao.LocationDao;
import me.ruslanys.highloadcup.dao.UserDao;
import me.ruslanys.highloadcup.dao.VisitDao;
import me.ruslanys.highloadcup.dto.load.LocationsImportDto;
import me.ruslanys.highloadcup.dto.load.UsersImportDto;
import me.ruslanys.highloadcup.dto.load.VisitsImportDto;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Slf4j
public class DataImporter implements StartupListener {

    private static final String FILE_PATH = "/tmp/data/data.zip";

    private final UserDao userDao;
    private final LocationDao locationDao;
    private final VisitDao visitDao;
    private final ObjectMapper objectMapper;

    public DataImporter() {
        this.userDao = DI.getBean(UserDao.class);
        this.locationDao = DI.getBean(LocationDao.class);
        this.visitDao = DI.getBean(VisitDao.class);
        this.objectMapper = DI.getBean(ObjectMapper.class);
    }

    @SneakyThrows
    @Override
    public void onStartup() {
        long startTime = System.currentTimeMillis();

        // --
        log.info("Importing users and locations...");
        importUsersAndLocations(FILE_PATH);

        log.info("Importing visits...");
        importVisits(FILE_PATH);


        // --
        log.info("=============================");
        log.info("Users in DB: {}", userDao.count());
        log.info("Locations in DB: {}", locationDao.count());
        log.info("Visits in DB: {}", visitDao.count());
        log.info("Data import is finished. It took {} sec", (System.currentTimeMillis() - startTime) / 1000);
    }

    private void importUsersAndLocations(String filePath) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(filePath))) {

            byte[] buffer = new byte[1024];
            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                String fileName = ze.getName();

                if (!fileName.startsWith("locations_") && !fileName.startsWith("users_")) {
                    continue;
                }

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }

                    if (fileName.startsWith("users_")) {
                        UsersImportDto dto = objectMapper.readValue(baos.toByteArray(), UsersImportDto.class);
                        dto.getUsers().forEach(userDao::add);
                    }

                    if (fileName.startsWith("locations_")) {
                        LocationsImportDto dto = objectMapper.readValue(baos.toByteArray(), LocationsImportDto.class);
                        dto.getLocations().forEach(locationDao::add);
                    }
                }
            }

            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            log.error("Somethings wrong due the unzipping process", e);
        }
    }

    private void importVisits(String filePath) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(filePath))) {

            byte[] buffer = new byte[1024];
            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                String fileName = ze.getName();

                if (!fileName.startsWith("visits_")) {
                    continue;
                }

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }

                    VisitsImportDto dto = objectMapper.readValue(baos.toByteArray(), VisitsImportDto.class);
                    dto.getVisits().forEach(visitDao::add);
                }
            }

            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            log.error("Somethings wrong due the unzipping process", e);
        }
    }

}
