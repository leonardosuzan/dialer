package dialer;


public class Campaign {
	
	protected Campaign() {}


	private Integer idCampnha;
	private Integer idListagem;


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


	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
