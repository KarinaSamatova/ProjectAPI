package models.forGames;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Requirements{
	@JsonProperty("hardDrive")
	private Integer hardDrive;
	@JsonProperty("osName")
	private String osName;
	@JsonProperty("ramGb")
	private Integer ramGb;
	@JsonProperty("videoCard")
	private String videoCard;
}