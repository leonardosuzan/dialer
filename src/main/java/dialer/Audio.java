package dialer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Audio {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idAudio;

	private String dir;
	private String nome;

	@Override
	public String toString() {
		return "Audio [idAudio=" + idAudio + ", dir=" + dir + ", nome=" + nome
				+ "]";
	}

	public Long getIdAudio() {
		return idAudio;
	}

	public void setIdAudio(Long idAudio) {
		this.idAudio = idAudio;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
