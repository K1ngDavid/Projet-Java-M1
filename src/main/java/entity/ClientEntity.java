package entity;

import jakarta.persistence.*;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Client", schema = "LeTresBonCoin")
public class ClientEntity {

    public ClientEntity(){
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "active_command_id")
    private CommandEntity panier;

    @Basic
    private String password;

    public String getPassword() {
        return password;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCveNumber() {
        return cveNumber;
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

    // Définition de l'énumération interne pour le rôle
    public enum Role {
        CLIENT,
        ADMIN
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = true)
    private Role role = Role.CLIENT; // Valeur par défaut

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

    public void setCveNumber(String cveNumber) {
        this.cveNumber = cveNumber;
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

    public CommandEntity getPanier() {
        if (this.panier == null) {
            // Créer un nouveau panier avec les informations par défaut
            this.panier = new CommandEntity();
            this.panier.setClient(this);
            this.panier.setCommandStatus("En attente");
            this.panier.setCommandDate(new Date(System.currentTimeMillis()));
        }
        return this.panier;
    }



    /**
     * Ajoute un véhicule au panier en créant un CommandLineEntity.
     */
    public void addToPanier(VehicleEntity vehicle){
        // Si le panier est null, le créer
        if(this.panier == null){
            this.panier = new CommandEntity();
            this.panier.setClient(this);
            this.panier.setCommandStatus("En attente");
            this.panier.setCommandDate(new Date(System.currentTimeMillis()));
        }
        CommandLineEntity commandLine = new CommandLineEntity();
        commandLine.setVehicle(vehicle);
        this.panier.addCommandLine(commandLine);
    }


    public void setPanier(CommandEntity panier) {
        this.panier = panier;
    }

}
