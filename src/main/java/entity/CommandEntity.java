package entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Command", schema = "LeTresBonCoin", catalog = "")
public class CommandEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idCommand")
    private int idCommand;
    @Basic
    @Column(name = "commandStatus")
    private String commandStatus;
    @Basic
    @Column(name = "commandDate")
    private Date commandDate;
    @Basic
    @Column(name = "idClient")
    private Integer idClient;

    public int getIdCommand() {
        return idCommand;
    }

    public void setIdCommand(int idCommand) {
        this.idCommand = idCommand;
    }

    public String getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(String commandStatus) {
        this.commandStatus = commandStatus;
    }

    public Date getCommandDate() {
        return commandDate;
    }

    public void setCommandDate(Date commandDate) {
        this.commandDate = commandDate;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandEntity that = (CommandEntity) o;
        return idCommand == that.idCommand && Objects.equals(commandStatus, that.commandStatus) && Objects.equals(commandDate, that.commandDate) && Objects.equals(idClient, that.idClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommand, commandStatus, commandDate, idClient);
    }
}
