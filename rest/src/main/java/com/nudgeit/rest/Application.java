package com.nudgeit.rest;

import com.nudgeit.domain.Ticket;
import com.nudgeit.domain.TicketState;
import com.nudgeit.domain.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nudgeit")
@EntityScan(basePackages = "com.nudgeit")
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class
})
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value(value = "#{T(java.lang.Integer).parseUnsignedInt('${test.items.count}')}")
    private int nbOfTestItemsToCreate = 1000000;

    @Bean
    CommandLineRunner init(TicketRepository ticketRepository) {
        //TODO: remove this testing lines
        for (int i = 0; i < nbOfTestItemsToCreate; i++) {
            ticketRepository.save(
                    new Ticket("title_" + i, "description_" + i, "acceptanceCriteria_" + i, TicketState.ACTIVE)
            );
        }
        return null;
    }

}

