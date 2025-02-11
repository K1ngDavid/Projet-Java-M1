package tools;

import entity.ClientEntity;
import entity.ReviewEntity;
import entity.VehicleEntity;
import frames.ProductForm;
import jakarta.persistence.EntityManager;
import service.ReviewService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class ReviewDialog extends JDialog {
    private JSlider sliderRating;
    private JTextArea txtComment;
    private JButton btnSubmit;

    private ClientEntity client;
    private ReviewService reviewService;
    private VehicleEntity vehicle;
    EntityManager entityManager;

    public ReviewDialog(Frame owner, ClientEntity client, VehicleEntity vehicle) {
        super(owner, "Laisser un avis", true);
        this.client = client;
        this.vehicle = vehicle;
        this.reviewService = new ReviewService();
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        // Utilisation d'un BorderLayout avec des espacements
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panneau pour la note (1 à 5 étoiles)
        JPanel ratingPanel = new JPanel(new BorderLayout());
        JLabel lblRating = new JLabel("Notez ce véhicule (1-5) :");
        lblRating.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sliderRating = new JSlider(1, 5, 3);
        sliderRating.setMajorTickSpacing(1);
        sliderRating.setPaintTicks(true);
        sliderRating.setPaintLabels(true);
        ratingPanel.add(lblRating, BorderLayout.NORTH);
        ratingPanel.add(sliderRating, BorderLayout.CENTER);

        // Panneau pour le commentaire
        JPanel commentPanel = new JPanel(new BorderLayout());
        JLabel lblComment = new JLabel("Votre commentaire :");
        lblComment.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtComment = new JTextArea(5, 30);
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(txtComment);
        commentPanel.add(lblComment, BorderLayout.NORTH);
        commentPanel.add(commentScroll, BorderLayout.CENTER);

        // Ajout des deux panneaux dans le panel principal
        mainPanel.add(ratingPanel, BorderLayout.NORTH);
        mainPanel.add(commentPanel, BorderLayout.CENTER);

        // Bouton pour soumettre l'avis
        btnSubmit = new JButton("Envoyer");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.addActionListener(e -> submitReview());

        add(mainPanel, BorderLayout.CENTER);
        add(btnSubmit, BorderLayout.SOUTH);
    }

    /**
     * Méthode appelée lors de la soumission de l'avis.
     * Vérifie que le commentaire n'est pas vide, crée un objet Review et ferme la boîte de dialogue.
     * Vous pouvez ajouter ici l'appel à votre service de persistance pour enregistrer l'avis en base.
     */
    private void submitReview() {
        int rating = sliderRating.getValue();
        String comment = txtComment.getText().trim();
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un commentaire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Création de l'objet Review (assurez-vous que l'entité Review existe et possède les bons champs)
        ReviewEntity review = new ReviewEntity();
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDate.now());
        review.setClient(client);
        review.setVehicle(vehicle);

        reviewService.saveReview(review);
        vehicle.getReviews().add(review);
        vehicle.getReviews()
                .forEach(reviewEntity -> System.out.println(reviewEntity.getComment()));

        JOptionPane.showMessageDialog(this, "Merci pour votre avis !", "Avis envoyé", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        getOwner().revalidate();
        try {
            super.dispose();
            new ProductForm(client,vehicle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
