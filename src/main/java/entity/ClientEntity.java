package entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Client", schema = "LeTresBonCoin")
public class ClientEntity {

    public ClientEntity(){
        panier = new ArrayList<>();
    }
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idClient")
    private int idClient;

    // Relation avec CommandEntity
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommandEntity> commands;

    @Basic
    @Column(name = "name")
    private String name;

    @Transient
    private List<VehicleEntity> panier;

    @Basic
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<VehicleEntity> getVehicles() {
        List<VehicleEntity> vehicles = new ArrayList<>();
        for (CommandEntity command : this.commands) {
            vehicles.addAll(command.getVehicles());
        }
        return vehicles;
    }

    @Basic
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Basic
    @Column(name = "email",unique = true)
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

    @Override
    public String toString() {
        return "ClientEntity{" +
                "idClient=" + idClient +
                ", commands=" + commands +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", cveNumber='" + cveNumber + '\'' +
                '}';
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public List<VehicleEntity> getPanier() {
        return panier;
    }

    public void setPanier(List<VehicleEntity> panier) {
        this.panier = panier;
    }

    public void addToPanier(VehicleEntity vehicle) {
        this.panier.add(vehicle);
    }

    public void removeFromPanier(VehicleEntity vehicle) {
        this.panier.remove(vehicle);
    }
}
