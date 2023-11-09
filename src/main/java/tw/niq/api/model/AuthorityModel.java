package tw.niq.api.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class AuthorityModel {
	
	@JsonIgnore
	private Long id;
	
	private Long version;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime lastModifiedDate;
	
	@NotBlank
	private String name;

}
