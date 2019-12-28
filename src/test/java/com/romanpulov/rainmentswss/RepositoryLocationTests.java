package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.Location;
import com.romanpulov.rainmentswss.repository.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RepositoryLocationTests {

    private static final Logger log = Logger.getLogger(RepositoryLocationTests.class.getName());

    @BeforeAll
    static void prepareDB() {
        log.info("Before all tests");

        String templateDBFileName = System.getProperty("user.dir") + File.separator + "db" + File.separator + "database" + File.separator + "rainments.db";
        String testDBFileName = System.getProperty("user.dir") + File.separator + "db" + File.separator + "database" + File.separator + "rainments-test.db";

        log.info("templateDBFileName:" + templateDBFileName);
        log.info("testDBPath:" + testDBFileName);

        Path templateDB = Paths.get(templateDBFileName);
        Path testDB = Paths.get(testDBFileName);

        //delete test
        if (Files.exists(testDB)) {
            log.info(String.format("Deleting database file: %s", testDB));
            try {
                Files.delete(testDB);
            } catch (IOException e) {
                log.severe(String.format("Unable to delete test database file %s : %s", testDB, e.getMessage()));
            }
        }

        //copy new db
        try {
            Files.copy(templateDB, testDB);
        } catch (IOException e) {
            log.severe(String.format("Unable to copy test database file %s : %s", testDB, e.getMessage()));
        }
    }

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void mainTest() {
        Iterable<Location> locations;

        log.info("mainTest started");
        locations = locationRepository.findAll();
        assertThat(locations).isNotNull();

        Location newLocation = new Location();
        newLocation.setName("New Name");
        locationRepository.save(newLocation);

        locations = locationRepository.findAllByOrderByIdAsc();
        Location savedEntity = locations.iterator().next();

        log.info("Saved entity: " + savedEntity);

        assertThat(savedEntity.getName()).isEqualTo(newLocation.getName());

        locationRepository.deleteAll();
        locations = locationRepository.findAll();
        Assertions.assertFalse(locations.iterator().hasNext());

        log.info("getTest completed");
    }
}
