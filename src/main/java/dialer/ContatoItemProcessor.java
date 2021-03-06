package dialer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class ContatoItemProcessor implements ItemProcessor<Contato, Contato> {
	
	private static final Logger log = LoggerFactory.getLogger(ContatoItemProcessor.class);

	@Override
	public Contato process(Contato contato) throws Exception {
//		final String firstName = contato.getFirstName().toUpperCase();
//        final String lastName = contato.getLastName().toUpperCase();
//
//        final contato transformedPerson = new contato(firstName, lastName);
		
		
		if(contato.getNumeroTelefonico().isEmpty()){
			log.info("Ignorando (nulo) (" + contato + ")");
			return null;
		}
		
		
		try{
			Double.parseDouble(contato.getNumeroTelefonico());
		} catch(NumberFormatException e){
			log.info("Ignorando (não numérico) (" + contato + ")");
			return null;
		}

        log.info("Processando (" + contato + ")");

        return contato;
	}

}
