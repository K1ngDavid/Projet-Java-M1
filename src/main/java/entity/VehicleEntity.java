package entity;

import enumerations.VehicleType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "Vehicle", schema = "LeTresBonCoin", catalog = "")
public class VehicleEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idVehicle")
    private int idVehicle;
    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "price")
    private BigDecimal price;
    @Basic
    @Column(name = "countryOfOrigin")
    private String countryOfOrigin;
    @Basic
    @Column(name = "vehicule_model_id",insertable = false, updatable = false)
    private Integer idModel;
    @Basic
    @Column(name = "idType")
    private Integer idType;
    @Basic
    @Column(name = "horse_power")
    private Integer horsePower;
    @Basic
    @Column(name = "vehicle_power_source")
    private VehicleType vehiclePowerSource;

    @OneToOne
    @JoinColumn(name = "vehicule_model_id", referencedColumnName = "idModel", foreignKey = @ForeignKey(name = "FK_Vehicle_Model"))
    private ModelEntity model;

//     Getter pour modelName (ajouté pour obtenir le nom du modèle via la relation)
    public ModelEntity getModel() {
        if (model != null) {
            return model; // Accès à modelName depuis ModelEntity
        }
        return null;
    }

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

    public Integer getIdModel() {
        return idModel;
    }

    public void setIdModel(Integer idModel) {
        this.idModel = idModel;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public Integer getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(Integer horsePower) {
        this.horsePower = horsePower;
    }

    public Object getVehiclePowerSource() {
        return vehiclePowerSource;
    }

    public void setVehiclePowerSource(VehicleType vehiclePowerSource) {
        this.vehiclePowerSource = vehiclePowerSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleEntity that = (VehicleEntity) o;
        return idVehicle == that.idVehicle && Objects.equals(status, that.status) && Objects.equals(price, that.price) && Objects.equals(countryOfOrigin, that.countryOfOrigin) && Objects.equals(idModel, that.idModel) && Objects.equals(idType, that.idType) && Objects.equals(horsePower, that.horsePower) && Objects.equals(vehiclePowerSource, that.vehiclePowerSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVehicle, status, price, countryOfOrigin, idModel, idType, horsePower, vehiclePowerSource);
    }
}
