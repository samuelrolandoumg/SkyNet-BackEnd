//package com.example.demo;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class DemoApplicationTests {
//
//    @BeforeAll
//    static void loadEnv() {
//        Dotenv dotenv = Dotenv.configure().filename(".env").load();
//        System.setProperty("DB_URL", dotenv.get("DB_URL"));
//        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//    }
//
//    @Test
//    void contextLoads() {
//    }
//}

package com.example.demo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
