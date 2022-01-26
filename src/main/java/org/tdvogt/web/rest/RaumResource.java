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
import org.tdvogt.domain.Raum;
import org.tdvogt.repository.RaumRepository;
import org.tdvogt.service.RaumService;
import org.tdvogt.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.tdvogt.domain.Raum}.
 */
@RestController
@RequestMapping("/api")
public class RaumResource {

    private final Logger log = LoggerFactory.getLogger(RaumResource.class);

    private static final String ENTITY_NAME = "raum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaumService raumService;

    private final RaumRepository raumRepository;

    public RaumResource(RaumService raumService, RaumRepository raumRepository) {
        this.raumService = raumService;
        this.raumRepository = raumRepository;
    }

    /**
     * {@code POST  /raums} : Create a new raum.
     *
     * @param raum the raum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raum, or with status {@code 400 (Bad Request)} if the raum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/raums")
    public ResponseEntity<Raum> createRaum(@RequestBody Raum raum) throws URISyntaxException {
        log.debug("REST request to save Raum : {}", raum);
        if (raum.getId() != null) {
            throw new BadRequestAlertException("A new raum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Raum result = raumService.save(raum);
        return ResponseEntity
            .created(new URI("/api/raums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /raums/:id} : Updates an existing raum.
     *
     * @param id the id of the raum to save.
     * @param raum the raum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raum,
     * or with status {@code 400 (Bad Request)} if the raum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/raums/{id}")
    public ResponseEntity<Raum> updateRaum(@PathVariable(value = "id", required = false) final Long id, @RequestBody Raum raum)
        throws URISyntaxException {
        log.debug("REST request to update Raum : {}, {}", id, raum);
        if (raum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!raumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Raum result = raumService.save(raum);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, raum.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /raums/:id} : Partial updates given fields of an existing raum, field will ignore if it is null
     *
     * @param id the id of the raum to save.
     * @param raum the raum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raum,
     * or with status {@code 400 (Bad Request)} if the raum is not valid,
     * or with status {@code 404 (Not Found)} if the raum is not found,
     * or with status {@code 500 (Internal Server Error)} if the raum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/raums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Raum> partialUpdateRaum(@PathVariable(value = "id", required = false) final Long id, @RequestBody Raum raum)
        throws URISyntaxException {
        log.debug("REST request to partial update Raum partially : {}, {}", id, raum);
        if (raum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!raumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Raum> result = raumService.partialUpdate(raum);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, raum.getId().toString())
        );
    }

    /**
     * {@code GET  /raums} : get all the raums.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raums in body.
     */
    @GetMapping("/raums")
    public ResponseEntity<List<Raum>> getAllRaums(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Raums");
        Page<Raum> page = raumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /raums/:id} : get the "id" raum.
     *
     * @param id the id of the raum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/raums/{id}")
    public ResponseEntity<Raum> getRaum(@PathVariable Long id) {
        log.debug("REST request to get Raum : {}", id);
        Optional<Raum> raum = raumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raum);
    }

    /**
     * {@code DELETE  /raums/:id} : delete the "id" raum.
     *
     * @param id the id of the raum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/raums/{id}")
    public ResponseEntity<Void> deleteRaum(@PathVariable Long id) {
        log.debug("REST request to delete Raum : {}", id);
        raumService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
