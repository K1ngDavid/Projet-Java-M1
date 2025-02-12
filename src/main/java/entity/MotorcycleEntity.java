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

    public MotorcycleEntity(){
        setImageUrl("/images/motorcycle.png");
    }

    @Override
    public String toString() {
        return "<html>" +
                "ID: " + getIdVehicle() + "<br>" +
                "Prix: " + getPrice() + " €<br>" +
                "Pays d'origine: " + getCountryOfOrigin() + "<br>" +
                "Modèle: " + (getModel() != null ? getModel().getModelName() : "N/A") + "<br>" +
                "Puissance: " + getHorsePower() + " HP<br>" +
                "Source d'énergie: " + getVehiclePowerSource() + "<br>" +
                "Type: " + getVehicleType() + "<br>" +
                "Transmission: " + getTransmissionType() +
                "</html>";
    }

    @Override
    protected String getDefaultImageUrl() {
        return "/images/motorcycle.png";
    }


}
