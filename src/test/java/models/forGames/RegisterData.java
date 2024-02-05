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
public class RegisterData{

	@JsonProperty("gameId")
	private Integer gameId;
	@JsonProperty("title")
	private String title;
	@JsonProperty("genre")
	private String genre;
	@JsonProperty("requiredAge")
	private Boolean requiredAge;
	@JsonProperty("isFree")
	private Boolean isFree;
	@JsonProperty("price")
	private Integer price;
	@JsonProperty("company")
	private String company;
	@JsonProperty("publish_date")
	private String publishDate;
	@JsonProperty("rating")
	private Integer rating;
	@JsonProperty("description")
	private String description;
	@JsonProperty("tags")
	private List<String> tags;
	@JsonProperty("dlcs")
	private List<DlcsItem> dlcs;
	@JsonProperty("requirements")
	private Requirements requirements;
}