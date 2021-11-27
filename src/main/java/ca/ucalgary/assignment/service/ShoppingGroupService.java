package ca.ucalgary.assignment.service;

import ca.ucalgary.assignment.domain.ShoppingGroup;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ShoppingGroup}.
 */
public interface ShoppingGroupService {
    /**
     * Save a shoppingGroup.
     *
     * @param shoppingGroup the entity to save.
     * @return the persisted entity.
     */
    ShoppingGroup save(ShoppingGroup shoppingGroup);

    /**
     * Partially updates a shoppingGroup.
     *
     * @param shoppingGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShoppingGroup> partialUpdate(ShoppingGroup shoppingGroup);

    /**
     * Get all the shoppingGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShoppingGroup> findAll(Pageable pageable);

    /**
     * Get the "id" shoppingGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShoppingGroup> findOne(Long id);

    /**
     * Delete the "id" shoppingGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
