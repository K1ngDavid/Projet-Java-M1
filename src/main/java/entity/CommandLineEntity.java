package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CommandLine", schema = "LeTresBonCoin", catalog = "")
public class CommandLineEntity {

    @EmbeddedId
    private CommandLineEntityPK id;

    @ManyToOne
    @JoinColumn(name = "idCommand", referencedColumnName = "idCommand", insertable = false, updatable = false)
    private CommandEntity command;

    @ManyToOne
    @JoinColumn(name = "idVehicle", referencedColumnName = "idVehicle", insertable = false, updatable = false)
    private VehicleEntity vehicle;

    // Getters and setters
    public CommandLineEntityPK getId() {
        return id;
    }

    public void setId(CommandLineEntityPK id) {
        this.id = id;
    }

    public CommandEntity getCommand() {
        return command;
    }

    public void setCommand(CommandEntity command) {
        this.command = command;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }
}
