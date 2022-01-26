package org.tdvogt.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tdvogt.domain.Raum;
import org.tdvogt.repository.RaumRepository;
import org.tdvogt.service.RaumService;

/**
 * Service Implementation for managing {@link Raum}.
 */
@Service
@Transactional
public class RaumServiceImpl implements RaumService {

    private final Logger log = LoggerFactory.getLogger(RaumServiceImpl.class);

    private final RaumRepository raumRepository;

    public RaumServiceImpl(RaumRepository raumRepository) {
        this.raumRepository = raumRepository;
    }

    @Override
    public Raum save(Raum raum) {
        log.debug("Request to save Raum : {}", raum);
        return raumRepository.save(raum);
    }

    @Override
    public Optional<Raum> partialUpdate(Raum raum) {
        log.debug("Request to partially update Raum : {}", raum);

        return raumRepository
            .findById(raum.getId())
            .map(existingRaum -> {
                if (raum.getGebaeude() != null) {
                    existingRaum.setGebaeude(raum.getGebaeude());
                }
                if (raum.getRaumnummer() != null) {
                    existingRaum.setRaumnummer(raum.getRaumnummer());
                }
                if (raum.getEtage() != null) {
                    existingRaum.setEtage(raum.getEtage());
                }
                if (raum.getZusatz() != null) {
                    existingRaum.setZusatz(raum.getZusatz());
                }

                return existingRaum;
            })
            .map(raumRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Raum> findAll(Pageable pageable) {
        log.debug("Request to get all Raums");
        return raumRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Raum> findOne(Long id) {
        log.debug("Request to get Raum : {}", id);
        return raumRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Raum : {}", id);
        raumRepository.deleteById(id);
    }
}
