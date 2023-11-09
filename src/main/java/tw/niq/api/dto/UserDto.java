package tw.niq.api.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDto {
	
	private Long id;
	
	private Long version;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime lastModifiedDate;
	
	private String userId;
	
	private String username;
	
	private String password;

	private Boolean accountNonExpired;

	private Boolean accountNonLocked;

	private Boolean credentialsNonExpired;

	private Boolean enabled;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<RoleDto> roles;
	
}
