package fh.server.controller;

import fh.server.entity.Account;
import fh.server.rest.dto.AccountDTO;
import fh.server.rest.mapper.DTOMapper;
import fh.server.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountController {

    private final AccountService accountService;

    AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * creates a new account, using the request's data as blueprint
     * @param accountDTO blueprint
     * @return newly created account data
     */
    @PostMapping("/account/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AccountDTO registerAccount(
            @RequestBody AccountDTO accountDTO
    ) {
        Account blueprint = DTOMapper.INSTANCE.convertAccountDTOtoEntity(accountDTO);
        Account created = accountService.createAccount(blueprint);
        return DTOMapper.INSTANCE.convertEntityToAccountDTO(created);
    }

    /**
     * updates an account
     * @param accountDTO blueprint
     * @param id client id
     * @param token authentication token
     * @param remote if the account to update is different from own one (requires admin rights)
     * @return updated account data
     */
    @PutMapping("/account/update")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO updateAccount(
            @RequestBody AccountDTO accountDTO,
            @RequestHeader("id") Long id,
            @RequestHeader("token") String token,
            @RequestParam("remote") Optional<Long> remote
    ) {
        Account blueprint = DTOMapper.INSTANCE.convertAccountDTOtoEntity(accountDTO);
        accountService.authenticateAccount(id, token,
                remote.isPresent()? Account.CLEARANCE_LEVEL_ADMIN : Account.CLEARANCE_LEVEL_CUSTOMER);
        Account updated = accountService.updateAccount(remote.orElse(id), blueprint);
        return DTOMapper.INSTANCE.convertEntityToAccountDTO(updated);
    }

    /**
     * deletes an account
     * @param id client id
     * @param token authentication token
     * @param remote if the account to delete is different from own one (requires admin rights)
     */
    @DeleteMapping("/account/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteAccount(
            @RequestHeader("id") Long id,
            @RequestHeader("token") String token,
            @RequestParam("remote") Optional<Long> remote
    ) {
        accountService.authenticateAccount(id, token,
                remote.isPresent()? Account.CLEARANCE_LEVEL_ADMIN : Account.CLEARANCE_LEVEL_CUSTOMER);
        accountService.deleteAccount(remote.orElse(id));
    }

    /**
     * fetches account data
     * @param id client id
     * @param token authentication token
     * @param remote if the account to fetch is different from own one (requires admin rights)
     * @return found account data
     */
    @GetMapping("/account/get")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO getAccount(
            @RequestHeader("id") Long id,
            @RequestHeader("token") String token,
            @RequestParam("remote") Optional<Long> remote
    ) {
        accountService.authenticateAccount(id, token,
                remote.isPresent()? Account.CLEARANCE_LEVEL_ADMIN : Account.CLEARANCE_LEVEL_CUSTOMER);
        Account account = accountService.fetchAccount(remote.orElse(id));
        return DTOMapper.INSTANCE.convertEntityToAccountDTO(account);
    }

    /**
     * similar to getAccount(), except that this method uses the email address instead of the id for
     * identification, and the password instead of the token for verification.
     * can't be done remotely.
     * also refreshes the account's verification token.
     * @param email client email
     * @param password client password
     * @return account data
     */
    @GetMapping("/account/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO loginAccount(
            @RequestHeader("email") String email,
            @RequestHeader("password") String password
    ) {
        Account account = accountService.loginAccount(email, password);
        return DTOMapper.INSTANCE.convertEntityToAccountDTO(account);
    }


}
