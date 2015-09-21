package dialer;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

//@Transactional
public interface ListingDAO extends CrudRepository<Listing, Long> {

	public Listing findByName(String name);
	public Campaign findByidListing(Long id);
	public List<Listing> findAll();

}
