package ca.ucalgary.assignment.web.rest;

import ca.ucalgary.assignment.domain.ShoppingGroup;
import ca.ucalgary.assignment.repository.ShoppingGroupRepository;
import ca.ucalgary.assignment.service.ShoppingGroupService;
import ca.ucalgary.assignment.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ca.ucalgary.assignment.domain.ShoppingGroup}.
 */
@RestController
@RequestMapping("/api")
public class ShoppingGroupResource {

    private final Logger log = LoggerFactory.getLogger(ShoppingGroupResource.class);

    private static final String ENTITY_NAME = "shoppingGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShoppingGroupService shoppingGroupService;

    private final ShoppingGroupRepository shoppingGroupRepository;

    public ShoppingGroupResource(ShoppingGroupService shoppingGroupService, ShoppingGroupRepository shoppingGroupRepository) {
        this.shoppingGroupService = shoppingGroupService;
        this.shoppingGroupRepository = shoppingGroupRepository;
    }

    /**
     * {@code POST  /shopping-groups} : Create a new shoppingGroup.
     *
     * @param shoppingGroup the shoppingGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shoppingGroup, or with status {@code 400 (Bad Request)} if the shoppingGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shopping-groups")
    public ResponseEntity<ShoppingGroup> createShoppingGroup(@Valid @RequestBody ShoppingGroup shoppingGroup) throws URISyntaxException {
        log.debug("REST request to save ShoppingGroup : {}", shoppingGroup);
        if (shoppingGroup.getId() != null) {
            throw new BadRequestAlertException("A new shoppingGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShoppingGroup result = shoppingGroupService.save(shoppingGroup);
        return ResponseEntity
            .created(new URI("/api/shopping-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shopping-groups/:id} : Updates an existing shoppingGroup.
     *
     * @param id the id of the shoppingGroup to save.
     * @param shoppingGroup the shoppingGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingGroup,
     * or with status {@code 400 (Bad Request)} if the shoppingGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shopping-groups/{id}")
    public ResponseEntity<ShoppingGroup> updateShoppingGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShoppingGroup shoppingGroup
    ) throws URISyntaxException {
        log.debug("REST request to update ShoppingGroup : {}, {}", id, shoppingGroup);
        if (shoppingGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShoppingGroup result = shoppingGroupService.save(shoppingGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shopping-groups/:id} : Partial updates given fields of an existing shoppingGroup, field will ignore if it is null
     *
     * @param id the id of the shoppingGroup to save.
     * @param shoppingGroup the shoppingGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingGroup,
     * or with status {@code 400 (Bad Request)} if the shoppingGroup is not valid,
     * or with status {@code 404 (Not Found)} if the shoppingGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the shoppingGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shopping-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShoppingGroup> partialUpdateShoppingGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShoppingGroup shoppingGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShoppingGroup partially : {}, {}", id, shoppingGroup);
        if (shoppingGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShoppingGroup> result = shoppingGroupService.partialUpdate(shoppingGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /shopping-groups} : get all the shoppingGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shoppingGroups in body.
     */
    @GetMapping("/shopping-groups")
    public ResponseEntity<List<ShoppingGroup>> getAllShoppingGroups(Pageable pageable) {
        log.debug("REST request to get a page of ShoppingGroups");
        Page<ShoppingGroup> page = shoppingGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shopping-groups/:id} : get the "id" shoppingGroup.
     *
     * @param id the id of the shoppingGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shopping-groups/{id}")
    public ResponseEntity<ShoppingGroup> getShoppingGroup(@PathVariable Long id) {
        log.debug("REST request to get ShoppingGroup : {}", id);
        Optional<ShoppingGroup> shoppingGroup = shoppingGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shoppingGroup);
    }

    /**
     * {@code DELETE  /shopping-groups/:id} : delete the "id" shoppingGroup.
     *
     * @param id the id of the shoppingGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shopping-groups/{id}")
    public ResponseEntity<Void> deleteShoppingGroup(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingGroup : {}", id);
        shoppingGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
