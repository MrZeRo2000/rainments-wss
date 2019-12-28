package com.romanpulov.rainmentswss.repository;

import com.romanpulov.rainmentswss.entity.Location;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LocationRepository extends PagingAndSortingRepository<Location, Long> {
    public List<Location> findAllByOrderByIdAsc();
}
