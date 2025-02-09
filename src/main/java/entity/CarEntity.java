package entity;

import enumerations.TransmissionType;
import jakarta.persistence.*;
import service.VehicleService;

@Entity
@DiscriminatorValue("CAR")  // Valeur du discriminant pour cette classe (ce sera "CAR")
public class CarEntity extends VehicleEntity {

    public CarEntity(){
        setImageUrl("/images/car.png");
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

    @Override
    protected String getDefaultImageUrl() {
        return "/images/car.png";
    }



}
