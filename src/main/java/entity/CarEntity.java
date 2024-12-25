package entity;

import enumerations.TransmissionType;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CAR")  // Valeur du discriminant pour cette classe (ce sera "CAR")
public class CarEntity extends VehicleEntity {

    @Override
    public String toString() {
        return "CarEntity{" +
                "idVehicle=" + getIdVehicle() +
                ", status='" + getStatus() + '\'' +
                ", price=" + getPrice() +
                ", countryOfOrigin='" + getCountryOfOrigin() + '\'' +
                ", model=" + getModel().getModelName() +
                ", horsePower=" + getHorsePower() +
                ", vehiclePowerSource=" + getVehiclePowerSource() +
                ", vehicleType=" + getVehicleType() +
                ", numberOfDoors=" + getNumberOfDoors() +
                ", transmissionType=" + getTransmissionType() +
                '}';
    }
}
