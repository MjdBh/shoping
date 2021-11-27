package ca.ucalgary.assignment.domain;

import ca.ucalgary.assignment.domain.enumeration.PersonRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private PersonRole role;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "items", "createdBy", "subscribedPersons", "joinedPersons" }, allowSetters = true)
    private Set<ShoppingGroup> shoppingGroups = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "group", "owner", "interestedPersons" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_person__interests",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "interests_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "group", "owner", "interestedPersons" }, allowSetters = true)
    private Set<Item> interests = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_person__subscriptions",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "subscriptions_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "items", "createdBy", "subscribedPersons", "joinedPersons" }, allowSetters = true)
    private Set<ShoppingGroup> subscriptions = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_person__joined",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "joined_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "items", "createdBy", "subscribedPersons", "joinedPersons" }, allowSetters = true)
    private Set<ShoppingGroup> joineds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public Person username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public Person name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonRole getRole() {
        return this.role;
    }

    public Person role(PersonRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(PersonRole role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Person createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ShoppingGroup> getShoppingGroups() {
        return this.shoppingGroups;
    }

    public void setShoppingGroups(Set<ShoppingGroup> shoppingGroups) {
        if (this.shoppingGroups != null) {
            this.shoppingGroups.forEach(i -> i.setCreatedBy(null));
        }
        if (shoppingGroups != null) {
            shoppingGroups.forEach(i -> i.setCreatedBy(this));
        }
        this.shoppingGroups = shoppingGroups;
    }

    public Person shoppingGroups(Set<ShoppingGroup> shoppingGroups) {
        this.setShoppingGroups(shoppingGroups);
        return this;
    }

    public Person addShoppingGroup(ShoppingGroup shoppingGroup) {
        this.shoppingGroups.add(shoppingGroup);
        shoppingGroup.setCreatedBy(this);
        return this;
    }

    public Person removeShoppingGroup(ShoppingGroup shoppingGroup) {
        this.shoppingGroups.remove(shoppingGroup);
        shoppingGroup.setCreatedBy(null);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setOwner(null));
        }
        if (items != null) {
            items.forEach(i -> i.setOwner(this));
        }
        this.items = items;
    }

    public Person items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Person addItem(Item item) {
        this.items.add(item);
        item.setOwner(this);
        return this;
    }

    public Person removeItem(Item item) {
        this.items.remove(item);
        item.setOwner(null);
        return this;
    }

    public Set<Item> getInterests() {
        return this.interests;
    }

    public void setInterests(Set<Item> items) {
        this.interests = items;
    }

    public Person interests(Set<Item> items) {
        this.setInterests(items);
        return this;
    }

    public Person addInterests(Item item) {
        this.interests.add(item);
        item.getInterestedPersons().add(this);
        return this;
    }

    public Person removeInterests(Item item) {
        this.interests.remove(item);
        item.getInterestedPersons().remove(this);
        return this;
    }

    public Set<ShoppingGroup> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(Set<ShoppingGroup> shoppingGroups) {
        this.subscriptions = shoppingGroups;
    }

    public Person subscriptions(Set<ShoppingGroup> shoppingGroups) {
        this.setSubscriptions(shoppingGroups);
        return this;
    }

    public Person addSubscriptions(ShoppingGroup shoppingGroup) {
        this.subscriptions.add(shoppingGroup);
        shoppingGroup.getSubscribedPersons().add(this);
        return this;
    }

    public Person removeSubscriptions(ShoppingGroup shoppingGroup) {
        this.subscriptions.remove(shoppingGroup);
        shoppingGroup.getSubscribedPersons().remove(this);
        return this;
    }

    public Set<ShoppingGroup> getJoineds() {
        return this.joineds;
    }

    public void setJoineds(Set<ShoppingGroup> shoppingGroups) {
        this.joineds = shoppingGroups;
    }

    public Person joineds(Set<ShoppingGroup> shoppingGroups) {
        this.setJoineds(shoppingGroups);
        return this;
    }

    public Person addJoined(ShoppingGroup shoppingGroup) {
        this.joineds.add(shoppingGroup);
        shoppingGroup.getJoinedPersons().add(this);
        return this;
    }

    public Person removeJoined(ShoppingGroup shoppingGroup) {
        this.joineds.remove(shoppingGroup);
        shoppingGroup.getJoinedPersons().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", name='" + getName() + "'" +
            ", role='" + getRole() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
