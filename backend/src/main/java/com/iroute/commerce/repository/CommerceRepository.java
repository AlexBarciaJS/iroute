package com.iroute.commerce.repository;

import com.iroute.commerce.entity.Commerce;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommerceRepository extends JpaRepository<Commerce, Long> {

    List<Commerce> findByPcProcessdate(LocalDate pcProcessdate);

    long countByPcProcessdate(LocalDate pcProcessdate);
}
