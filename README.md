# LeTresBonCoin

**Version :** 1.0

LeTresBonCoin est une application de gestion dédiée à la vente et à l’échange de véhicules. Elle permet aux clients de consulter le catalogue, de constituer un panier, de passer commande, de payer en ligne (avec génération automatique d'une facture PDF) et de consulter ou publier des avis. Les administrateurs disposent d'outils avancés pour gérer le catalogue, les clients et les commandes, ainsi que pour configurer l’application.

---

## Description

LeTresBonCoin est une solution complète pour la gestion de transactions liées à la vente et à l’échange de véhicules. L'application offre une interface intuitive pour les clients et des fonctionnalités étendues pour les administrateurs, permettant ainsi de gérer efficacement le catalogue de véhicules, les commandes, les avis ainsi que les comptes utilisateurs.

---

## Fonctionnalités

### Pour les Clients

- **Consultation du Catalogue :**  
  Parcourez les véhicules disponibles avec leurs images, caractéristiques techniques, prix et état.

- **Gestion du Panier :**  
  Ajoutez des véhicules à votre panier, modifiez vos sélections ou supprimez des articles. Le panier conserve vos choix durant votre session.

- **Paiement et Facturation :**  
  Validez votre panier et procédez au paiement en saisissant vos informations de paiement. Une facture PDF est générée automatiquement et sauvegardée dans votre dossier de téléchargements.

- **Gestion des Commandes et Suivi :**  
  Passez commande et consultez l’historique ainsi que l’état d’avancement de vos commandes.

- **Gestion des Avis :**  
  Lisez les avis des autres utilisateurs et laissez votre propre commentaire avec une note sur les véhicules.

- **Modification du Compte :**  
  Modifiez vos informations personnelles (nom, email, mot de passe, etc.) via la section Paramètres.

### Pour les Administrateurs

- **Gestion du Catalogue de Véhicules :**  
  Ajoutez, modifiez ou supprimez des véhicules du catalogue via un formulaire complet (incluant caractéristiques techniques, images, etc.).

- **Gestion des Clients :**  
  Consultez et gérez les comptes des clients inscrits. Vous pouvez ajouter, modifier ou supprimer des comptes selon les besoins.

- **Gestion des Commandes Globales :**  
  Visualisez l’ensemble des commandes passées par les clients, modifiez leur statut et contrôlez le traitement des paiements.

- **Configuration et Paramétrages Avancés :**  
  Accédez à un panneau d’administration pour ajuster les paramètres globaux de l’application, modérer les avis et consulter des rapports statistiques détaillés.

---

## Installation et Configuration

### Prérequis

- **Java 17** (ou ultérieur)
- **MySQL** (ou un SGBD configuré) en cours d'exécution

### Compilation et Déploiement

1. **Compilation :**  
   Dans le répertoire racine du projet, exécutez :
   ```bash
   mvn clean package
    ```
   et ensuite 
2. ```bash
   java -jar DavidRoufé.jar
   ```

Données de test
Vous pouvez vous connecter avec les données de test :

jean.dupont@email.com,Password123$

alice.martin@email.com,password456
