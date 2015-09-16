package dialer;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AgendamentoDAO extends CrudRepository<Agendamento, Long> {
	
	public List<Agendamento> findByIdCampanha(Long idCampanha);

}
