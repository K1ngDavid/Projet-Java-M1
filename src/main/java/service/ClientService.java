package service;


import entity.ClientEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClientService {
    private EntityManager entityManager;

    // Constructeur
    public ClientService(EntityManager entityManager){
        this.entityManager = entityManager;
    }

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
            List<ClientEntity> results = query.getResultList();

            // On suppose qu'un seul client correspond au nom
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère un client par son identifiant.
     *
     * @param id L'identifiant du client recherché.
     * @return Le client correspondant ou null si aucun client n'est trouvé.
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
            String hql = "FROM ClientEntity";
            TypedQuery<ClientEntity> query = entityManager.createQuery(hql, ClientEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ajoute un nouveau client à la base de données.
     *
     * @param client L'objet ClientEntity à ajouter.
     */
    public boolean addClient(ClientEntity client) {

        try {
            if(!entityManager.getTransaction().isActive()){
                entityManager.getTransaction().begin();
            }
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
     * @param client L'objet ClientEntity à mettre à jour.
     * @return
     */
    public ClientEntity updateClient(ClientEntity client) {
        try {
            if(!entityManager.getTransaction().isActive()){
                entityManager.getTransaction().begin();
            }
            entityManager.merge(client);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return client;
    }

    /**
     * Supprime un client par son identifiant.
     *
     * @param id L'identifiant du client à supprimer.
     */
    public boolean deleteClient(int clientId) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }

        ClientEntity clientToDelete = entityManager.find(ClientEntity.class, clientId);
        if (clientToDelete != null) {
            entityManager.remove(clientToDelete);
            entityManager.getTransaction().commit();
            return true;
        } else {
            return false;
        }
    }


    /**
     * Récupère les clients dont le nom contient une certaine chaîne de caractères.
     *
     * @param partialName La chaîne de caractères à rechercher dans les noms des clients.
     * @return Une liste de clients dont le nom contient la chaîne de caractères.
     */
    public List<ClientEntity> getClientsByNameContains(String partialName) {
        try {
            String hql = "FROM ClientEntity c WHERE c.name LIKE :partialName";
            TypedQuery<ClientEntity> query = entityManager.createQuery(hql, ClientEntity.class);
            query.setParameter("partialName", "%" + partialName + "%");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère les clients par un critère spécifique, par exemple, les clients par pays.
     *
     * @param country Le pays des clients recherchés.
     * @return Une liste de clients du pays spécifié.
     */
    public List<ClientEntity> getClientsByCountry(String country) {
        try {
            String hql = "FROM ClientEntity c WHERE c.country = :country";
            TypedQuery<ClientEntity> query = entityManager.createQuery(hql, ClientEntity.class);
            query.setParameter("country", country);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère un client par son email.
     *
     * @param email L'email du client recherché.
     * @return Le client correspondant ou null si aucun client n'est trouvé.
     */
    public ClientEntity getClientByEmail(String email) {
        try {
            String hql = "FROM ClientEntity c WHERE c.email = :email";
            TypedQuery<ClientEntity> query = entityManager.createQuery(hql, ClientEntity.class);
            query.setParameter("email", email);
            List<ClientEntity> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Compte le nombre total de clients dans la base de données.
     *
     * @return Le nombre total de clients.
     */
    public long countClients() {
        try {
            String hql = "SELECT COUNT(c) FROM ClientEntity c";
            TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
