package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Motorcycle", schema = "LeTresBonCoin", catalog = "")
public class MotorcycleEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idVehicle")
    private int idVehicle;
    @Basic
    @Column(name = "engineCapacity")
    private Integer engineCapacity;

    public int getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(int idVehicle) {
        this.idVehicle = idVehicle;
    }

    public Integer getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(Integer engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotorcycleEntity that = (MotorcycleEntity) o;
        return idVehicle == that.idVehicle && Objects.equals(engineCapacity, that.engineCapacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVehicle, engineCapacity);
    }
}
