package ca.ucalgary.assignment.service.impl;

import ca.ucalgary.assignment.domain.Need;
import ca.ucalgary.assignment.repository.NeedRepository;
import ca.ucalgary.assignment.service.NeedService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Need}.
 */
@Service
@Transactional
public class NeedServiceImpl implements NeedService {

    private final Logger log = LoggerFactory.getLogger(NeedServiceImpl.class);

    private final NeedRepository needRepository;

    public NeedServiceImpl(NeedRepository needRepository) {
        this.needRepository = needRepository;
    }

    @Override
    public Need save(Need need) {
        log.debug("Request to save Need : {}", need);
        return needRepository.save(need);
    }

    @Override
    public Optional<Need> partialUpdate(Need need) {
        log.debug("Request to partially update Need : {}", need);

        return needRepository
            .findById(need.getId())
            .map(existingNeed -> {
                if (need.getCreatedAt() != null) {
                    existingNeed.setCreatedAt(need.getCreatedAt());
                }
                if (need.getQuantity() != null) {
                    existingNeed.setQuantity(need.getQuantity());
                }
                if (need.getDeadline() != null) {
                    existingNeed.setDeadline(need.getDeadline());
                }

                return existingNeed;
            })
            .map(needRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Need> findAll(Pageable pageable) {
        log.debug("Request to get all Needs");
        return needRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Need> findOne(Long id) {
        log.debug("Request to get Need : {}", id);
        return needRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Need : {}", id);
        needRepository.deleteById(id);
    }
}
