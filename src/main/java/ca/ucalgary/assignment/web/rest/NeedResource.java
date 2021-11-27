package ca.ucalgary.assignment.web.rest;

import ca.ucalgary.assignment.domain.Need;
import ca.ucalgary.assignment.repository.NeedRepository;
import ca.ucalgary.assignment.service.NeedService;
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
 * REST controller for managing {@link ca.ucalgary.assignment.domain.Need}.
 */
@RestController
@RequestMapping("/api")
public class NeedResource {

    private final Logger log = LoggerFactory.getLogger(NeedResource.class);

    private static final String ENTITY_NAME = "need";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NeedService needService;

    private final NeedRepository needRepository;

    public NeedResource(NeedService needService, NeedRepository needRepository) {
        this.needService = needService;
        this.needRepository = needRepository;
    }

    /**
     * {@code POST  /needs} : Create a new need.
     *
     * @param need the need to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new need, or with status {@code 400 (Bad Request)} if the need has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/needs")
    public ResponseEntity<Need> createNeed(@Valid @RequestBody Need need) throws URISyntaxException {
        log.debug("REST request to save Need : {}", need);
        if (need.getId() != null) {
            throw new BadRequestAlertException("A new need cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Need result = needService.save(need);
        return ResponseEntity
            .created(new URI("/api/needs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /needs/:id} : Updates an existing need.
     *
     * @param id the id of the need to save.
     * @param need the need to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated need,
     * or with status {@code 400 (Bad Request)} if the need is not valid,
     * or with status {@code 500 (Internal Server Error)} if the need couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/needs/{id}")
    public ResponseEntity<Need> updateNeed(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Need need)
        throws URISyntaxException {
        log.debug("REST request to update Need : {}, {}", id, need);
        if (need.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, need.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!needRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Need result = needService.save(need);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, need.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /needs/:id} : Partial updates given fields of an existing need, field will ignore if it is null
     *
     * @param id the id of the need to save.
     * @param need the need to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated need,
     * or with status {@code 400 (Bad Request)} if the need is not valid,
     * or with status {@code 404 (Not Found)} if the need is not found,
     * or with status {@code 500 (Internal Server Error)} if the need couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/needs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Need> partialUpdateNeed(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Need need
    ) throws URISyntaxException {
        log.debug("REST request to partial update Need partially : {}, {}", id, need);
        if (need.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, need.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!needRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Need> result = needService.partialUpdate(need);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, need.getId().toString())
        );
    }

    /**
     * {@code GET  /needs} : get all the needs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of needs in body.
     */
    @GetMapping("/needs")
    public ResponseEntity<List<Need>> getAllNeeds(Pageable pageable) {
        log.debug("REST request to get a page of Needs");
        Page<Need> page = needService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /needs/:id} : get the "id" need.
     *
     * @param id the id of the need to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the need, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/needs/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable Long id) {
        log.debug("REST request to get Need : {}", id);
        Optional<Need> need = needService.findOne(id);
        return ResponseUtil.wrapOrNotFound(need);
    }

    /**
     * {@code DELETE  /needs/:id} : delete the "id" need.
     *
     * @param id the id of the need to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/needs/{id}")
    public ResponseEntity<Void> deleteNeed(@PathVariable Long id) {
        log.debug("REST request to delete Need : {}", id);
        needService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
