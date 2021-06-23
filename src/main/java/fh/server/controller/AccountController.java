package fh.server.controller;

import fh.server.rest.dto.AccountDTO;
import fh.server.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * TODO
     * @param accountDTO
     * @return
     */
    @PostMapping("/account/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AccountDTO registerAccount(
            @RequestBody AccountDTO accountDTO
    ) {
//        Account accountBlueprint = DTOMapper.INSTANCE.convertAccountDTOtoEntity(accountDTO);
//        Account created = accountService.createAccount(accountBlueprint);
//        return DTOMapper.INSTANCE.convertEntityToAccountDTO(created);
        // TODO
        return null;
    }

    /**
     * TODO
     * @param accountDTO
     * @param id
     * @param token
     * @return
     */
    @PutMapping("/account/edit")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO editAccount(
            @RequestBody AccountDTO accountDTO,
            @RequestHeader("id") Long id,
            @RequestHeader("token") String token
    ) {
        // TODO
        return null;
    }

    /**
     * TODO
     * @param id
     * @param token
     * @return
     */
    @DeleteMapping("/account/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteAccount(
            @RequestHeader("id") Long id,
            @RequestHeader("token") String token
    ) {
        // TODO
    }

    /**
     * TODO
     * @param id
     * @param token
     * @return
     */
    @GetMapping("/account/get")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO getAccount(
            @RequestHeader("id") Long id,
            @RequestHeader("token") String token
    ) {
        // TODO
        return null;
    }


}
