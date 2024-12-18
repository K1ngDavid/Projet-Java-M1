package entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Client", schema = "LeTresBonCoin", catalog = "")
public class ClientEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idClient")
    private int idClient;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommandEntity> commands;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "postalAddress")
    private String postalAddress;
    @Basic
    @Column(name = "creditCardNumber")
    private String creditCardNumber;
    @Basic
    @Column(name = "cveNumber")
    private String cveNumber;

    @OneToMany
    @JoinTable(name = "Vehicle")
    Set<VehicleEntity> vehicleEntitySet;

    public int getIdClient() {
        return idClient;
    }

    // Getters and Setters
    public List<CommandEntity> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandEntity> commands) {
        this.commands = commands;
    }


    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCveNumber() {
        return cveNumber;
    }

    public void setCveNumber(String cveNumber) {
        this.cveNumber = cveNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientEntity that = (ClientEntity) o;
        return this.idClient == that.idClient && Objects.equals(this.name, that.name) && Objects.equals(this.phoneNumber, that.phoneNumber) && Objects.equals(this.email, that.email) && Objects.equals(postalAddress, that.postalAddress) && Objects.equals(this.creditCardNumber, that.creditCardNumber) && Objects.equals(this.cveNumber, that.cveNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClient, name, phoneNumber, email, postalAddress, creditCardNumber, cveNumber);
    }
}
