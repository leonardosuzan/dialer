package dialer;

import java.io.Serializable;


	
	public  class Action implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3593090753606690277L;
		private String tipo;
		private String var;
		
		@Override
		public String toString() {
			return "Action [tipo=" + tipo + ", var=" + var + "]";
		}

		public Action(){
			tipo = null;
			var = null;
			
		}
		
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public String getVar() {
			return var;
		}
		public void setVar(String var) {
			this.var = var;
		}
	}
