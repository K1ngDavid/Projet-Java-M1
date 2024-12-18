package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Client", schema = "LeTresBonCoin", catalog = "")
public class ClientEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idClient")
    private int idClient;
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

    public int getIdClient() {
        return idClient;
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
        return idClient == that.idClient && Objects.equals(name, that.name) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(email, that.email) && Objects.equals(postalAddress, that.postalAddress) && Objects.equals(creditCardNumber, that.creditCardNumber) && Objects.equals(cveNumber, that.cveNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClient, name, phoneNumber, email, postalAddress, creditCardNumber, cveNumber);
    }
}
