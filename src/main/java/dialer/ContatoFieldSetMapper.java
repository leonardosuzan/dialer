package dialer;

import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ContatoFieldSetMapper implements FieldSetMapper<Contato> {

	@Override
	public Contato mapFieldSet(FieldSet arg0) throws BindException {
		
		Contato contato = new Contato();
		
		contato.setPrimeiroNome(arg0.readString(0));
		contato.setSobrenome(arg0.readString(1));
		contato.setCodigoArea(arg0.readString(2));
		contato.setNumeroTelefonico(arg0.readString(3));
		contato.setEndereco(arg0.readString(6));
		contato.setNumeroEndereco(arg0.readString(7));
		contato.setComplemento(arg0.readString(8));
		contato.setBairro(arg0.readString(9));
		contato.setCidade(arg0.readString(4));
		contato.setEstado(arg0.readString(5));
		contato.setObservacoes(arg0.readString(10));
		
		
		
		
		return contato;
	}
	
}
