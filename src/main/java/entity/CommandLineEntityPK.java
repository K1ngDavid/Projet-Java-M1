package entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommandLineEntityPK implements Serializable {
    @Column(name = "idCommand")
    private int idCommand;

    @Column(name = "idVehicle")
    private int idVehicle;

    // Getters and setters
    public int getIdCommand() {
        return idCommand;
    }

    public void setIdCommand(int idCommand) {
        this.idCommand = idCommand;
    }

    public int getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(int idVehicle) {
        this.idVehicle = idVehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandLineEntityPK that = (CommandLineEntityPK) o;
        return idCommand == that.idCommand && idVehicle == that.idVehicle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommand, idVehicle);
    }
}
