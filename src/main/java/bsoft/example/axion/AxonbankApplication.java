package bsoft.example.axion;

import bsoft.example.axion.account.Account;
import bsoft.example.axion.coreapi.CreateAccountCommand;
import bsoft.example.axion.coreapi.WithdrawMoneyCommand;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.model.GenericJpaRepository;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.spring.config.EnableAxonAutoConfiguration;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

//import org.axonframework.spring.config.EnableAxon;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@EnableAxonAutoConfiguration           // Check for aggregates on classpath
//@EnableAxon
@SpringBootApplication
public class AxonbankApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext config = SpringApplication.run(AxonbankApplication.class, args);

        CommandBus commandBus = config.getBean(CommandBus.class);

        commandBus.dispatch(asCommandMessage(new CreateAccountCommand("4321", 500)), new CommandCallback<Object, Object>() {
            @Override
            public void onSuccess(CommandMessage<?> commandMessage, Object o) {
                System.out.println("See told you!");
                commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 250)));
                commandBus.dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 251)));
            }

            @Override
            public void onFailure(CommandMessage<?> commandMessage, Throwable cause) {
                cause.printStackTrace();
            }
        });


    }

    @Bean
    public Repository<Account> jpaAccountRepository(EventBus eventBus) {
        return new GenericJpaRepository<Account>(entityManagerProvider(), Account.class, eventBus);
    }

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    @Bean
    public EventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public TransactionManager axonTransactionManager(PlatformTransactionManager tx) {
        return new SpringTransactionManager(tx);
    }


//    @Bean
//    public CommandBus commandBus() {
//        return new AsynchronousCommandBus();
//    }
}
