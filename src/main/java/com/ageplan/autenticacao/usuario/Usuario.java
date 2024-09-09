package com.ageplan.autenticacao.usuario;

import com.ageplan.autenticacao.papel.Papel;
import com.ageplan.autenticacao.pessoa.Pessoa;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entidade que representa um usuário no sistema.
 * Extende a classe {@link Pessoa} e implementa a interface {@link UserDetails} para integração com o Spring Security.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
public class Usuario extends Pessoa implements UserDetails {

    @NotBlank(message = "Nome de usuário é obrigatório")
    @Column(unique = true, nullable = false)
    private String nomeUsuario;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "usuario_papel",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "papel_id", referencedColumnName = "id"))
    private Set<Papel> papeis = new HashSet<>();

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Column(nullable = false)
    private boolean enabled = true;

    public void addPapel(Papel papel) {
        this.papeis.add(papel);
    }

    public void removePapel(Papel papel) {
        this.papeis.remove(papel);
    }

    public boolean hasPapel(Papel.NomePapel nomePapel) {
        return this.papeis.stream().anyMatch(papel -> papel.getNomePapel() == nomePapel);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.papeis.stream()
                .map(papel -> new SimpleGrantedAuthority(papel.getNomePapel().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.nomeUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + getId() +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", email='" + email + '\'' +
                ", papeis=" + papeis +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        if (!super.equals(o)) return false;

        return isAccountNonExpired() == usuario.isAccountNonExpired() &&
                isAccountNonLocked() == usuario.isAccountNonLocked() &&
                isCredentialsNonExpired() == usuario.isCredentialsNonExpired() &&
                isEnabled() == usuario.isEnabled() &&
                Objects.equals(getNomeUsuario(), usuario.getNomeUsuario()) &&
                Objects.equals(getEmail(), usuario.getEmail()) &&
                Objects.equals(getSenha(), usuario.getSenha()) &&
                Objects.equals(getPapeis(), usuario.getPapeis());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(getNomeUsuario());
        result = 31 * result + Objects.hashCode(getEmail());
        result = 31 * result + Objects.hashCode(getSenha());
        result = 31 * result + Objects.hashCode(getPapeis());
        result = 31 * result + Boolean.hashCode(isAccountNonExpired());
        result = 31 * result + Boolean.hashCode(isAccountNonLocked());
        result = 31 * result + Boolean.hashCode(isCredentialsNonExpired());
        result = 31 * result + Boolean.hashCode(isEnabled());
        return result;
    }
}