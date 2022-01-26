package org.tdvogt.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tdvogt.domain.Raum;

/**
 * Service Interface for managing {@link Raum}.
 */
public interface RaumService {
    /**
     * Save a raum.
     *
     * @param raum the entity to save.
     * @return the persisted entity.
     */
    Raum save(Raum raum);

    /**
     * Partially updates a raum.
     *
     * @param raum the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Raum> partialUpdate(Raum raum);

    /**
     * Get all the raums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Raum> findAll(Pageable pageable);

    /**
     * Get the "id" raum.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Raum> findOne(Long id);

    /**
     * Delete the "id" raum.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
