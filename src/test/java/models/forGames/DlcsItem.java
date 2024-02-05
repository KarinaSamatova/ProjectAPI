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
public class DlcsItem{
	@JsonProperty("isDlcFree")
	private Boolean isDlcFree;
	@JsonProperty("dlcName")
	private String dlcName;
	@JsonProperty("rating")
	private Integer rating;
	@JsonProperty("description")
	private String description;
	@JsonProperty("price")
	private Integer price;
	@JsonProperty("similarDlc")
	private SimilarDlc similarDlc;
}