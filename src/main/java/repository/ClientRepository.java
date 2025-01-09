package repository;

import entity.ClientEntity;
import entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    /**
     * Requête personnalisée pour récupérer tous les véhicules d'un client via les commandes.
     *
     * @param clientId L'identifiant du client.
     * @return Une liste de véhicules associés au client.
     */
    @Query("SELECT v FROM VehicleEntity v " +
            "JOIN CommandLineEntity cl ON cl.vehicle = v " +
            "JOIN CommandEntity c ON cl.command = c " +
            "WHERE c.client.idClient = :clientId")
    List<VehicleEntity> findVehiclesByClientId(@Param("clientId") int clientId);

    /**
     * Requête pour récupérer un client par son email.
     *
     * @param email L'email du client.
     * @return Le client correspondant ou null s'il n'existe pas.
     */
    @Query("SELECT c FROM ClientEntity c WHERE c.email = :email")
    ClientEntity findByEmail(@Param("email") String email);

    /**
     * Requête pour récupérer les clients dont le nom contient une chaîne spécifique (recherche partielle).
     *
     * @param namePart La chaîne partielle à rechercher dans les noms des clients.
     * @return Une liste de clients correspondant à la recherche.
     */
    @Query("SELECT c FROM ClientEntity c WHERE c.name LIKE %:namePart%")
    List<ClientEntity> findByNameContaining(@Param("namePart") String namePart);

    /**
     * Vérifie si un client peut se connecter avec son email et son mot de passe.
     *
     * @param email    L'email du client.
     * @param password Le mot de passe du client.
     * @return true si les identifiants sont corrects, sinon false.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM ClientEntity c WHERE c.email = :email AND c.password = :password")
    boolean canClientLogIn(@Param("email") String email, @Param("password") String password);

    /**
     * Requête pour récupérer les clients avec un certain nombre de commandes minimum.
     *
     * @param minCommands Le nombre minimum de commandes.
     * @return Une liste de clients ayant au moins le nombre spécifié de commandes.
     */
    @Query("SELECT c FROM ClientEntity c WHERE SIZE(c.commands) >= :minCommands")
    List<ClientEntity> findClientsWithMinCommands(@Param("minCommands") int minCommands);
}
