package tw.niq.api.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "niq_user")
public class User implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;

	@Column(updatable = false)
	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp lastModifiedDate;

	@Column(nullable = false, unique = true)
	private String userId;

	@Column(nullable = false, length = 50)
	private String username;

	@Column(nullable = false)
	private String password;

	@Builder.Default
	private Boolean accountNonExpired = true;

	@Builder.Default
	private Boolean accountNonLocked = true;

	@Builder.Default
	private Boolean credentialsNonExpired = true;

	@Builder.Default
	private Boolean enabled = true;

	@Singular
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToMany(
			cascade = { CascadeType.MERGE, CascadeType.PERSIST }, 
			fetch = FetchType.EAGER)
	@JoinTable(
			name = "niq_users_roles", 
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles;

	public User addRole(Role role) {
		if (role != null) {
			this.roles.add(role);
			role.getUsers().add(this);
		}
		return this;
	}

	public User removeRole(Role role) {
		if (role != null && this.roles.contains(role)) {
			this.roles.remove(role);
			if (role.getUsers().contains(this)) {
				role.getUsers().remove(this);
			}
		}
		return this;
	}

	@Transient
	public Set<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		if (this.roles != null && this.roles.size() > 0) {

			// Adding roles
			grantedAuthorities.addAll(this.roles.stream()
					.map(Role::getName)
					.map(role -> "ROLE_" + role)
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet()));

			Set<Authority> authorities = this.roles.stream()
					.map(Role::getAuthorities)
					.flatMap(Set::stream)
					.collect(Collectors.toSet());

			// Adding authorities
			if (authorities != null && authorities.size() > 0) {
				grantedAuthorities.addAll(authorities.stream()
						.map(Authority::getName)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet()));
			}
		}

		return grantedAuthorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

}
