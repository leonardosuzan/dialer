package dialer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Campanha")
public class Campaign {

	protected Campaign() {
	}
	
	public Campaign(String name){
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idCampanha;

	private Long idListagem;
	
	private String description;
    
	@NotNull
	private String name;

	public Integer getIdCampnha() {
		return idCampanha;
	}

	public void setIdCampnha(Integer idCampnha) {
		this.idCampanha = idCampnha;
	}

	@Override
	public String toString() {
		return "Campaign [idCampanha=" + idCampanha + ", idListagem="
				+ idListagem + ", description=" + description + ", name="
				+ name + "]";
	}

	public Long getIdListagem() {
		return idListagem;
	}

	public void setIdListagem(Long idListagem) {
		this.idListagem = idListagem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

}
