package org.tdvogt.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tdvogt.domain.Akte;

/**
 * Service Interface for managing {@link Akte}.
 */
public interface AkteService {
    /**
     * Save a akte.
     *
     * @param akte the entity to save.
     * @return the persisted entity.
     */
    Akte save(Akte akte);

    /**
     * Partially updates a akte.
     *
     * @param akte the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Akte> partialUpdate(Akte akte);

    /**
     * Get all the aktes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Akte> findAll(Pageable pageable);

    /**
     * Get the "id" akte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Akte> findOne(Long id);

    /**
     * Delete the "id" akte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Werte die Gesamtmeterzahl aus
     * @return die Gesamtmeterzahl an Akten aus allen Räumen
     */
    Long getGesamtmeter();
}
