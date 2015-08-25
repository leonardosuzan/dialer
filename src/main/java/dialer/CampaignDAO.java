package dialer;

import org.springframework.data.repository.CrudRepository;

//@Transactional
public interface CampaignDAO extends CrudRepository<Campaign, Long> {

	public Campaign findByName(String name);

}
