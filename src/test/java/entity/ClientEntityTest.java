package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientEntityTest {

    private ClientEntity client;

    @BeforeEach
    void setUp() {
        client = new ClientEntity();
        client.setIdClient(1);
        client.setName("Michel");
        client.setPhoneNumber("0782749122");
        client.setEmail("david.roufe@gmail.com");
        client.setPostalAddress("d");
        client.setCreditCardNumber("a");

    }

    @Test
    void getIdClient() {
        client.setIdClient(1);
        assertEquals(1,client.getIdClient());
    }

//    @Test
//    void setIdClient() {
//    }

    @Test
    void getName() {
        client.setName("Michel");
        assertEquals("Michel",client.getName());
    }

//    @Test
//    void setName() {
//
//    }

    @Test
    void getPhoneNumber() {
    }

//    @Test
//    void setPhoneNumber() {
//    }

    @Test
    void getEmail() {
    }

//    @Test
//    void setEmail() {
//    }

    @Test
    void getPostalAddress() {
    }

//    @Test
//    void setPostalAddress() {
//    }

    @Test
    void getCreditCardNumber() {
    }

//    @Test
//    void setCreditCardNumber() {
//    }

    @Test
    void getCveNumber() {
    }

//    @Test
//    void setCveNumber() {
//    }

    @Test
    void testEquals() {
        ClientEntity client2 = new ClientEntity();
        client2.setIdClient(1);
        client2.setName("Michel");
        client2.setPhoneNumber("0782749122");
        client2.setEmail("david.roufe@gmail.com");
        client2.setPostalAddress("d");
        client2.setCreditCardNumber("a");
        System.out.println(client.getName());
        assertEquals(client, client2);
    }

    @Test
    void testHashCode() {

    }
}
