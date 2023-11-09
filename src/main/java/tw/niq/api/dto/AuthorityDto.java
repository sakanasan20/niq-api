package tw.niq.api.dto;

import java.time.LocalDateTime;

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
public class AuthorityDto {
	
	private Long id;
	
	private Long version;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime lastModifiedDate;
	
	private String name;

}
