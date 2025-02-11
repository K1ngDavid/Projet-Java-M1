package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VAN")
public class VanEntity extends VehicleEntity {
    // Ajoutez ici des champs ou comportements spécifiques à un véhicule de type VAN
}
