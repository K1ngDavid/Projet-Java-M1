package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "CommandLine", schema = "LeTresBonCoin", catalog = "")
@IdClass(CommandLineEntityPK.class)
public class CommandLineEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idCommand")
    private int idCommand;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idVehicle")
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
        CommandLineEntity that = (CommandLineEntity) o;
        return idCommand == that.idCommand && idVehicle == that.idVehicle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommand, idVehicle);
    }
}
