package tw.niq.api.domain;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Builder
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "niq_role")
public class Role {

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
	
	@Column(nullable = false, length = 50, unique = true)
	private String name;
	
	@Singular
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToMany(
			cascade = { CascadeType.MERGE, CascadeType.PERSIST }, 
			fetch = FetchType.EAGER)
	@JoinTable(
			name = "niq_roles_authorities", 
			joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private Set<Authority> authorities;
	
	public Role addAuthority(Authority authority) {
		if (authority != null) {
			this.authorities.add(authority);
			authority.getRoles().add(this);
		}
		return this;
	}

	public Role removeAuthority(Authority authority) {
		if (authority != null && this.authorities.contains(authority)) {
			this.authorities.remove(authority);
			if (authority.getRoles().contains(this)) {
				authority.getRoles().remove(this);
			}
		}
		return this;
	}
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToMany(mappedBy = "roles")
	private Set<User> users;
	
}
