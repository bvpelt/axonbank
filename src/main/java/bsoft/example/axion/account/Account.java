package bsoft.example.axion.account;

import bsoft.example.axion.coreapi.*;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * Created by bvpelt on 1/20/17.
 */
@Aggregate(repository = "jpaAccountRepository")
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @AggregateIdentifier
    private String accountId;

    @Basic
    private int balance;

    @Basic
    private int overdraftLimit;

    @CommandHandler
    public Account(CreateAccountCommand command) {
        apply(new AccountCreatedEvent(command.getAccountId(), command.getOverdraftLimit()));
    }

    @CommandHandler
    public void handle(DepositMoneyCommand command) {
        apply(new MoneyDepositEvent(accountId, command.getAmount()));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command) throws OverdraftLimteExceededException {
            if (balance + overdraftLimit >= command.getAmount()) {
                apply(new MoneyWithdrawnEvent(accountId, command.getAmount(), balance - command.getAmount()));
            } else {
                throw new OverdraftLimteExceededException();
            }
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.overdraftLimit = event.getOverdraftLimit();
    }

    @EventSourcingHandler
    public void on(MoneyDepositEvent event) {
        this.balance = this.balance + event.getDepositAmount();
    }

    @EventSourcingHandler
    public void on(MoneyWithdrawnEvent event) {
        this.balance = event.getBalance();
    }
}
