package bsoft.example.axion.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

/**
 * Created by bvpelt on 1/20/17.
 */

class CreateAccountCommand(val accountId: String, val overdraftLimit: Int)
class DepositMoneyCommand(val accountId: String, val amount: Int)
class WithdrawMoneyCommand(@TargetAggregateIdentifier val accountId: String, val amount: Int)

class AccountCreatedEvent(val accountId: String, val overdraftLimit: Int)
class MoneyDepositEvent(val accountId: String, val depositAmount: Int)
class MoneyWithdrawnEvent(val accountId: String, val amount: Int, val balance: Int)
