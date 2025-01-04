package entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Command", schema = "LeTresBonCoin")
public class CommandEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idCommand")
    private int idCommand;

    @ManyToOne
    @JoinColumn(name = "idClient", nullable = true)
    private ClientEntity client;

    @OneToMany(mappedBy = "command", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommandLineEntity> commandLines;

    @Basic
    @Column(name = "commandStatus")
    private String commandStatus;

    @Basic
    @Column(name = "commandDate")
    private Date commandDate;

    // Getter pour récupérer tous les véhicules de la commande
    public List<VehicleEntity> getVehicles() {
        return commandLines.stream()
                .map(CommandLineEntity::getVehicle)
                .collect(Collectors.toList());
    }

    // Getters et setters
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

    public List<CommandLineEntity> getCommandLines() {
        return commandLines;
    }

    public void setCommandLines(List<CommandLineEntity> commandLines) {
        this.commandLines = commandLines;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }
}
