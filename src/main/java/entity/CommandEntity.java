package entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Command", schema = "LeTresBonCoin", catalog = "")
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

    // Removed redundant idClient field

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandEntity that = (CommandEntity) o;
        return idCommand == that.idCommand &&
                Objects.equals(commandStatus, that.commandStatus) &&
                Objects.equals(commandDate, that.commandDate) &&
                Objects.equals(client, that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommand, commandStatus, commandDate, client);
    }
}
