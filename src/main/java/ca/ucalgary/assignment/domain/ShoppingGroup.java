package ca.ucalgary.assignment.domain;

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
 * A ShoppingGroup.
 */
@Entity
@Table(name = "shopping_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShoppingGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "group", "owner", "interestedPersons" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "shoppingGroups", "items", "interests", "subscriptions", "joineds" }, allowSetters = true)
    private Person createdBy;

    @ManyToMany(mappedBy = "subscriptions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "shoppingGroups", "items", "interests", "subscriptions", "joineds" }, allowSetters = true)
    private Set<Person> subscribedPersons = new HashSet<>();

    @ManyToMany(mappedBy = "joineds")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "shoppingGroups", "items", "interests", "subscriptions", "joineds" }, allowSetters = true)
    private Set<Person> joinedPersons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShoppingGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ShoppingGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ShoppingGroup createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setGroup(null));
        }
        if (items != null) {
            items.forEach(i -> i.setGroup(this));
        }
        this.items = items;
    }

    public ShoppingGroup items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public ShoppingGroup addItem(Item item) {
        this.items.add(item);
        item.setGroup(this);
        return this;
    }

    public ShoppingGroup removeItem(Item item) {
        this.items.remove(item);
        item.setGroup(null);
        return this;
    }

    public Person getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Person person) {
        this.createdBy = person;
    }

    public ShoppingGroup createdBy(Person person) {
        this.setCreatedBy(person);
        return this;
    }

    public Set<Person> getSubscribedPersons() {
        return this.subscribedPersons;
    }

    public void setSubscribedPersons(Set<Person> people) {
        if (this.subscribedPersons != null) {
            this.subscribedPersons.forEach(i -> i.removeSubscriptions(this));
        }
        if (people != null) {
            people.forEach(i -> i.addSubscriptions(this));
        }
        this.subscribedPersons = people;
    }

    public ShoppingGroup subscribedPersons(Set<Person> people) {
        this.setSubscribedPersons(people);
        return this;
    }

    public ShoppingGroup addSubscribedPersons(Person person) {
        this.subscribedPersons.add(person);
        person.getSubscriptions().add(this);
        return this;
    }

    public ShoppingGroup removeSubscribedPersons(Person person) {
        this.subscribedPersons.remove(person);
        person.getSubscriptions().remove(this);
        return this;
    }

    public Set<Person> getJoinedPersons() {
        return this.joinedPersons;
    }

    public void setJoinedPersons(Set<Person> people) {
        if (this.joinedPersons != null) {
            this.joinedPersons.forEach(i -> i.removeJoined(this));
        }
        if (people != null) {
            people.forEach(i -> i.addJoined(this));
        }
        this.joinedPersons = people;
    }

    public ShoppingGroup joinedPersons(Set<Person> people) {
        this.setJoinedPersons(people);
        return this;
    }

    public ShoppingGroup addJoinedPersons(Person person) {
        this.joinedPersons.add(person);
        person.getJoineds().add(this);
        return this;
    }

    public ShoppingGroup removeJoinedPersons(Person person) {
        this.joinedPersons.remove(person);
        person.getJoineds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingGroup)) {
            return false;
        }
        return id != null && id.equals(((ShoppingGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
