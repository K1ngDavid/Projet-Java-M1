package service;

import entity.ClientEntity;
import entity.CommandEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import tools.JPAUtil;

import java.util.List;

public class ClientService extends Service {

    /**
     * Récupère un client par son nom.
     *
     * @param name Le nom du client recherché.
     * @return Le client correspondant ou null si aucun client n'est trouvé.
     */
    public ClientEntity getClientByName(String name) {
        try {
            String hql = "FROM ClientEntity c WHERE c.name = :name";
            TypedQuery<ClientEntity> query = entityManager.createQuery(hql, ClientEntity.class);
            query.setParameter("name", name);
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère un client par son email.
     *
     * @param email Email du client recherché.
     * @return Le client correspondant ou null si aucun client n'est trouvé.
     */
    public ClientEntity getClientByEmail(String email) {
        try {
            String hql = "FROM ClientEntity c WHERE c.email = :email";
            TypedQuery<ClientEntity> query = entityManager.createQuery(hql, ClientEntity.class);
            query.setParameter("email", email);
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère les commandes d'un client.
     *
     * @param clientId ID du client.
     * @return Liste des commandes associées.
     */
    public List<CommandEntity> getCommandsByClientId(int clientId) {
        try {
            ClientEntity client = entityManager.find(ClientEntity.class, clientId);
            return (client != null) ? client.getCommands() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère un client par son ID.
     *
     * @param id Identifiant du client.
     * @return L'entité ClientEntity trouvée ou null.
     */
    public ClientEntity getClientById(int id) {
        try {
            return entityManager.find(ClientEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère tous les clients.
     *
     * @return Une liste de tous les clients.
     */
    public List<ClientEntity> getAllClients() {
        try {
            TypedQuery<ClientEntity> query = entityManager.createQuery("FROM ClientEntity", ClientEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ajoute un nouveau client à la base de données.
     *
     * @param client L'entité ClientEntity à ajouter.
     * @return true si l'ajout est réussi, false sinon.
     */
    public boolean addClient(ClientEntity client) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(client);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Met à jour un client existant.
     *
     * @param client L'entité ClientEntity mise à jour.
     * @return Le client mis à jour.
     */
    public ClientEntity updateClient(ClientEntity client) {
        try {
            entityManager.getTransaction().begin();
            ClientEntity updatedClient = entityManager.merge(client);
            entityManager.getTransaction().commit();
            return updatedClient;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return null;
        }
    }

    /**
     * Supprime un client par son ID.
     *
     * @param clientId ID du client à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean deleteClient(int clientId) {
        try {
            entityManager.getTransaction().begin();
            ClientEntity client = entityManager.find(ClientEntity.class, clientId);
            if (client != null) {
                entityManager.remove(client);
                entityManager.getTransaction().commit();
                return true;
            }
            entityManager.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Vérifie si un email existe déjà.
     *
     * @param email Email à vérifier.
     * @return true si l'email existe déjà, false sinon.
     */
    public boolean isEmailAlreadyExists(String email) {
        try {
            String hql = "SELECT COUNT(c) FROM ClientEntity c WHERE c.email = :email";
            TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifie si un client peut se connecter.
     *
     * @param email Email du client.
     * @param pswd  Mot de passe du client.
     * @return true si le client peut se connecter, false sinon.
     */
    public boolean clientCanLogIn(String email, String pswd) {
        try {
            String hql = "SELECT COUNT(c) FROM ClientEntity c WHERE c.email = :email AND c.password = :pswd";
            TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
            query.setParameter("email", email);
            query.setParameter("pswd", pswd);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
