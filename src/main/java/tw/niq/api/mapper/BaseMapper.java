package tw.niq.api.mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface BaseMapper {
	
	default Timestamp map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }
	
	default LocalDateTime map(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
	
}
