package entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class CommandLineEntityPK implements Serializable {
    @Column(name = "idCommand")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCommand;
    @Column(name = "idVehicle")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idVehicle;

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
