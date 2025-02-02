package entity;

import enumerations.VehicleType; // ✅ Import correct de l'énumération
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "VehicleType", schema = "LeTresBonCoin")
public class VehicleTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idType")
    private int idType;

    @Enumerated(EnumType.STRING) // ✅ Utilisation correcte de l'énumération
    @Column(name = "typeName", nullable = false, unique = true)
    private VehicleType typeName;

    // ✅ Getters et Setters
    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public VehicleType getTypeName() {
        return typeName;
    }

    public void setTypeName(VehicleType typeName) {
        this.typeName = typeName;
    }

    // ✅ Correction de equals() et hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleTypeEntity that = (VehicleTypeEntity) o;
        return idType == that.idType && typeName == that.typeName; // 🔥 Comparaison directe de l'enum
    }

    @Override
    public int hashCode() {
        return Objects.hash(idType, typeName);
    }
}
