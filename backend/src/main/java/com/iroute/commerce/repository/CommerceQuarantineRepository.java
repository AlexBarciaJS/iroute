package com.iroute.commerce.repository;

import com.iroute.commerce.entity.CommerceQuarantine;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommerceQuarantineRepository extends JpaRepository<CommerceQuarantine, Long> {

    List<CommerceQuarantine> findAllByOrderByIdDesc();

    List<CommerceQuarantine> findByPcProcessdateOrderByIdDesc(LocalDate pcProcessdate);

    List<CommerceQuarantine> findByIdGreaterThanAndPcProcessdateOrderByIdAsc(
            Long id, LocalDate pcProcessdate);

    @Query("select coalesce(max(q.id), 0) from CommerceQuarantine q")
    Long findMaxId();
}
