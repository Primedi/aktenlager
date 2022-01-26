package org.tdvogt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.tdvogt.IntegrationTest;
import org.tdvogt.domain.Raum;
import org.tdvogt.repository.RaumRepository;

/**
 * Integration tests for the {@link RaumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RaumResourceIT {

    private static final String DEFAULT_GEBAEUDE = "AAAAAAAAAA";
    private static final String UPDATED_GEBAEUDE = "BBBBBBBBBB";

    private static final Long DEFAULT_RAUMNUMMER = 1L;
    private static final Long UPDATED_RAUMNUMMER = 2L;

    private static final Long DEFAULT_ETAGE = 1L;
    private static final Long UPDATED_ETAGE = 2L;

    private static final String DEFAULT_ZUSATZ = "AAAAAAAAAA";
    private static final String UPDATED_ZUSATZ = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/raums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RaumRepository raumRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRaumMockMvc;

    private Raum raum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raum createEntity(EntityManager em) {
        Raum raum = new Raum().gebaeude(DEFAULT_GEBAEUDE).raumnummer(DEFAULT_RAUMNUMMER).etage(DEFAULT_ETAGE).zusatz(DEFAULT_ZUSATZ);
        return raum;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raum createUpdatedEntity(EntityManager em) {
        Raum raum = new Raum().gebaeude(UPDATED_GEBAEUDE).raumnummer(UPDATED_RAUMNUMMER).etage(UPDATED_ETAGE).zusatz(UPDATED_ZUSATZ);
        return raum;
    }

    @BeforeEach
    public void initTest() {
        raum = createEntity(em);
    }

    @Test
    @Transactional
    void createRaum() throws Exception {
        int databaseSizeBeforeCreate = raumRepository.findAll().size();
        // Create the Raum
        restRaumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raum)))
            .andExpect(status().isCreated());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeCreate + 1);
        Raum testRaum = raumList.get(raumList.size() - 1);
        assertThat(testRaum.getGebaeude()).isEqualTo(DEFAULT_GEBAEUDE);
        assertThat(testRaum.getRaumnummer()).isEqualTo(DEFAULT_RAUMNUMMER);
        assertThat(testRaum.getEtage()).isEqualTo(DEFAULT_ETAGE);
        assertThat(testRaum.getZusatz()).isEqualTo(DEFAULT_ZUSATZ);
    }

    @Test
    @Transactional
    void createRaumWithExistingId() throws Exception {
        // Create the Raum with an existing ID
        raum.setId(1L);

        int databaseSizeBeforeCreate = raumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raum)))
            .andExpect(status().isBadRequest());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRaums() throws Exception {
        // Initialize the database
        raumRepository.saveAndFlush(raum);

        // Get all the raumList
        restRaumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raum.getId().intValue())))
            .andExpect(jsonPath("$.[*].gebaeude").value(hasItem(DEFAULT_GEBAEUDE)))
            .andExpect(jsonPath("$.[*].raumnummer").value(hasItem(DEFAULT_RAUMNUMMER.intValue())))
            .andExpect(jsonPath("$.[*].etage").value(hasItem(DEFAULT_ETAGE.intValue())))
            .andExpect(jsonPath("$.[*].zusatz").value(hasItem(DEFAULT_ZUSATZ)));
    }

    @Test
    @Transactional
    void getRaum() throws Exception {
        // Initialize the database
        raumRepository.saveAndFlush(raum);

        // Get the raum
        restRaumMockMvc
            .perform(get(ENTITY_API_URL_ID, raum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(raum.getId().intValue()))
            .andExpect(jsonPath("$.gebaeude").value(DEFAULT_GEBAEUDE))
            .andExpect(jsonPath("$.raumnummer").value(DEFAULT_RAUMNUMMER.intValue()))
            .andExpect(jsonPath("$.etage").value(DEFAULT_ETAGE.intValue()))
            .andExpect(jsonPath("$.zusatz").value(DEFAULT_ZUSATZ));
    }

    @Test
    @Transactional
    void getNonExistingRaum() throws Exception {
        // Get the raum
        restRaumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRaum() throws Exception {
        // Initialize the database
        raumRepository.saveAndFlush(raum);

        int databaseSizeBeforeUpdate = raumRepository.findAll().size();

        // Update the raum
        Raum updatedRaum = raumRepository.findById(raum.getId()).get();
        // Disconnect from session so that the updates on updatedRaum are not directly saved in db
        em.detach(updatedRaum);
        updatedRaum.gebaeude(UPDATED_GEBAEUDE).raumnummer(UPDATED_RAUMNUMMER).etage(UPDATED_ETAGE).zusatz(UPDATED_ZUSATZ);

        restRaumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRaum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRaum))
            )
            .andExpect(status().isOk());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
        Raum testRaum = raumList.get(raumList.size() - 1);
        assertThat(testRaum.getGebaeude()).isEqualTo(UPDATED_GEBAEUDE);
        assertThat(testRaum.getRaumnummer()).isEqualTo(UPDATED_RAUMNUMMER);
        assertThat(testRaum.getEtage()).isEqualTo(UPDATED_ETAGE);
        assertThat(testRaum.getZusatz()).isEqualTo(UPDATED_ZUSATZ);
    }

    @Test
    @Transactional
    void putNonExistingRaum() throws Exception {
        int databaseSizeBeforeUpdate = raumRepository.findAll().size();
        raum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, raum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(raum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRaum() throws Exception {
        int databaseSizeBeforeUpdate = raumRepository.findAll().size();
        raum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(raum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRaum() throws Exception {
        int databaseSizeBeforeUpdate = raumRepository.findAll().size();
        raum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRaumWithPatch() throws Exception {
        // Initialize the database
        raumRepository.saveAndFlush(raum);

        int databaseSizeBeforeUpdate = raumRepository.findAll().size();

        // Update the raum using partial update
        Raum partialUpdatedRaum = new Raum();
        partialUpdatedRaum.setId(raum.getId());

        partialUpdatedRaum.raumnummer(UPDATED_RAUMNUMMER).zusatz(UPDATED_ZUSATZ);

        restRaumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRaum))
            )
            .andExpect(status().isOk());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
        Raum testRaum = raumList.get(raumList.size() - 1);
        assertThat(testRaum.getGebaeude()).isEqualTo(DEFAULT_GEBAEUDE);
        assertThat(testRaum.getRaumnummer()).isEqualTo(UPDATED_RAUMNUMMER);
        assertThat(testRaum.getEtage()).isEqualTo(DEFAULT_ETAGE);
        assertThat(testRaum.getZusatz()).isEqualTo(UPDATED_ZUSATZ);
    }

    @Test
    @Transactional
    void fullUpdateRaumWithPatch() throws Exception {
        // Initialize the database
        raumRepository.saveAndFlush(raum);

        int databaseSizeBeforeUpdate = raumRepository.findAll().size();

        // Update the raum using partial update
        Raum partialUpdatedRaum = new Raum();
        partialUpdatedRaum.setId(raum.getId());

        partialUpdatedRaum.gebaeude(UPDATED_GEBAEUDE).raumnummer(UPDATED_RAUMNUMMER).etage(UPDATED_ETAGE).zusatz(UPDATED_ZUSATZ);

        restRaumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRaum))
            )
            .andExpect(status().isOk());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
        Raum testRaum = raumList.get(raumList.size() - 1);
        assertThat(testRaum.getGebaeude()).isEqualTo(UPDATED_GEBAEUDE);
        assertThat(testRaum.getRaumnummer()).isEqualTo(UPDATED_RAUMNUMMER);
        assertThat(testRaum.getEtage()).isEqualTo(UPDATED_ETAGE);
        assertThat(testRaum.getZusatz()).isEqualTo(UPDATED_ZUSATZ);
    }

    @Test
    @Transactional
    void patchNonExistingRaum() throws Exception {
        int databaseSizeBeforeUpdate = raumRepository.findAll().size();
        raum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, raum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(raum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRaum() throws Exception {
        int databaseSizeBeforeUpdate = raumRepository.findAll().size();
        raum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(raum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRaum() throws Exception {
        int databaseSizeBeforeUpdate = raumRepository.findAll().size();
        raum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(raum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Raum in the database
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRaum() throws Exception {
        // Initialize the database
        raumRepository.saveAndFlush(raum);

        int databaseSizeBeforeDelete = raumRepository.findAll().size();

        // Delete the raum
        restRaumMockMvc
            .perform(delete(ENTITY_API_URL_ID, raum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Raum> raumList = raumRepository.findAll();
        assertThat(raumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
