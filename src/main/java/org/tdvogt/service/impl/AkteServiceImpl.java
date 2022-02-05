package org.tdvogt.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tdvogt.domain.Akte;
import org.tdvogt.repository.AkteRepository;
import org.tdvogt.service.AkteService;

/**
 * Service Implementation for managing {@link Akte}.
 */
@Service
@Transactional
public class AkteServiceImpl implements AkteService {

    private final Logger log = LoggerFactory.getLogger(AkteServiceImpl.class);

    private final AkteRepository akteRepository;

    public AkteServiceImpl(AkteRepository akteRepository) {
        this.akteRepository = akteRepository;
    }

    @Override
    public Akte save(Akte akte) {
        log.debug("Request to save Akte : {}", akte);
        return akteRepository.save(akte);
    }

    @Override
    public Optional<Akte> partialUpdate(Akte akte) {
        log.debug("Request to partially update Akte : {}", akte);

        return akteRepository
            .findById(akte.getId())
            .map(existingAkte -> {
                if (akte.getAktenthema() != null) {
                    existingAkte.setAktenthema(akte.getAktenthema());
                }
                if (akte.getOrganisationsEinheit() != null) {
                    existingAkte.setOrganisationsEinheit(akte.getOrganisationsEinheit());
                }
                if (akte.getAktenMeter() != null) {
                    existingAkte.setAktenMeter(akte.getAktenMeter());
                }
                if (akte.getHaengend() != null) {
                    existingAkte.setHaengend(akte.getHaengend());
                }
                if (akte.getStandort() != null) {
                    existingAkte.setStandort(akte.getStandort());
                }

                return existingAkte;
            })
            .map(akteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Akte> findAll(Pageable pageable) {
        log.debug("Request to get all Aktes");
        return akteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Akte> findOne(Long id) {
        log.debug("Request to get Akte : {}", id);
        return akteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Akte : {}", id);
        akteRepository.deleteById(id);
    }

    @Override
    public Long getGesamtmeter() {
        log.debug("Request to get Gesamtmeteranzahl aller gespeicherter Akten");
        List<Akte> akteList = akteRepository.findAll();
        Long summe = 0L;
        for (Akte akte : akteList) {
            summe = summe + akte.getAktenMeter();
        }
        return summe;
    }

    @Override
    public Long getAktenmeterHaengend() {
        log.debug("Request to get Gesamtmeteranzahl aller haengenden Akten");
        return akteRepository.sumAktenMeterByHaengend();
    }
    /*@Override
    public Long getAktenMeterNotHaengend() {
        return null;
    }
    */
}
