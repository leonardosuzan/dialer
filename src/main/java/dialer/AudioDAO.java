package dialer;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AudioDAO extends CrudRepository <Audio, Long>{
	
	public List<Audio> findByIdAudio(Long idAudio);
	public List<Audio> findByNome(String nome);
	

}
