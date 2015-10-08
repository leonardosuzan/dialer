package dialer;

import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.crsh.console.jline.internal.Log;

public class CallerHelper {

	public static final String HOSTNAME = "192.168.40.129";
	public static final int PORT = 5038;
	public static final String USERNAME = "dialer";
	public static final String PASSWORD = "mt2015tcc";
	public static final String CONTEXT = "demo-tcc";

	private ManagerConnection managerConnection;

	public CallerHelper() {
		ManagerConnectionFactory factory = new ManagerConnectionFactory(HOSTNAME, PORT, USERNAME, PASSWORD);

		this.managerConnection = factory.createManagerConnection();
	}

	public String reload() {

		CommandAction commandAction = new CommandAction("dialplan reload");
		CommandResponse response;
		try {
			managerConnection.login();
			response = (CommandResponse) managerConnection.sendAction(commandAction);
			managerConnection.logoff();
		} catch (IllegalArgumentException | IllegalStateException | IOException | TimeoutException
				| AuthenticationFailedException e) {
			e.printStackTrace();
			return e.toString();

		}
		for (String line : response.getResult()) {
			Log.info(line);
		}

		return null;
	}

	public String run(String number, String context)
			throws IOException, AuthenticationFailedException, TimeoutException {

		OriginateAction originateAction;
		ManagerResponse originateResponse;

		originateAction = new OriginateAction();
		
		if(number.length() <=4){
			originateAction.setChannel("SIP/" + number);
		} else {
			originateAction.setChannel("SIP/minutostelecom/" + number);
		}

		originateAction.setContext(context);
		originateAction.setExten("s");
		originateAction.setPriority(1);

		originateAction.setCallerId("dialer.mt");
		originateAction.setTimeout(new Long(30000));
		
		originateAction.setVariable("dialingNumber", number);

		Log.info(originateAction.toString());

		/*
		 * Set variables that a script can access when the phone call is
		 * answered, such as a user or message ID, tracking code, or other state
		 */
		// originateAction.setVariables(new Hashtable(...))

		// connect to Asterisk and log in
		managerConnection.login();

		/*
		 * send the originate action and wait for a maximum of 30 seconds for
		 * Asterisk to send a reply in ms
		 */
		originateResponse = managerConnection.sendAction(originateAction, 30000);

		// print out whether the originate succeeded or not
		Log.info("Asterisk Response:" + originateResponse.getResponse());
		Log.info("Asterisk Message:" + originateResponse.getMessage());

		// and finally log off and disconnect
		managerConnection.logoff();

		return originateResponse.getResponse() + " : " + originateResponse.getMessage();

	}

}
