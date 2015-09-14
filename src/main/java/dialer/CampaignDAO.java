package dialer;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

//@Transactional
public interface CampaignDAO extends CrudRepository<Campaign, Long> {

	public List<Campaign> findByName(String name);
	public Campaign findByidCampanha(Integer id);
	public List<Campaign> findAll();
	

}
