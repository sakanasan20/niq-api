package tw.niq.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class UserCreateModel {
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	@Builder.Default
	private Boolean accountNonExpired = true;

	@Builder.Default
	private Boolean accountNonLocked = true;

	@Builder.Default
	private Boolean credentialsNonExpired = true;

	@Builder.Default
	private Boolean enabled = true;

}
