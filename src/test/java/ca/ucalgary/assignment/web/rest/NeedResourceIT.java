package ca.ucalgary.assignment.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.ucalgary.assignment.IntegrationTest;
import ca.ucalgary.assignment.domain.Need;
import ca.ucalgary.assignment.repository.NeedRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

/**
 * Integration tests for the {@link NeedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NeedResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Instant DEFAULT_DEADLINE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEADLINE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/needs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NeedRepository needRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNeedMockMvc;

    private Need need;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Need createEntity(EntityManager em) {
        Need need = new Need().createdAt(DEFAULT_CREATED_AT).quantity(DEFAULT_QUANTITY).deadline(DEFAULT_DEADLINE);
        return need;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Need createUpdatedEntity(EntityManager em) {
        Need need = new Need().createdAt(UPDATED_CREATED_AT).quantity(UPDATED_QUANTITY).deadline(UPDATED_DEADLINE);
        return need;
    }

    @BeforeEach
    public void initTest() {
        need = createEntity(em);
    }

    @Test
    @Transactional
    void createNeed() throws Exception {
        int databaseSizeBeforeCreate = needRepository.findAll().size();
        // Create the Need
        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(need)))
            .andExpect(status().isCreated());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeCreate + 1);
        Need testNeed = needList.get(needList.size() - 1);
        assertThat(testNeed.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testNeed.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testNeed.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
    }

    @Test
    @Transactional
    void createNeedWithExistingId() throws Exception {
        // Create the Need with an existing ID
        need.setId(1L);

        int databaseSizeBeforeCreate = needRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(need)))
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = needRepository.findAll().size();
        // set the field null
        need.setCreatedAt(null);

        // Create the Need, which fails.

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(need)))
            .andExpect(status().isBadRequest());

        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = needRepository.findAll().size();
        // set the field null
        need.setQuantity(null);

        // Create the Need, which fails.

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(need)))
            .andExpect(status().isBadRequest());

        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNeeds() throws Exception {
        // Initialize the database
        needRepository.saveAndFlush(need);

        // Get all the needList
        restNeedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(need.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())));
    }

    @Test
    @Transactional
    void getNeed() throws Exception {
        // Initialize the database
        needRepository.saveAndFlush(need);

        // Get the need
        restNeedMockMvc
            .perform(get(ENTITY_API_URL_ID, need.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(need.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNeed() throws Exception {
        // Get the need
        restNeedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNeed() throws Exception {
        // Initialize the database
        needRepository.saveAndFlush(need);

        int databaseSizeBeforeUpdate = needRepository.findAll().size();

        // Update the need
        Need updatedNeed = needRepository.findById(need.getId()).get();
        // Disconnect from session so that the updates on updatedNeed are not directly saved in db
        em.detach(updatedNeed);
        updatedNeed.createdAt(UPDATED_CREATED_AT).quantity(UPDATED_QUANTITY).deadline(UPDATED_DEADLINE);

        restNeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNeed.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNeed))
            )
            .andExpect(status().isOk());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
        Need testNeed = needList.get(needList.size() - 1);
        assertThat(testNeed.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testNeed.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testNeed.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void putNonExistingNeed() throws Exception {
        int databaseSizeBeforeUpdate = needRepository.findAll().size();
        need.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, need.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(need))
            )
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNeed() throws Exception {
        int databaseSizeBeforeUpdate = needRepository.findAll().size();
        need.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(need))
            )
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNeed() throws Exception {
        int databaseSizeBeforeUpdate = needRepository.findAll().size();
        need.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(need)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNeedWithPatch() throws Exception {
        // Initialize the database
        needRepository.saveAndFlush(need);

        int databaseSizeBeforeUpdate = needRepository.findAll().size();

        // Update the need using partial update
        Need partialUpdatedNeed = new Need();
        partialUpdatedNeed.setId(need.getId());

        partialUpdatedNeed.deadline(UPDATED_DEADLINE);

        restNeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNeed))
            )
            .andExpect(status().isOk());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
        Need testNeed = needList.get(needList.size() - 1);
        assertThat(testNeed.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testNeed.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testNeed.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void fullUpdateNeedWithPatch() throws Exception {
        // Initialize the database
        needRepository.saveAndFlush(need);

        int databaseSizeBeforeUpdate = needRepository.findAll().size();

        // Update the need using partial update
        Need partialUpdatedNeed = new Need();
        partialUpdatedNeed.setId(need.getId());

        partialUpdatedNeed.createdAt(UPDATED_CREATED_AT).quantity(UPDATED_QUANTITY).deadline(UPDATED_DEADLINE);

        restNeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNeed))
            )
            .andExpect(status().isOk());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
        Need testNeed = needList.get(needList.size() - 1);
        assertThat(testNeed.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testNeed.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testNeed.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void patchNonExistingNeed() throws Exception {
        int databaseSizeBeforeUpdate = needRepository.findAll().size();
        need.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, need.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(need))
            )
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNeed() throws Exception {
        int databaseSizeBeforeUpdate = needRepository.findAll().size();
        need.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(need))
            )
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNeed() throws Exception {
        int databaseSizeBeforeUpdate = needRepository.findAll().size();
        need.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(need)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Need in the database
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNeed() throws Exception {
        // Initialize the database
        needRepository.saveAndFlush(need);

        int databaseSizeBeforeDelete = needRepository.findAll().size();

        // Delete the need
        restNeedMockMvc
            .perform(delete(ENTITY_API_URL_ID, need.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Need> needList = needRepository.findAll();
        assertThat(needList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
