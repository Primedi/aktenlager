package org.tdvogt.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.tdvogt.domain.Akte;

/**
 * Spring Data SQL repository for the Akte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AkteRepository extends JpaRepository<Akte, Long> {}
