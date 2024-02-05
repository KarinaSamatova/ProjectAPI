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
public class SimilarDlc{
	@JsonProperty("dlcNameFromAnotherGame")
	private String dlcNameFromAnotherGame;
	@JsonProperty("isFree")
	private Boolean isFree;
}