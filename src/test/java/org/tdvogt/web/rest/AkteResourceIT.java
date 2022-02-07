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
import org.tdvogt.domain.Akte;
import org.tdvogt.repository.AkteRepository;

/**
 * Integration tests for the {@link AkteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AkteResourceIT {

    private static final String DEFAULT_AKTENTHEMA = "AAAAAAAAAA";
    private static final String UPDATED_AKTENTHEMA = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATIONS_EINHEIT = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATIONS_EINHEIT = "BBBBBBBBBB";

    private static final Long DEFAULT_AKTEN_METER = 1L;
    private static final Long UPDATED_AKTEN_METER = 2L;

    private static final Boolean DEFAULT_HAENGEND = false;
    private static final Boolean UPDATED_HAENGEND = true;

    private static final String DEFAULT_STANDORT = "AAAAAAAAAA";
    private static final String UPDATED_STANDORT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aktes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AkteRepository akteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAkteMockMvc;

    private Akte akte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Akte createEntity(EntityManager em) {
        Akte akte = new Akte()
            .aktenthema(DEFAULT_AKTENTHEMA)
            .organisationsEinheit(DEFAULT_ORGANISATIONS_EINHEIT)
            .aktenMeter(DEFAULT_AKTEN_METER)
            .haengend(DEFAULT_HAENGEND)
            .standort(DEFAULT_STANDORT);
        return akte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Akte createUpdatedEntity(EntityManager em) {
        Akte akte = new Akte()
            .aktenthema(UPDATED_AKTENTHEMA)
            .organisationsEinheit(UPDATED_ORGANISATIONS_EINHEIT)
            .aktenMeter(UPDATED_AKTEN_METER)
            .haengend(UPDATED_HAENGEND)
            .standort(UPDATED_STANDORT);
        return akte;
    }

    @BeforeEach
    public void initTest() {
        akte = createEntity(em);
    }

    @Test
    @Transactional
    void createAkte() throws Exception {
        int databaseSizeBeforeCreate = akteRepository.findAll().size();
        // Create the Akte
        restAkteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(akte)))
            .andExpect(status().isCreated());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeCreate + 1);
        Akte testAkte = akteList.get(akteList.size() - 1);
        assertThat(testAkte.getAktenthema()).isEqualTo(DEFAULT_AKTENTHEMA);
        assertThat(testAkte.getOrganisationsEinheit()).isEqualTo(DEFAULT_ORGANISATIONS_EINHEIT);
        assertThat(testAkte.getAktenMeter()).isEqualTo(DEFAULT_AKTEN_METER);
        assertThat(testAkte.getHaengend()).isEqualTo(DEFAULT_HAENGEND);
        assertThat(testAkte.getStandort()).isEqualTo("");
    }

    @Test
    @Transactional
    void createAkteWithExistingId() throws Exception {
        // Create the Akte with an existing ID
        akte.setId(1L);

        int databaseSizeBeforeCreate = akteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAkteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(akte)))
            .andExpect(status().isBadRequest());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAktes() throws Exception {
        // Initialize the database
        akteRepository.saveAndFlush(akte);

        // Get all the akteList
        restAkteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(akte.getId().intValue())))
            .andExpect(jsonPath("$.[*].aktenthema").value(hasItem(DEFAULT_AKTENTHEMA)))
            .andExpect(jsonPath("$.[*].organisationsEinheit").value(hasItem(DEFAULT_ORGANISATIONS_EINHEIT)))
            .andExpect(jsonPath("$.[*].aktenMeter").value(hasItem(DEFAULT_AKTEN_METER.intValue())))
            .andExpect(jsonPath("$.[*].haengend").value(hasItem(DEFAULT_HAENGEND.booleanValue())))
            .andExpect(jsonPath("$.[*].standort").value(hasItem("")));
    }

    @Test
    @Transactional
    void getAkte() throws Exception {
        // Initialize the database
        akteRepository.saveAndFlush(akte);

        // Get the akte
        restAkteMockMvc
            .perform(get(ENTITY_API_URL_ID, akte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(akte.getId().intValue()))
            .andExpect(jsonPath("$.aktenthema").value(DEFAULT_AKTENTHEMA))
            .andExpect(jsonPath("$.organisationsEinheit").value(DEFAULT_ORGANISATIONS_EINHEIT))
            .andExpect(jsonPath("$.aktenMeter").value(DEFAULT_AKTEN_METER.intValue()))
            .andExpect(jsonPath("$.haengend").value(DEFAULT_HAENGEND.booleanValue()))
            .andExpect(jsonPath("$.standort").value(""));
    }

    @Test
    @Transactional
    void getNonExistingAkte() throws Exception {
        // Get the akte
        restAkteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAkte() throws Exception {
        // Initialize the database
        akteRepository.saveAndFlush(akte);

        int databaseSizeBeforeUpdate = akteRepository.findAll().size();

        // Update the akte
        Akte updatedAkte = akteRepository.findById(akte.getId()).get();
        // Disconnect from session so that the updates on updatedAkte are not directly saved in db
        em.detach(updatedAkte);
        updatedAkte
            .aktenthema(UPDATED_AKTENTHEMA)
            .organisationsEinheit(UPDATED_ORGANISATIONS_EINHEIT)
            .aktenMeter(UPDATED_AKTEN_METER)
            .haengend(UPDATED_HAENGEND)
            .standort(UPDATED_STANDORT);

        restAkteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAkte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAkte))
            )
            .andExpect(status().isOk());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
        Akte testAkte = akteList.get(akteList.size() - 1);
        assertThat(testAkte.getAktenthema()).isEqualTo(UPDATED_AKTENTHEMA);
        assertThat(testAkte.getOrganisationsEinheit()).isEqualTo(UPDATED_ORGANISATIONS_EINHEIT);
        assertThat(testAkte.getAktenMeter()).isEqualTo(UPDATED_AKTEN_METER);
        assertThat(testAkte.getHaengend()).isEqualTo(UPDATED_HAENGEND);
        assertThat(testAkte.getStandort()).isEqualTo("");
    }

    @Test
    @Transactional
    void putNonExistingAkte() throws Exception {
        int databaseSizeBeforeUpdate = akteRepository.findAll().size();
        akte.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAkteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, akte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(akte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAkte() throws Exception {
        int databaseSizeBeforeUpdate = akteRepository.findAll().size();
        akte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAkteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(akte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAkte() throws Exception {
        int databaseSizeBeforeUpdate = akteRepository.findAll().size();
        akte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAkteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(akte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAkteWithPatch() throws Exception {
        // Initialize the database
        akteRepository.saveAndFlush(akte);

        int databaseSizeBeforeUpdate = akteRepository.findAll().size();

        // Update the akte using partial update
        Akte partialUpdatedAkte = new Akte();
        partialUpdatedAkte.setId(akte.getId());

        partialUpdatedAkte.organisationsEinheit(UPDATED_ORGANISATIONS_EINHEIT);

        restAkteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAkte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAkte))
            )
            .andExpect(status().isOk());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
        Akte testAkte = akteList.get(akteList.size() - 1);
        assertThat(testAkte.getAktenthema()).isEqualTo(DEFAULT_AKTENTHEMA);
        assertThat(testAkte.getOrganisationsEinheit()).isEqualTo(UPDATED_ORGANISATIONS_EINHEIT);
        assertThat(testAkte.getAktenMeter()).isEqualTo(DEFAULT_AKTEN_METER);
        assertThat(testAkte.getHaengend()).isEqualTo(DEFAULT_HAENGEND);
        assertThat(testAkte.getStandort()).isEqualTo("");
    }

    @Test
    @Transactional
    void fullUpdateAkteWithPatch() throws Exception {
        // Initialize the database
        akteRepository.saveAndFlush(akte);

        int databaseSizeBeforeUpdate = akteRepository.findAll().size();

        // Update the akte using partial update
        Akte partialUpdatedAkte = new Akte();
        partialUpdatedAkte.setId(akte.getId());

        partialUpdatedAkte
            .aktenthema(UPDATED_AKTENTHEMA)
            .organisationsEinheit(UPDATED_ORGANISATIONS_EINHEIT)
            .aktenMeter(UPDATED_AKTEN_METER)
            .haengend(UPDATED_HAENGEND)
            .standort(UPDATED_STANDORT);

        restAkteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAkte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAkte))
            )
            .andExpect(status().isOk());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
        Akte testAkte = akteList.get(akteList.size() - 1);
        assertThat(testAkte.getAktenthema()).isEqualTo(UPDATED_AKTENTHEMA);
        assertThat(testAkte.getOrganisationsEinheit()).isEqualTo(UPDATED_ORGANISATIONS_EINHEIT);
        assertThat(testAkte.getAktenMeter()).isEqualTo(UPDATED_AKTEN_METER);
        assertThat(testAkte.getHaengend()).isEqualTo(UPDATED_HAENGEND);
        assertThat(testAkte.getStandort()).isEqualTo("");
    }

    @Test
    @Transactional
    void patchNonExistingAkte() throws Exception {
        int databaseSizeBeforeUpdate = akteRepository.findAll().size();
        akte.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAkteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, akte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(akte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAkte() throws Exception {
        int databaseSizeBeforeUpdate = akteRepository.findAll().size();
        akte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAkteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(akte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAkte() throws Exception {
        int databaseSizeBeforeUpdate = akteRepository.findAll().size();
        akte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAkteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(akte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Akte in the database
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAkte() throws Exception {
        // Initialize the database
        akteRepository.saveAndFlush(akte);

        int databaseSizeBeforeDelete = akteRepository.findAll().size();

        // Delete the akte
        restAkteMockMvc
            .perform(delete(ENTITY_API_URL_ID, akte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Akte> akteList = akteRepository.findAll();
        assertThat(akteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
