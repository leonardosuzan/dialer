package dialer;

import java.io.Serializable;



public class Contato implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Integer idContato;
	
	private Integer idListagens;
	
	
	private String numeroTelefonico;
	private String codigoArea;
	private String primeiroNome;
	private String sobrenome;
	private String endereco;
	private String numeroEndereco;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String observacoes;

	@Override
	public String toString() {
		return "Contato [numeroTelefonico=" + numeroTelefonico
				+ ", codigoArea=" + codigoArea + ", primeiroNome="
				+ primeiroNome + ", sobrenome=" + sobrenome + ", endereco="
				+ endereco + ", numeroEndereco=" + numeroEndereco
				+ ", complemento=" + complemento + ", bairro=" + bairro
				+ ", cidade=" + cidade + ", estado=" + estado
				+ ", observacoes=" + observacoes + "]";
	}

	public String getNumeroTelefonico() {
		return numeroTelefonico;
	}

	public void setNumeroTelefonico(String numeroTelefonico) {
		this.numeroTelefonico = numeroTelefonico;
	}

	public String getCodigoArea() {
		return codigoArea;
	}

	public void setCodigoArea(String codigoArea) {
		this.codigoArea = codigoArea;
	}

	public String getPrimeiroNome() {
		return primeiroNome;
	}

	public void setPrimeiroNome(String primeiroNome) {
		this.primeiroNome = primeiroNome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumeroEndereco() {
		return numeroEndereco;
	}

	public void setNumeroEndereco(String numeroEndereco) {
		this.numeroEndereco = numeroEndereco;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Integer getIdContato() {
		return idContato;
	}

	public void setIdContato(Integer idContato) {
		this.idContato = idContato;
	}

	public Integer getIdListagens() {
		return idListagens;
	}

	public void setIdListagens(Integer idListagens) {
		this.idListagens = idListagens;
	}
	
	public String getNumeroTelefonicoCompleto(){
		return "0" + this.codigoArea + this.numeroTelefonico;
	}

}
