package ca.ucalgary.assignment.service;

import ca.ucalgary.assignment.domain.Need;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Need}.
 */
public interface NeedService {
    /**
     * Save a need.
     *
     * @param need the entity to save.
     * @return the persisted entity.
     */
    Need save(Need need);

    /**
     * Partially updates a need.
     *
     * @param need the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Need> partialUpdate(Need need);

    /**
     * Get all the needs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Need> findAll(Pageable pageable);

    /**
     * Get the "id" need.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Need> findOne(Long id);

    /**
     * Delete the "id" need.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
