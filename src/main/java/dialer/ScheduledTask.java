package dialer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.crsh.console.jline.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
	
	public static final int MAXCHANNELS = 100;

	@Autowired
	AgendamentoDAO agendamentoDAO;

	@Autowired
	CampaignDAO campaignDAO;

	@Autowired
	JdbcTemplate jdbc;

	@Scheduled(fixedRate = 60000)
	public void checkSchedule() {
		Log.info("Verificando se há campanhas agendadas");

		List<Agendamento> a = agendamentoDAO.findAll();

		Iterator<Agendamento> i = a.iterator();

		while (i.hasNext()) {
			Agendamento t = i.next();
			if (t.getInicio().before(new Date()) && !t.isExec()) { // se a
																	// campanha
																	// está
																	// agendada
																	// para
																	// antes do
																	// tempo
																	// atual e
																	// ainda não
																	// foi
																	// executada
				
				Log.info("Ativando agendamento da campanha ID: "+t.getIdCampanha());
				exec(t);
			}

		}

		return;
	}

	private void exec(Agendamento a) {

		Campaign c = campaignDAO.findByidCampanha(a.getIdCampanha());

		a.setExec(true);
		agendamentoDAO.save(a);

		CallerHelper ast = new CallerHelper();

		ast.reload();

		Integer total = new Integer(0);

		total = getTotal(a);

		String queryString = "SELECT idContato,ddd,numeroTelefonico FROM dialer.contatosListagens WHERE idListagens="
				+ c.getIdListagem();
		List<Contato> contatos = jdbc.query(queryString, new RowMapper<Contato>() {
			public Contato mapRow(ResultSet rs, int rowNum) throws SQLException {
				Contato x = new Contato();
				
				x.setIdContato(rs.getInt(1));
				x.setCodigoArea(rs.getString(2));
				x.setNumeroTelefonico(rs.getString(3));
				
				return x;
			}
		});
		
		Log.info(contatos.size() + " Contatos carregados.");
		
		Iterator<Contato> i = contatos.iterator();
		Contato atual = null;

		while (total < a.getMeta() && i.hasNext()) {
			total = getTotal(a);
			atual = i.next();
			
			Log.info("Iniciando chamada para número: "+atual.getNumeroTelefonicoCompleto());
			
			ast.run(atual.getNumeroTelefonicoCompleto(), c.getIdCampanha().toString(), c, atual.getIdContato());
		}
		
		Log.info("Execução do agendamento finalizada");

		return;
	}
	
	private Integer getTotal (Agendamento a){
		return jdbc.queryForObject("select count(*) from disparosEfetuados where idCampanha = ? and `atendeu?` = 1",
				Integer.class, a.getIdCampanha());
	}

}
