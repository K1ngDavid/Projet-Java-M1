package entity;

import entity.VehicleEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Client", schema = "LeTresBonCoin", catalog = "")
public class ClientEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idClient")
    private int idClient;



    // Relation avec VehicleEntity
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehicleEntity> vehicles = new ArrayList<>(); // Liste de véhicules associés à ce client

    // Relation avec CommandEntity
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommandEntity> commands;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    private String password;


    @Basic
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Basic
    @Column(name = "email")
    private String email;

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "postalAddress")
    private String postalAddress;

    @Basic
    @Column(name = "creditCardNumber")
    private String creditCardNumber;

    @Basic
    @Column(name = "cveNumber")
    private String cveNumber;

    // Getters et setters
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public List<CommandEntity> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandEntity> commands) {
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
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

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
