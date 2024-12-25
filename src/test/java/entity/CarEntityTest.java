package entity;

import enumerations.TransmissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarEntityTest {

    private CarEntity carEntity;

    @BeforeEach
    void setUp(){
        carEntity = new CarEntity();
    }

    @Test
    void getIdVehicle() {
        carEntity.setIdVehicle(1);
        System.out.println(carEntity);
        assertEquals(1, carEntity.getIdVehicle());
    }

    @Test
    void testSetIdVehicle() {
        carEntity.setIdVehicle(2);
        assertEquals(2, carEntity.getIdVehicle());
    }

    @Test
    void testGetNumberOfDoors() {
        carEntity.setNumberOfDoors(4);
        assertEquals(4, carEntity.getNumberOfDoors());
    }

    @Test
    void testSetNumberOfDoors() {
        carEntity.setNumberOfDoors(3);
        assertEquals(3, carEntity.getNumberOfDoors());
    }


    @Test
    void testGetTransmissionType() {
        carEntity.setTransmissionType(TransmissionType.AUTOMATIC);
        assertEquals(TransmissionType.AUTOMATIC, carEntity.getTransmissionType());
    }

    @Test
    void testSetTransmissionType() {
        carEntity.setTransmissionType(TransmissionType.MANUAL);
        assertEquals(TransmissionType.MANUAL, carEntity.getTransmissionType());
    }

    @Test
    void testEquals() {
        CarEntity anotherCarEntity = new CarEntity();
        anotherCarEntity.setIdVehicle(1);
        anotherCarEntity.setNumberOfDoors(4);
        anotherCarEntity.setTransmissionType(TransmissionType.AUTOMATIC);

        carEntity.setIdVehicle(1);
        carEntity.setNumberOfDoors(4);
        carEntity.setTransmissionType(TransmissionType.AUTOMATIC);

        assertEquals(carEntity, anotherCarEntity);
    }

    @Test
    void testNotEquals() {
        CarEntity anotherCarEntity = new CarEntity();
        anotherCarEntity.setIdVehicle(2);
        anotherCarEntity.setNumberOfDoors(4);
        anotherCarEntity.setTransmissionType(TransmissionType.AUTOMATIC);

        carEntity.setIdVehicle(1);
        carEntity.setNumberOfDoors(4);
        carEntity.setTransmissionType(TransmissionType.AUTOMATIC);

        assertNotEquals(carEntity, anotherCarEntity);
    }

    @Test
    void testHashCode() {
        carEntity.setIdVehicle(1);
        carEntity.setNumberOfDoors(4);
        carEntity.setTransmissionType(TransmissionType.AUTOMATIC);

        CarEntity anotherCarEntity = new CarEntity();
        anotherCarEntity.setIdVehicle(1);
        anotherCarEntity.setNumberOfDoors(4);
        anotherCarEntity.setTransmissionType(TransmissionType.AUTOMATIC);

        assertEquals(carEntity.hashCode(), anotherCarEntity.hashCode());
    }

}
