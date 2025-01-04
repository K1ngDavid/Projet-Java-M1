//package repository;
//
//import entity.ClientEntity;
//import entity.VehicleEntity;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
//
//    // Requête personnalisée pour récupérer tous les véhicules d'un client via les commandes
//    @Query("SELECT v FROM VehicleEntity v " +
//            "JOIN CommandLineEntity cl ON cl.vehicle = v " +
//            "JOIN CommandEntity c ON cl.command = c " +
//            "WHERE c.client.idClient = :clientId")
//    List<VehicleEntity> findVehiclesByClientId(@Param("clientId") int clientId);
//}
