package dialer;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Agendamento {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idAgendamento;
	
	private Date inicio;
	private Date fim;
	
	private Long meta;
	
	private Long idCampanha;

	public Long getIdAgendamento() {
		return idAgendamento;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public Long getMeta() {
		return meta;
	}

	public void setMeta(Long meta) {
		this.meta = meta;
	}

	public Long getIdCampanha() {
		return idCampanha;
	}

	public void setIdCampanha(Long idCampanha) {
		this.idCampanha = idCampanha;
	}

	@Override
	public String toString() {
		return "Agendamento [idAgendamento=" + idAgendamento + ", inicio="
				+ inicio + ", fim=" + fim + ", meta=" + meta + ", idCampanha="
				+ idCampanha + "]";
	}
	

}
