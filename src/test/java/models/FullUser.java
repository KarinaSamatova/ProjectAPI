package models;

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
//Эта аннотация позволяет игнорировать все, что имеет значение null при создании экземпляра этого класса (соот-но при отправке тела запроса)
// т.е. при использовании билдера (когда заполняем не все поля класса)
@JsonIgnoreProperties(ignoreUnknown = true)
//Эта аннотация позволяет игнорировать поля из json-ответа, не содержащиеся в данном классе
public class FullUser{
	@JsonProperty("login")
	private String login;
	@JsonProperty("pass")
	private String pass;

	@JsonProperty("games")
	private List<GamesItem> games;
}