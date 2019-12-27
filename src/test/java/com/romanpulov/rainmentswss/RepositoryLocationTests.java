package com.romanpulov.rainmentswss;

import com.romanpulov.rainmentswss.entity.Location;
import com.romanpulov.rainmentswss.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RepositoryLocationTests {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void getTest() {
        Iterable<Location> locations = locationRepository.findAll();
        assertThat(locations).isNotNull();
    }
}
