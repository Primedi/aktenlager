package org.tdvogt.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.tdvogt.domain.Akte;
import org.tdvogt.repository.AkteRepository;
import org.tdvogt.service.AkteService;
import org.tdvogt.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.tdvogt.domain.Akte}.
 */
@RestController
@RequestMapping("/api")
public class AkteResource {

    private final Logger log = LoggerFactory.getLogger(AkteResource.class);

    private static final String ENTITY_NAME = "akte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AkteService akteService;

    private final AkteRepository akteRepository;

    public AkteResource(AkteService akteService, AkteRepository akteRepository) {
        this.akteService = akteService;
        this.akteRepository = akteRepository;
    }

    /**
     * {@code POST  /aktes} : Create a new akte.
     *
     * @param akte the akte to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new akte, or with status {@code 400 (Bad Request)} if the akte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aktes")
    public ResponseEntity<Akte> createAkte(@RequestBody Akte akte) throws URISyntaxException {
        log.debug("REST request to save Akte : {}", akte);
        if (akte.getId() != null) {
            throw new BadRequestAlertException("A new akte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Akte result = akteService.save(akte);
        return ResponseEntity
            .created(new URI("/api/aktes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aktes/:id} : Updates an existing akte.
     *
     * @param id the id of the akte to save.
     * @param akte the akte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated akte,
     * or with status {@code 400 (Bad Request)} if the akte is not valid,
     * or with status {@code 500 (Internal Server Error)} if the akte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aktes/{id}")
    public ResponseEntity<Akte> updateAkte(@PathVariable(value = "id", required = false) final Long id, @RequestBody Akte akte)
        throws URISyntaxException {
        log.debug("REST request to update Akte : {}, {}", id, akte);
        if (akte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, akte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!akteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Akte result = akteService.save(akte);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, akte.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aktes/:id} : Partial updates given fields of an existing akte, field will ignore if it is null
     *
     * @param id the id of the akte to save.
     * @param akte the akte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated akte,
     * or with status {@code 400 (Bad Request)} if the akte is not valid,
     * or with status {@code 404 (Not Found)} if the akte is not found,
     * or with status {@code 500 (Internal Server Error)} if the akte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aktes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Akte> partialUpdateAkte(@PathVariable(value = "id", required = false) final Long id, @RequestBody Akte akte)
        throws URISyntaxException {
        log.debug("REST request to partial update Akte partially : {}, {}", id, akte);
        if (akte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, akte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!akteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Akte> result = akteService.partialUpdate(akte);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, akte.getId().toString())
        );
    }

    /**
     * {@code GET  /aktes} : get all the aktes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aktes in body.
     */
    @GetMapping("/aktes")
    public ResponseEntity<List<Akte>> getAllAktes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Aktes");
        Page<Akte> page = akteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aktes/:id} : get the "id" akte.
     *
     * @param id the id of the akte to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the akte, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aktes/{id}")
    public ResponseEntity<Akte> getAkte(@PathVariable Long id) {
        log.debug("REST request to get Akte : {}", id);
        Optional<Akte> akte = akteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(akte);
    }

    /**
     * {@code GET  /aktes/summe} : get Summe der Aktenmeter
     *
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the akte, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aktes/summe")
    public ResponseEntity<Long> getSumme() {
        log.debug("REST request to get Summe der Aktenmeter");
        Long meteranzahl = akteService.getGesamtmeter();
        return ResponseUtil.wrapOrNotFound(Optional.of(meteranzahl));
    }

    /**
     * {@code DELETE  /aktes/:id} : delete the "id" akte.
     *
     * @param id the id of the akte to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aktes/{id}")
    public ResponseEntity<Void> deleteAkte(@PathVariable Long id) {
        log.debug("REST request to delete Akte : {}", id);
        akteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
