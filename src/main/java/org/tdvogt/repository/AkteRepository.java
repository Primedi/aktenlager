package org.tdvogt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.tdvogt.domain.Akte;

/**
 * Spring Data SQL repository for the Akte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AkteRepository extends JpaRepository<Akte, Long> {
    @Query("select sum(aktenMeter) from Akte where haengend = true")
    public Long sumAktenMeterByHaengend();

    @Query("select sum(aktenMeter) from Akte where haengend = false")
    public Long sumAktenMeterByNotHaengend();
}
