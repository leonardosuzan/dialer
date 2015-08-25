package dialer;

import org.springframework.data.repository.CrudRepository;

//@Transactional
public interface ListingDAO extends CrudRepository<Listing, Long> {

	public Listing findByName(String name);

}
