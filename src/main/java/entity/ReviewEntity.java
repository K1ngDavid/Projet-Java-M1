package entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReview;

    private int rating;

    @Column(length = 2000)
    private String comment;

    private LocalDate reviewDate;

    @ManyToOne
    private VehicleEntity vehicle;

    @ManyToOne
    private ClientEntity client;

    // Getters, setters, et constructeurs
}
