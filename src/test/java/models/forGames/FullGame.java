package models.forGames;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullGame{
	@JsonProperty("company")
	private String company;
	@JsonProperty("description")
	private String description;
	@JsonProperty("dlcs")
	private List<DlcsItem> dlcs;
	@JsonProperty("gameId")
	private Integer gameId;
	@JsonProperty("genre")
	private String genre;
	@JsonProperty("isFree")
	private Boolean isFree;
	@JsonProperty("price")
	private Integer price;
	@JsonProperty("publish_date")
	private String publishDate;
	@JsonProperty("rating")
	private Integer rating;
	@JsonProperty("requiredAge")
	private Boolean requiredAge;
	@JsonProperty("requirements")
	private Requirements requirements;
	@JsonProperty("tags")
	private List<String> tags;
	@JsonProperty("title")
	private String title;
}