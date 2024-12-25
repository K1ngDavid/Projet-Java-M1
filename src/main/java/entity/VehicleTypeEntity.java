package entity;

import enumerations.PowerSource;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "VehicleType", schema = "LeTresBonCoin", catalog = "")
public class VehicleTypeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idType")
    private int idType;
    @Enumerated(EnumType.STRING) // Utilisation du type énuméré
    @Column(name = "typeName")
    private PowerSource typeName;

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public PowerSource getTypeName() {
        return typeName;
    }

    public void setTypeName(PowerSource typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleTypeEntity that = (VehicleTypeEntity) o;
        return idType == that.idType && Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idType, typeName);
    }
}
