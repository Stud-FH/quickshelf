package fh.server.service;

import fh.server.entity.Account;
import fh.server.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import java.util.Properties;
import java.util.UUID;

@Service
public class AccountService {

    private static final long HASH_INITIAL = 1000099; // used to hash passwords. Modifying this number will invalidate all existing passwords.
    private static final long HASH_MULTIPLIER = 31;   // used to hash passwords. Modifying this number will invalidate all existing passwords.

    private static final long MIN_PASSWORD_LENGTH = 8;
    private static final long MAX_PASSWORD_LENGTH = 20;
    private static final long MIN_PASSWORD_CHARACTER_DIVERSITY = 2;
    private static final String PASSWORD_ALLOWED_SPECIAL_CHARACTERS = "()[]{}!?&%*@#";  // none of these are supposed to allow sql injection

//    public static final String MAIL_ADDRESS = "fh.mail@bluewin.ch";
//    public static final String MAIL_USERNAME = "fh.mail";
//    public static final String MAIL_PASSWORD = "1234";

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

//    private Session session;

    private final AccountRepository accountRepository;

    public AccountService(
            @Qualifier("accountRepository") AccountRepository accountRepository
    ) {
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    private void init() {

        if (accountRepository.findAll().isEmpty()) {
            Account admin = new Account();
            admin.setEmail("admin");
            admin.setPassword("1234");
            admin.setPasswordHash(hash(admin.getPassword()));
            admin.setAddress("address");
            admin.setPhoneNumber("+41 00 000 00 00");
            admin.setToken(UUID.randomUUID().toString());
            admin.setClearanceLevel(Account.CLEARANCE_LEVEL_ADMIN);

            accountRepository.saveAndFlush(admin);
            LOGGER.info("admin account created. Please update this account as soon as possible to ensure safety.");
        }

//        java.lang.System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,TLSv1.3");
//
//        Properties properties = new Properties();
//        properties.put("mail.smtp.auth", true);
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.host", "smtpauths.bluewin.ch");
//        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.ssl.trust", "smtpauths.bluewin.ch");
//
//        session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(MAIL_USERNAME, MAIL_PASSWORD);
//            }
//        });
//
//        try {
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(MAIL_ADDRESS));
//            message.setRecipients(
//                    Message.RecipientType.TO, InternetAddress.parse("dr_duke@outlook.com"));
//            message.setSubject("Mail Subject");
//
//            String msg = "This is my first email using JavaMailer";
//
//            MimeBodyPart mimeBodyPart = new MimeBodyPart();
//            mimeBodyPart.setContent(msg, "text/html");
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(mimeBodyPart);
//
//            message.setContent(multipart);
//
//            Transport t = session.getTransport("smtp");
//
//            t.connect();
//
//            t.sendMessage(message, message.getAllRecipients());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    /**
     * verifies an account id by the corresponding token.
     * The admin token will verify any account id.
     * @param id the account id that is tried to access
     * @param token the verification token
     * @param clearanceLevel the minimum clearance level the account should have
     * @return the account corresponding to the given id
     * @throws ResponseStatusException
     *      404 if no account with this id exists;
     *      401 if token does not match the account and is not the admin token;
     *      403 if the minimum clearance level is not met;
     */
    public Account authenticateAccount(Long id, String token, int clearanceLevel) {
        if (!accountRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid account id");

        Account account = accountRepository.getById(id);
        if (!account.getToken().equals(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token");
        if (account.getClearanceLevel() < clearanceLevel)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "requires clearance level " + clearanceLevel);
        return account;
    }

    /**
     * fetches an account from the repository
     * @param id account id
     * @return the account that was found
     * @throws ResponseStatusException 404 if no account with this id exists;
     */
    public Account fetchAccount(Long id) {
        if (!accountRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid account id");
        return accountRepository.getById(id);
    }

    /**
     * creates an account
     * @param blueprint a blueprint of the account to create
     * @return the new account
     * @throws ResponseStatusException if any attribute violates any security- or integrity constraints.
     */
    public Account createAccount(Account blueprint) {
        blueprint.setId(null);
        checkEmailFormat(blueprint.getEmail());
        checkEmailUniqueness(blueprint.getEmail());

        checkPasswordFormat(blueprint.getPassword());
        blueprint.setAddress(checkAddressFormat(blueprint.getAddress()));
        blueprint.setPhoneNumber(checkPhoneNumberFormat(blueprint.getPhoneNumber()));

        blueprint.setPasswordHash(hash(blueprint.getPassword()));
        blueprint.setPassword(null);
        blueprint.setToken(UUID.randomUUID().toString());
        blueprint.setClearanceLevel(Account.CLEARANCE_LEVEL_CUSTOMER);

        // TODO verify account by email (requires additional attribute verificationToken, REST interface and mail sender)

        Account account = accountRepository.saveAndFlush(blueprint);
        LOGGER.info(account.getEmail() + " created an account");
        return account;
    }

    /**
     * fetches the account information from the repository through the email address.
     * also refreshes the account's authentication token.
     * @param email account email address
     * @param password account password
     * @return the account
     * @throws ResponseStatusException 404 if no account with this email exists; 401 if the password is wrong
     */
    public Account loginAccount(String email, String password) {
        if (!accountRepository.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown email");

        Account account = accountRepository.getByEmail(email);
        Long passwordHash = hash(password);
        if (!passwordHash.equals(account.getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong password");

        // refresh token
        account.setToken(UUID.randomUUID().toString());

        accountRepository.flush();
        LOGGER.info(account.getEmail() + " logged themselves in");
        return account;
    }

    /**
     * updates an account.
     * null attributes in the blueprint are ignored
     * @param id the id of the account to update
     * @param blueprint attributes to update
     * @return the updated account
     * @throws ResponseStatusException if an update violates any security- or integrity constraints. In that case no updates are performed.
     */
    public Account updateAccount(Long id, Account blueprint, boolean remote) {
        if (!accountRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown id");

        Account account = accountRepository.getById(id);

        // first check all modifications
        if (blueprint.getEmail() != null && !blueprint.getEmail().equals(account.getEmail())) {
            checkEmailFormat(blueprint.getEmail());
            checkEmailUniqueness(blueprint.getEmail());
        }
        if (blueprint.getPassword() != null && !blueprint.getPassword().equals(account.getPassword())) {
            checkPasswordFormat(blueprint.getPassword());
        }
        if (blueprint.getAddress() != null && !blueprint.getAddress().equals(account.getAddress())) {
            blueprint.setAddress(checkAddressFormat(blueprint.getAddress()));
        }
        if (blueprint.getPhoneNumber() != null && !blueprint.getPhoneNumber().equals(account.getPhoneNumber())) {
            blueprint.setPhoneNumber(checkPhoneNumberFormat(blueprint.getPhoneNumber()));
        }
        if (blueprint.getClearanceLevel() != null && !blueprint.getClearanceLevel().equals(account.getClearanceLevel())) {
            if (account.getClearanceLevel() >= Account.CLEARANCE_LEVEL_ADMIN
                    && blueprint.getClearanceLevel() < Account.CLEARANCE_LEVEL_ADMIN)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "cannot degrade an admin account");
        }

        // if all modifications are valid, perform the updates
        if (blueprint.getEmail() != null && !blueprint.getEmail().equals(account.getEmail())) {
            account.setEmail(blueprint.getEmail());
        }
        if (blueprint.getPassword() != null && !blueprint.getPassword().equals(account.getPassword())) {
            account.setPasswordHash(hash(blueprint.getPassword()));
        }
        if (blueprint.getAddress() != null && !blueprint.getAddress().equals(account.getAddress())) {
            account.setAddress(blueprint.getAddress());
        }
        if (blueprint.getPhoneNumber() != null && !blueprint.getPhoneNumber().equals(account.getPhoneNumber())) {
            account.setPhoneNumber(blueprint.getPhoneNumber());
        }
        if (remote && blueprint.getClearanceLevel() != null && !blueprint.getClearanceLevel().equals(account.getClearanceLevel())) {
            account.setClearanceLevel(blueprint.getClearanceLevel());
        }
        accountRepository.flush();
        LOGGER.info(account.getEmail() + (remote? " was updated" : " updated their account"));
        return account;
    }

    /**
     * deletes an account
     * @param id the id of the account to delete
     * @throws ResponseStatusException 404 if there is no such id; 403 if this account is an admin
     */
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "unknown id");
        Account account = accountRepository.getById(id);
        if (account.getClearanceLevel() == Account.CLEARANCE_LEVEL_ADMIN)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "cannot delete an admin account");
        accountRepository.deleteById(id);
        accountRepository.flush();
        LOGGER.info(account.getEmail() + " deleted their account");
    }

    /**
     * hashes a String to a long
     * @param str the String to hash
     * @return  hashed String (long)
     */
    private long hash(String str) {
        if (str == null) return 0;
        long h = HASH_INITIAL;
        for (char c : str.toCharArray()) h = HASH_MULTIPLIER * h + c;
        return h;
    }

    /**
     * checks whether a String is a valid email address.
     * Currently not checking anything.
     * @param email the String to check
     */
    private void checkEmailFormat(String email) {
        if (email == null || email.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "email cannot be empty");
        /*
         * could check for valid email format here,
         * but account verification through email would solve the problem of invalid emails anyway
         */
    }

    /**
     * checks whether an email address is unique (must check before creating an account)
     * @param email the email address to check
     * @throws ResponseStatusException 409 if there already is an account using this email address
     */
    private void checkEmailUniqueness(String email) {
        if (accountRepository.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email is already registered");
    }

    /**
     * checks whether a String is a valid password.
     * @param password the String to check
     * @throws ResponseStatusException 422 if any of the following requirements is not met:
     *      - not null
     *      - length within MIN_PASSWORD_LENGTH and MAX_PASSWORD_LENGTH
     *      - consists only of letters, digits and allowed special characters
     *      - sufficient character diversity
     */
    private void checkPasswordFormat(String password) {
        if (password == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "password cannot be null");
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("password length must be within %d and %d characters.", MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));

        int lowercase = 0;
        int uppercase = 0;
        int digit = 0;
        int special = 0;
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) lowercase++;
            else if (Character.isUpperCase(c)) uppercase++;
            else if (Character.isDigit(c)) digit++;
            else if (PASSWORD_ALLOWED_SPECIAL_CHARACTERS.contains(""+c)) special++;
            else throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "invalid character: "+c);
        }
        if (Math.min(lowercase, 1) + Math.min(uppercase, 1) + Math.min(digit, 1) + Math.min(special, 1) < MIN_PASSWORD_CHARACTER_DIVERSITY)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    String.format("password must contain at least %d different types of characters", MIN_PASSWORD_CHARACTER_DIVERSITY));

    }

    /**
     * checks whether a String is a valid address.
     * Currently not checking anything.
     * @param address the String to check
     * @return the same address
     */
    private String checkAddressFormat(String address) {
        // TODO best use google maps API or something similar instead of plaintext address
        return address;
    }

    /**
     * checks whether a String is a valid phone number.
     * For the sake of convenience, we only accept swiss phone numbers.
     * @param phoneNumber the String to check
     * @return the same phone number in the unified format +41 XX XXX XX XX
     * @throws ResponseStatusException if the provided String is not a valid phone number
     */
    private String checkPhoneNumberFormat(String phoneNumber) {
        // prefix "+41" is equal to prefix "0"
        if (phoneNumber.startsWith("+41")) phoneNumber = "0"+ phoneNumber.substring(1);
        phoneNumber = phoneNumber.replaceAll(" ", "");

        if (!phoneNumber.startsWith("0"))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "invalid phone number format");
        if (phoneNumber.length() != 10)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "invalid phone number format");
        for (char c : phoneNumber.toCharArray()) if (!Character.isDigit(c))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "invalid phone number format");
        // 0789202087
        return String.format("+41 %s %s %s %s", phoneNumber.substring(1,3), phoneNumber.substring(3,6), phoneNumber.substring(6,8), phoneNumber.substring(8,10));
    }

}
