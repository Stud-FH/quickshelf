package ch.bookoflies.qsserver.util;

import ch.bookoflies.qsserver.constant.Permission;
import ch.bookoflies.qsserver.persistent.User;

import java.util.stream.Stream;

public interface AccessScope {

    AccessScope parentScope();

    User scopeOwner();

    Stream<String> getRoles();
    Stream<String> getRoles(User user);

    void putRole(User user, String role, boolean b);

    Stream<Permission> getDefaultPermissions();
    Stream<Permission> getPermissions(String role);

    void putDefaultPermission(Permission permission, boolean b);
    void putPermission(String role, Permission permission, boolean b);

    default boolean allows(User user, Permission permission) {
        if (user == scopeOwner()) return true;
        if (getDefaultPermissions().anyMatch(p -> p == permission)) return true;
        if (getRoles(user).anyMatch(r -> getPermissions(r).anyMatch(p -> p == permission))) return true;
        AccessScope parent = parentScope();
        return parent != null && parent.allows(user, permission);
    }

}
