package ch.bookoflies.qsserver.rest.dto;

import ch.bookoflies.qsserver.persistent.Stringset;
import ch.bookoflies.qsserver.persistent.User;

import java.util.Map;
import java.util.Set;

public class LibraryDTO {

    private String name;

    private String ownerName;

    private Set<String> articleNames;

    private Map<String, Set<String>> rolePermissions;

    private Map<User, Stringset> userRoles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
