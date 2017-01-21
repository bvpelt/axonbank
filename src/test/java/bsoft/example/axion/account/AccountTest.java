package bsoft.example.axion.account;

import bsoft.example.axion.coreapi.*;
import org.axonframework.test.FixtureConfiguration;
import org.junit.Before;
import org.axonframework.test.Fixtures;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bvpelt on 1/20/17.
 */
public class AccountTest {


    private FixtureConfiguration<Account> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(Account.class);
    }

    @Test
    public void testCreateAccount() throws Exception {
        fixture.givenNoPriorActivity()
                .when(new CreateAccountCommand("1234", 1000))
                .expectEvents(new AccountCreatedEvent("1234", 1000));

    }

    @Test
    public void testWithdrawReasonableAmount() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithdrawMoneyCommand("1234", 400))
                .expectEvents(new MoneyWithdrawnEvent("1234", 400, -400));
    }

    @Test
    public void testWithdrawAbsurtAmount() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithdrawMoneyCommand("1234", 1001))
                .expectNoEvents()
                .expectException(OverdraftLimteExceededException.class);
    }

    @Test
    public void testWithdrawTwice() {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                new MoneyWithdrawnEvent("1234", 999, -999))
                .when(new WithdrawMoneyCommand("1234", 2))
                .expectNoEvents()
                .expectException(OverdraftLimteExceededException.class);
    }

    @Test
    public void testDepositReasonableAmount() throws Exception {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                new MoneyDepositEvent("1234", 500))
                .when(new WithdrawMoneyCommand("1234", 400))
                .expectEvents(new MoneyWithdrawnEvent("1234", 400, 100));
    }

}