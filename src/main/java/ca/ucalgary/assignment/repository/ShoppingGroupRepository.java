package ca.ucalgary.assignment.repository;

import ca.ucalgary.assignment.domain.ShoppingGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ShoppingGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingGroupRepository extends JpaRepository<ShoppingGroup, Long> {}
