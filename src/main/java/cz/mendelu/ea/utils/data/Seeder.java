//package cz.mendelu.ea.utils.data;
//
//import cz.mendelu.ea.domain.account.Account;
//import cz.mendelu.ea.domain.account.AccountService;
//import cz.mendelu.ea.domain.user.User;
//import cz.mendelu.ea.domain.user.UserService;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class Seeder {
//
//    private final AccountService accountService;
//    private final UserService userService;
//
//    public Seeder(AccountService accountService, UserService userService) {
//        this.accountService = accountService;
//        this.userService = userService;
//    }
//
//    private boolean shouldSeedData() {
//        return accountService.getAllAccounts().isEmpty();
//    }
//
//    @PostConstruct
//    public void seedDefaultData() {
//        if (!shouldSeedData()) {
//            log.info("--- Default data already seeded ---");
//            return;
//        }
//
//        User user1 = new User("Ivo", "ivo");
//        User user2 = new User("Marie", "mar777");
//        userService.createUser(user1);
//        userService.createUser(user2);
//
//        Account account1 = new Account(user1, "My account", 100.0);
//        Account account2 = new Account(user2, "Savings for a car", 200.0);
//        user1.attachAccount(account2);
//
//        accountService.createAccount(account1);
//        accountService.createAccount(account2);
//
//        userService.updateUser(user1.getId(), user1);
//        userService.updateUser(user2.getId(), user2);
//
//        log.info("--- Default data seeded ---");
//    }
//
//}
