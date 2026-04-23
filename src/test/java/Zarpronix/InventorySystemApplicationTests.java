package Zarpronix;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // ← ESTA ES LA LÍNEA QUE FALTA

@SpringBootTest
@ActiveProfiles("test")
class InventorySystemApplicationTests {

    @Test
    void contextLoads() {
    }

}
