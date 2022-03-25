package ch.bookoflies.qsserver.persistent;

import ch.bookoflies.qsserver.util.AccessScope;
import ch.bookoflies.qsserver.constant.Permission;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "library")
public class Library implements AccessScope {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @OneToMany(fetch = FetchType.LAZY)
    private final Set<Article> articles = new HashSet<>();

    @ElementCollection
    private final Set<Permission> defaultPermissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private final Map<String, Stringset> rolePermissions = new HashMap<>();

    @OneToMany(cascade = CascadeType.ALL)
    private final Map<User, Stringset> userRoles = new HashMap<>();

    @ElementCollection
    private final Map<User, String> userAliases = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Collection<Article> getArticles() {
        return articles;
    }

    @Override
    public AccessScope parentScope() {
        return null;
    }

    @Override
    public User scopeOwner() {
        return owner;
    }

    @Override
    public Stream<String> getRoles() {
        return userRoles.values().stream().flatMap(Set::stream);
    }

    @Override
    public Stream<String> getRoles(User user) {
        return userRoles.get(user).stream();
    }

    @Override
    public void putRole(User user, String role, boolean b) {
        if (b) {
            userRoles.get(user).add(role);
        } else {
            userRoles.get(user).remove(role);
        }
    }

    @Override
    public Stream<Permission> getDefaultPermissions() {
        return defaultPermissions.stream();
    }

    @Override
    public Stream<Permission> getPermissions(String role) {
        return rolePermissions.get(role).stream().map(Permission::valueOf);
    }

    @Override
    public void putDefaultPermission(Permission permission, boolean b) {
        if (b) {
            defaultPermissions.add(permission);
        } else {
            defaultPermissions.remove(permission);
        }
    }

    @Override
    public void putPermission(String role, Permission permission, boolean b) {
        if (b) {
            rolePermissions.get(role).add(permission.name());
        } else {
            rolePermissions.get(role).remove(permission.name());
        }
    }
}
