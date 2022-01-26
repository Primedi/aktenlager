package org.tdvogt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tdvogt.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
