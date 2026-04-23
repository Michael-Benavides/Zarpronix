package Zarpronix;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ActiveProfiles("test")   // ← agrega esta línea
class InventorySystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
