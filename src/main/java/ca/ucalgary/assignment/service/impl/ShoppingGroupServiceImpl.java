package ca.ucalgary.assignment.service.impl;

import ca.ucalgary.assignment.domain.ShoppingGroup;
import ca.ucalgary.assignment.repository.ShoppingGroupRepository;
import ca.ucalgary.assignment.service.ShoppingGroupService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShoppingGroup}.
 */
@Service
@Transactional
public class ShoppingGroupServiceImpl implements ShoppingGroupService {

    private final Logger log = LoggerFactory.getLogger(ShoppingGroupServiceImpl.class);

    private final ShoppingGroupRepository shoppingGroupRepository;

    public ShoppingGroupServiceImpl(ShoppingGroupRepository shoppingGroupRepository) {
        this.shoppingGroupRepository = shoppingGroupRepository;
    }

    @Override
    public ShoppingGroup save(ShoppingGroup shoppingGroup) {
        log.debug("Request to save ShoppingGroup : {}", shoppingGroup);
        return shoppingGroupRepository.save(shoppingGroup);
    }

    @Override
    public Optional<ShoppingGroup> partialUpdate(ShoppingGroup shoppingGroup) {
        log.debug("Request to partially update ShoppingGroup : {}", shoppingGroup);

        return shoppingGroupRepository
            .findById(shoppingGroup.getId())
            .map(existingShoppingGroup -> {
                if (shoppingGroup.getName() != null) {
                    existingShoppingGroup.setName(shoppingGroup.getName());
                }
                if (shoppingGroup.getCreatedAt() != null) {
                    existingShoppingGroup.setCreatedAt(shoppingGroup.getCreatedAt());
                }

                return existingShoppingGroup;
            })
            .map(shoppingGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShoppingGroup> findAll(Pageable pageable) {
        log.debug("Request to get all ShoppingGroups");
        return shoppingGroupRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingGroup> findOne(Long id) {
        log.debug("Request to get ShoppingGroup : {}", id);
        return shoppingGroupRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShoppingGroup : {}", id);
        shoppingGroupRepository.deleteById(id);
    }
}
