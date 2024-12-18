package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Model", schema = "LeTresBonCoin", catalog = "")
public class ModelEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idModel")
    private int idModel;
    @Basic
    @Column(name = "modelName")
    private String modelName;
    @Basic
    @Column(name = "brandName")
    private String brandName;

    public int getIdModel() {
        return idModel;
    }

    public void setIdModel(int idModel) {
        this.idModel = idModel;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelEntity that = (ModelEntity) o;
        return idModel == that.idModel && Objects.equals(modelName, that.modelName) && Objects.equals(brandName, that.brandName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idModel, modelName, brandName);
    }
}
