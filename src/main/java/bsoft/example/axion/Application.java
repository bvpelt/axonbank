package bsoft.example.axion;

import bsoft.example.axion.account.Account;
import bsoft.example.axion.coreapi.CreateAccountCommand;
import bsoft.example.axion.coreapi.WithdrawMoneyCommand;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

/**
 * Created by bvpelt on 1/20/17.
 */
public class Application {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Configuration config = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(Account.class)
                .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
                .configureCommandBus(c -> new AsynchronousCommandBus())
                .buildConfiguration();

        config.start();
        config.commandBus().dispatch(asCommandMessage(new CreateAccountCommand("4321", 500)));
        config.commandBus().dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 250)));
        config.commandBus().dispatch(asCommandMessage(new WithdrawMoneyCommand("4321", 251)));
    }
}
