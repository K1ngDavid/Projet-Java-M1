package entity;

import enumerations.TransmissionType;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Car", schema = "LeTresBonCoin", catalog = "")
public class CarEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idVehicle")
    private int idVehicle;
    @Basic
    @Column(name = "numberOfDoors")
    private Integer numberOfDoors;
    @Enumerated(EnumType.STRING) // Utilisation du type énuméré
    @Column(name = "transmissionType")
    private TransmissionType transmissionType;

    public int getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(int idVehicle) {
        this.idVehicle = idVehicle;
    }

    public Integer getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(Integer numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(TransmissionType transmissionType) {
        this.transmissionType = transmissionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarEntity carEntity = (CarEntity) o;
        return idVehicle == carEntity.idVehicle && Objects.equals(numberOfDoors, carEntity.numberOfDoors) && Objects.equals(transmissionType, carEntity.transmissionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVehicle, numberOfDoors, transmissionType);
    }
}
