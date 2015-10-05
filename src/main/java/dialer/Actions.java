package dialer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Actions implements Serializable {
	
	/**
	 * 
	 */
	
	private class Opcao implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1847016641666304476L;
		private int id;
		private String nome;
		
		public Opcao(int i, String n){
			id = i;
			nome = n;
		}

		@SuppressWarnings("unused")
		public int getId() {
			return id;
		}

		@SuppressWarnings("unused")
		public String getNome() {
			return nome;
		}
		
		
		
	}
	
	private static final long serialVersionUID = 6808015566074110726L;
	private List<Action> l;
	private List<Opcao> o;
	
	public Actions(){
		l = new ArrayList<Action>();
		
		o = new ArrayList<Opcao>();
		
		
		
		o.add(new Opcao(0, "Atender"));
		o.add(new Opcao(1, "Desligar"));
		o.add(new Opcao(2, "Coletar resposta"));
		o.add(new Opcao(3, "Coletar resposta com reconhecimento de voz"));
		o.add(new Opcao(4, "Transferir"));
		o.add(new Opcao(5, "Transferir Se"));
		o.add(new Opcao(6, "Reproduzir audio"));
		
		
	}
	
	public void deleteAcion(int index){

		l.remove(index);
		return;
		
	}
	
	@Override
	public String toString() {
		return "Actions [l=" + l + "]";
	}

	public List<Action> getL() {
		return l;
	}

	public void setL(List<Action> l) {
		this.l = l;
	}

	public void addAction(Action a){
		l.add(a);
	}
	
	public List<Opcao> getO(){
		return o;
	}

}
