package dialer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Listing {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idListing;

	private String name;

	@Override
	public String toString() {
		return "Listing [id_listing=" + idListing + ", name=" + name
				+ ", description=" + description + "]";
	}

	private String description;

	public Long getIdListing() {
		return idListing;
	}

	public void setIdListing(Long id_listing) {
		this.idListing = id_listing;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
