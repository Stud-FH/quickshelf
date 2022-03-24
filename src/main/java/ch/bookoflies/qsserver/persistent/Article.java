package ch.bookoflies.qsserver.persistent;

import ch.bookoflies.qsserver.util.AccessScope;
import ch.bookoflies.qsserver.constant.Permission;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "article")
public class Article implements AccessScope {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Library library;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(nullable = false)
    private Long timestamp = System.currentTimeMillis(); // TODO version control

    @OneToOne
    private LongText text;

    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Note> notes;

    @ElementCollection
    private final Map<String, String> attributes = new HashMap<>();

    @ElementCollection
    private final Set<Permission> defaultPermissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private final Map<String, Stringset> rolePermissions = new HashMap<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private final Map<User, Stringset> userRoles = new HashMap<>();


    @Override
    public AccessScope parentScope() {
        return library;
    }

    @Override
    public User scopeOwner() {
        return author;
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
