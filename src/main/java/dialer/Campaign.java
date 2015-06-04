package dialer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Campanha")
public class Campaign {

	@Id
	@GeneratedValue
	private Integer idCampnha;

	@Id
	private Integer idListagem;

	@Column(name = "nome")
	private String name;

	public Integer getIdCampnha() {
		return idCampnha;
	}

	public void setIdCampnha(Integer idCampnha) {
		this.idCampnha = idCampnha;
	}

	public Integer getIdListagem() {
		return idListagem;
	}

	public void setIdListagem(Integer idListagem) {
		this.idListagem = idListagem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "descricao")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
