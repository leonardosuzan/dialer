package dialer;

import java.io.Serializable;


	
	public  class Action implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3593090753606690277L;
		private int tipo;
		private String var;
		
		@Override
		public String toString() {
			return "Action [tipo=" + tipo + ", var=" + var + "]";
		}

		public Action(){
			tipo = -1;
			var = null;
			
		}
		
		public int getTipo() {
			return tipo;
		}
		public void setTipo(int tipo) {
			this.tipo = tipo;
		}
		public String getVar() {
			return var;
		}
		public void setVar(String var) {
			this.var = var;
		}
	}
