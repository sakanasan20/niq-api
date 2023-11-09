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
public class RoleDto {
	
	private Long id;
	
	private Long version;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime lastModifiedDate;
	
	private String name;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<AuthorityDto> authorities;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<UserDto> users;
	
}
