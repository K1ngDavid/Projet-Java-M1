package entity;

import enumerations.PowerSource;
import enumerations.TransmissionType;
import enumerations.VehicleCategory;
import enumerations.VehicleType;
import jakarta.persistence.*;
import service.VehicleService;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Vehicle", schema = "LeTresBonCoin")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Vous pouvez aussi utiliser cette stratégie
@DiscriminatorColumn(name = "vehicleType", discriminatorType = DiscriminatorType.STRING) // Colonne pour différencier les types de véhicules
public abstract class VehicleEntity {

    @Transient
    protected VehicleService vehicleService;

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVehicle")
    private int idVehicle;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewEntity> reviews;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "countryOfOrigin")
    private String countryOfOrigin;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_power_source")
    private PowerSource vehiclePowerSource;

    @Column(name = "transmissionType")
    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(TransmissionType transmissionType) {
        this.transmissionType = transmissionType;
    }

    @Column(name = "numberOfDoors")
    private Integer numberOfDoors;

    public Integer getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    @Column(name = "horse_power")
    private Integer horsePower;

    // Ajout de la colonne vehicleType
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicleType",insertable=false, updatable=false)
    private VehicleType vehicleType;  // Ajout de l'enum VehicleType

    @OneToOne
    @JoinColumn(name = "vehicule_model_id", referencedColumnName = "idModel", foreignKey = @ForeignKey(name = "FK_Vehicle_Model"))
    private ModelEntity model;

    // Getters and setters
    public int getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(int idVehicle) {
        this.idVehicle = idVehicle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PowerSource getVehiclePowerSource() {
        return vehiclePowerSource;
    }

    public void setVehiclePowerSource(PowerSource vehiclePowerSource) {
        this.vehiclePowerSource = vehiclePowerSource;
    }

    public Integer getHorsePower() {
        return horsePower;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setHorsePower(Integer horsePower) {
        this.horsePower = horsePower;
    }

    public ModelEntity getModel() {
        return model;
    }

    public void setModel(ModelEntity model) {
        this.model = model;
    }

    @PrePersist
    public void prePersist() {
        if (this.imageUrl == null) {
            this.imageUrl = getDefaultImageUrl();
        }
    }

    /**
     * ✅ Méthode à surcharger dans les sous-classes pour définir l'image par défaut.
     */
    protected String getDefaultImageUrl() {
        return "/images/car.png"; // Valeur par défaut pour les véhicules génériques
    }

    @Override
    public String toString() {
        return "<html>" +
                "ID: " + getIdVehicle() + "<br>" +
                "Status: " + getStatus() + "<br>" +
                "Prix: " + getPrice() + " €<br>" +
                "Pays d'origine: " + getCountryOfOrigin() + "<br>" +
                "Modèle: " + (getModel() != null ? getModel().getModelName() : "N/A") + "<br>" +
                "Puissance: " + getHorsePower() + " HP<br>" +
                "Source d'énergie: " + getVehiclePowerSource() + "<br>" +
                "Type: " + getVehicleType() + "<br>" +
                "Nombre de portes: " + getNumberOfDoors() + "<br>" +
                "Transmission: " + getTransmissionType() +
                "</html>";
    }
}
