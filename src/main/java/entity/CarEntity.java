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
                "<ul style='margin: 0; padding-left: 20px;'>" +
                "<li><b>ID:</b> " + getIdVehicle() + "</li>" +
                "<li><b>Status:</b> " + getStatus() + "</li>" +
                "<li><b>Prix:</b> " + getPrice() + " €</li>" +
                "<li><b>Pays d'origine:</b> " + getCountryOfOrigin() + "</li>" +
                "<li><b>Modèle:</b> " + (getModel() != null ? getModel().getModelName() : "N/A") + "</li>" +
                "<li><b>Puissance:</b> " + getHorsePower() + " HP</li>" +
                "<li><b>Source d'énergie:</b> " + getVehiclePowerSource() + "</li>" +
                "<li><b>Type:</b> " + getVehicleType() + "</li>" +
                "<li><b>Nombre de portes:</b> " + getNumberOfDoors() + "</li>" +
                "<li><b>Transmission:</b> " + getTransmissionType() + "</li>" +
                "</ul>" +
                "</html>";
    }


    @Override
    protected String getDefaultImageUrl() {
        return "/images/car.png";
    }



}
