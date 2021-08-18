package hellojpa;

import jdk.nashorn.internal.objects.annotations.Getter;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {
    @Id
    @GeneratedValue
    private Long id;
//    @ManyToOne
    private Address address;

    public AddressEntity(String city, String street, String zipcode) {
        this.address = new Address(city, street, zipcode);
    }

    public AddressEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public AddressEntity(Long id, Address address) {
        this.id = id;
        this.address = address;
    }
}
