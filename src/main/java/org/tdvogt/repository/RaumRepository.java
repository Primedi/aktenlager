package org.tdvogt.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.tdvogt.domain.Raum;

/**
 * Spring Data SQL repository for the Raum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaumRepository extends JpaRepository<Raum, Long> {}
