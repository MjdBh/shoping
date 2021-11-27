package ca.ucalgary.assignment.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.ucalgary.assignment.IntegrationTest;
import ca.ucalgary.assignment.domain.ShoppingGroup;
import ca.ucalgary.assignment.repository.ShoppingGroupRepository;
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
 * Integration tests for the {@link ShoppingGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShoppingGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shopping-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShoppingGroupRepository shoppingGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingGroupMockMvc;

    private ShoppingGroup shoppingGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingGroup createEntity(EntityManager em) {
        ShoppingGroup shoppingGroup = new ShoppingGroup().name(DEFAULT_NAME).createdAt(DEFAULT_CREATED_AT);
        return shoppingGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingGroup createUpdatedEntity(EntityManager em) {
        ShoppingGroup shoppingGroup = new ShoppingGroup().name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);
        return shoppingGroup;
    }

    @BeforeEach
    public void initTest() {
        shoppingGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createShoppingGroup() throws Exception {
        int databaseSizeBeforeCreate = shoppingGroupRepository.findAll().size();
        // Create the ShoppingGroup
        restShoppingGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shoppingGroup)))
            .andExpect(status().isCreated());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ShoppingGroup testShoppingGroup = shoppingGroupList.get(shoppingGroupList.size() - 1);
        assertThat(testShoppingGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShoppingGroup.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createShoppingGroupWithExistingId() throws Exception {
        // Create the ShoppingGroup with an existing ID
        shoppingGroup.setId(1L);

        int databaseSizeBeforeCreate = shoppingGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shoppingGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingGroupRepository.findAll().size();
        // set the field null
        shoppingGroup.setName(null);

        // Create the ShoppingGroup, which fails.

        restShoppingGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shoppingGroup)))
            .andExpect(status().isBadRequest());

        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingGroupRepository.findAll().size();
        // set the field null
        shoppingGroup.setCreatedAt(null);

        // Create the ShoppingGroup, which fails.

        restShoppingGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shoppingGroup)))
            .andExpect(status().isBadRequest());

        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShoppingGroups() throws Exception {
        // Initialize the database
        shoppingGroupRepository.saveAndFlush(shoppingGroup);

        // Get all the shoppingGroupList
        restShoppingGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getShoppingGroup() throws Exception {
        // Initialize the database
        shoppingGroupRepository.saveAndFlush(shoppingGroup);

        // Get the shoppingGroup
        restShoppingGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, shoppingGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingShoppingGroup() throws Exception {
        // Get the shoppingGroup
        restShoppingGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShoppingGroup() throws Exception {
        // Initialize the database
        shoppingGroupRepository.saveAndFlush(shoppingGroup);

        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();

        // Update the shoppingGroup
        ShoppingGroup updatedShoppingGroup = shoppingGroupRepository.findById(shoppingGroup.getId()).get();
        // Disconnect from session so that the updates on updatedShoppingGroup are not directly saved in db
        em.detach(updatedShoppingGroup);
        updatedShoppingGroup.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);

        restShoppingGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShoppingGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShoppingGroup))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
        ShoppingGroup testShoppingGroup = shoppingGroupList.get(shoppingGroupList.size() - 1);
        assertThat(testShoppingGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShoppingGroup.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingShoppingGroup() throws Exception {
        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();
        shoppingGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shoppingGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShoppingGroup() throws Exception {
        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();
        shoppingGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shoppingGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShoppingGroup() throws Exception {
        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();
        shoppingGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shoppingGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShoppingGroupWithPatch() throws Exception {
        // Initialize the database
        shoppingGroupRepository.saveAndFlush(shoppingGroup);

        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();

        // Update the shoppingGroup using partial update
        ShoppingGroup partialUpdatedShoppingGroup = new ShoppingGroup();
        partialUpdatedShoppingGroup.setId(shoppingGroup.getId());

        partialUpdatedShoppingGroup.name(UPDATED_NAME);

        restShoppingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShoppingGroup))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
        ShoppingGroup testShoppingGroup = shoppingGroupList.get(shoppingGroupList.size() - 1);
        assertThat(testShoppingGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShoppingGroup.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateShoppingGroupWithPatch() throws Exception {
        // Initialize the database
        shoppingGroupRepository.saveAndFlush(shoppingGroup);

        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();

        // Update the shoppingGroup using partial update
        ShoppingGroup partialUpdatedShoppingGroup = new ShoppingGroup();
        partialUpdatedShoppingGroup.setId(shoppingGroup.getId());

        partialUpdatedShoppingGroup.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);

        restShoppingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShoppingGroup))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
        ShoppingGroup testShoppingGroup = shoppingGroupList.get(shoppingGroupList.size() - 1);
        assertThat(testShoppingGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShoppingGroup.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingShoppingGroup() throws Exception {
        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();
        shoppingGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shoppingGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shoppingGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShoppingGroup() throws Exception {
        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();
        shoppingGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shoppingGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShoppingGroup() throws Exception {
        int databaseSizeBeforeUpdate = shoppingGroupRepository.findAll().size();
        shoppingGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shoppingGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingGroup in the database
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShoppingGroup() throws Exception {
        // Initialize the database
        shoppingGroupRepository.saveAndFlush(shoppingGroup);

        int databaseSizeBeforeDelete = shoppingGroupRepository.findAll().size();

        // Delete the shoppingGroup
        restShoppingGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, shoppingGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShoppingGroup> shoppingGroupList = shoppingGroupRepository.findAll();
        assertThat(shoppingGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
