package service;

import entity.ClientEntity;
import entity.CommandEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CommandService extends Service {

    /**
     * ✅ Crée une nouvelle commande et l'ajoute à la base de données.
     * @param command La commande à enregistrer
     * @return true si la commande est enregistrée avec succès, sinon false
     */
    public boolean createCommand(CommandEntity command) {
        try {
            entityManager.persist(command);
            entityManager.flush(); // ✅ Forcer l'ID à être généré immédiatement
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * ✅ Récupère la commande "En attente" du client (ou null si elle n'existe pas).
     * @param client Le client concerné
     * @return La commande en attente ou null si aucune commande en attente n'existe
     */
    public CommandEntity getPendingCommand(ClientEntity client) {
        if (client == null) {
            return null;
        }

        try {
            String hql = "SELECT c FROM CommandEntity c WHERE c.client = :client AND c.commandStatus = 'En attente'";
            TypedQuery<CommandEntity> query = entityManager.createQuery(hql, CommandEntity.class);
            query.setParameter("client", client);
            List<CommandEntity> results = query.getResultList();

            return results.isEmpty() ? null : results.get(0); // ✅ Retourne la commande trouvée ou null
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ✅ Récupère **toutes** les commandes "En attente" du client.
     * @param client Le client concerné
     * @return Liste des commandes en attente ou une liste vide si aucune n'existe
     */
    public List<CommandEntity> getPendingCommands(ClientEntity client) {
        if (client == null) {
            return List.of(); // ✅ Retourne une liste vide si le client est null
        }

        try {
            String hql = "SELECT c FROM CommandEntity c WHERE c.client = :client AND c.commandStatus = 'En attente'";
            TypedQuery<CommandEntity> query = entityManager.createQuery(hql, CommandEntity.class);
            query.setParameter("client", client);

            return query.getResultList(); // ✅ Retourne la liste complète des commandes "En attente"
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // ✅ Évite les erreurs en retournant une liste vide en cas de problème
        }
    }



    /**
     * ✅ Récupère **toutes** les commandes "Payée" du client.
     * @param client Le client concerné
     * @return Liste des commandes en attente ou une liste vide si aucune n'existe
     */
    public List<CommandEntity> getPaidCommands(ClientEntity client) {
        if (client == null) {
            return List.of(); // ✅ Retourne une liste vide si le client est null
        }

        try {
            String hql = "SELECT c FROM CommandEntity c WHERE c.client = :client AND c.commandStatus = 'Payée'";
            TypedQuery<CommandEntity> query = entityManager.createQuery(hql, CommandEntity.class);
            query.setParameter("client", client);

            return query.getResultList(); // ✅ Retourne la liste complète des commandes "En attente"
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // ✅ Évite les erreurs en retournant une liste vide en cas de problème
        }
    }

    public List<CommandEntity> getAllPaidCommands() {
        try {
            String hql = "SELECT c FROM CommandEntity c WHERE c.commandStatus = 'Payée'";
            TypedQuery<CommandEntity> query = entityManager.createQuery(hql, CommandEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CommandEntity> getAllCommandsByClient(ClientEntity client) {
        try {
            String hql = "SELECT c FROM CommandEntity c WHERE c.commandStatus = 'Payée' AND c.client = :client";
            TypedQuery<CommandEntity> query = entityManager.createQuery(hql, CommandEntity.class);
            query.setParameter("client", client);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Récupère toutes les commandes dont le statut est "En attente".
     *
     * @return Liste des commandes en attente.
     */
    public List<CommandEntity> getAllPendingCommands() {
        try {
            String hql = "SELECT c FROM CommandEntity c WHERE c.commandStatus = 'En attente'";
            TypedQuery<CommandEntity> query = entityManager.createQuery(hql, CommandEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * ✅ Met à jour une commande existante.
     * @param commande La commande à mettre à jour
     * @return true si la mise à jour réussit, sinon false
     */
    public void updateCommand(CommandEntity commande) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(commande);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }


    /**
     * ✅ Supprime une commande.
     * @param command La commande à supprimer
     * @return true si la suppression réussit, sinon false
     */
    public boolean deleteCommand(CommandEntity command) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (!entityManager.contains(command)) {
                command = entityManager.merge(command); // ✅ Assurer la bonne gestion par Hibernate
            }
            entityManager.remove(command);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            return false;
        }
    }

    public CommandEntity getLastPendingCommand(ClientEntity client) {
        try {
            String hql = "SELECT c FROM CommandEntity c WHERE c.client = :client AND c.commandStatus = 'En attente' ORDER BY c.idCommand DESC";
            TypedQuery<CommandEntity> query = entityManager.createQuery(hql, CommandEntity.class);
            query.setParameter("client", client);
            query.setMaxResults(1); // ✅ Récupérer uniquement la commande avec le plus grand ID
            List<CommandEntity> results = query.getResultList();

            return results.isEmpty() ? null : results.get(0); // ✅ Retourne la dernière commande en attente
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
