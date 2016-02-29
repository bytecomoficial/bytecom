package br.com.clairtonluz.sicoba.model.entity.security;

import br.com.clairtonluz.sicoba.model.entity.extra.EntityGeneric;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author clairton
 */
@Entity
@Table(name = "users")
public class User extends EntityGeneric {

    @NotNull(message = "nome é obrigatório")
    private String name;
    @NotNull(message = "username é obrigatório")
    private String username;
    @NotNull(message = "password é obrigatório")
    private String password;

    private Boolean enabled;

    public User() {
        enabled = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
