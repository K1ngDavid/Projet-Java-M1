package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@DiscriminatorValue("MOTORCYCLE")  // Valeur du discriminant pour cette classe (ce sera "MOTORCYCLE")
public class MotorcycleEntity extends VehicleEntity {

    @Basic
    @Column(name = "engineCapacity")
    private Integer engineCapacity;

    public Integer getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(Integer engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

}
