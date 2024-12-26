package neoflex.deal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@SpringBootApplication
@Slf4j
@EnableFeignClients
@EnableJpaRepositories
public class DealApplication {
    public static void main(String[] args) {
        SpringApplication.run(DealApplication.class,args);
        log.info("Start!");
    }
}
