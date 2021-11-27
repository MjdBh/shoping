package ca.ucalgary.assignment.domain;

import ca.ucalgary.assignment.domain.enumeration.ItemState;
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
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private Integer price;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "picture")
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ItemState state;

    @ManyToOne
    @JsonIgnoreProperties(value = { "items", "createdBy", "subscribedPersons", "joinedPersons" }, allowSetters = true)
    private ShoppingGroup group;

    @ManyToOne
    @JsonIgnoreProperties(value = { "shoppingGroups", "items", "interests", "subscriptions", "joineds" }, allowSetters = true)
    private Person owner;

    @ManyToMany(mappedBy = "interests")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "shoppingGroups", "items", "interests", "subscriptions", "joineds" }, allowSetters = true)
    private Set<Person> interestedPersons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Item name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return this.price;
    }

    public Item price(Integer price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Item createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPicture() {
        return this.picture;
    }

    public Item picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ItemState getState() {
        return this.state;
    }

    public Item state(ItemState state) {
        this.setState(state);
        return this;
    }

    public void setState(ItemState state) {
        this.state = state;
    }

    public ShoppingGroup getGroup() {
        return this.group;
    }

    public void setGroup(ShoppingGroup shoppingGroup) {
        this.group = shoppingGroup;
    }

    public Item group(ShoppingGroup shoppingGroup) {
        this.setGroup(shoppingGroup);
        return this;
    }

    public Person getOwner() {
        return this.owner;
    }

    public void setOwner(Person person) {
        this.owner = person;
    }

    public Item owner(Person person) {
        this.setOwner(person);
        return this;
    }

    public Set<Person> getInterestedPersons() {
        return this.interestedPersons;
    }

    public void setInterestedPersons(Set<Person> people) {
        if (this.interestedPersons != null) {
            this.interestedPersons.forEach(i -> i.removeInterests(this));
        }
        if (people != null) {
            people.forEach(i -> i.addInterests(this));
        }
        this.interestedPersons = people;
    }

    public Item interestedPersons(Set<Person> people) {
        this.setInterestedPersons(people);
        return this;
    }

    public Item addInterestedPersons(Person person) {
        this.interestedPersons.add(person);
        person.getInterests().add(this);
        return this;
    }

    public Item removeInterestedPersons(Person person) {
        this.interestedPersons.remove(person);
        person.getInterests().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", picture='" + getPicture() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
