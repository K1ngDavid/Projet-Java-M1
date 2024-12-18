package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Invoice", schema = "LeTresBonCoin", catalog = "")
public class InvoiceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idInvoice")
    private int idInvoice;
    @Basic
    @Column(name = "invoiceDate")
    private Date invoiceDate;
    @Basic
    @Column(name = "totalAmount")
    private BigDecimal totalAmount;
    @Basic
    @Column(name = "idCommand")
    private Integer idCommand;

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getIdCommand() {
        return idCommand;
    }

    public void setIdCommand(Integer idCommand) {
        this.idCommand = idCommand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceEntity that = (InvoiceEntity) o;
        return idInvoice == that.idInvoice && Objects.equals(invoiceDate, that.invoiceDate) && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(idCommand, that.idCommand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInvoice, invoiceDate, totalAmount, idCommand);
    }
}
