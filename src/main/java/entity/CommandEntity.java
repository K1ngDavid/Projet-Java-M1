package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
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
    @JoinColumn(name = "idClient",nullable = false)
    private ClientEntity client;

    @OneToMany(mappedBy = "command", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CommandLineEntity> commandLines;

    @Basic
    @Column(name = "commandStatus")
    private String commandStatus;

    @Basic
    @Column(name = "commandDate")
    private Date commandDate;

    public CommandEntity() {
        commandLines = new ArrayList<>();
    }

    // ✅ Getter pour récupérer tous les véhicules de la commande
    public List<VehicleEntity> getVehicles() {
        return commandLines.stream()
                .map(CommandLineEntity::getVehicle)
                .collect(Collectors.toList());
    }

    // ✅ Calcul du montant total de la commande
    public BigDecimal getTotalAmount() {
        return commandLines.stream()
                .map(commandLine -> commandLine.getVehicle().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ✅ Vérifier si la commande est en attente
    public boolean isPending() {
        return "En attente".equalsIgnoreCase(this.commandStatus);
    }

    // ✅ Marquer la commande comme payée
    public void markAsPaid() {
        this.commandStatus = "Payée";
    }

    // ✅ Annuler la commande (supprime toutes les lignes de commande)
    public void cancelCommand() {
        this.commandLines.clear();
        this.commandStatus = "Annulée";
    }

    // ✅ Ajouter une ligne de commande (évite les doublons)
    public void addCommandLine(CommandLineEntity commandLine) {
        if (!commandLines.contains(commandLine)) {
            commandLines.add(commandLine);
        }
    }

    // ✅ Afficher un résumé de la commande
    public String getCommandSummary() {
        return "Commande #" + idCommand +
                " | Client: " + (client != null ? client.getName() : "Inconnu") +
                " | Status: " + commandStatus +
                " | Date: " + commandDate +
                " | Montant Total: " + getTotalAmount() + "€";
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
