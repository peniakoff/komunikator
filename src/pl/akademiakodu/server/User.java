package pl.akademiakodu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User implements Runnable {

	public static final int maxWarnings = 3;

	private Socket userSocket;
	private String nickname;
	private AccountType accountType;
	private String postfix;
	private int warnings;
	private boolean isMuted;
	private PrintWriter printWriter;
	private BufferedReader buffReader;
	private CommandController commandController;

	public User(Socket localSocket) {
		userSocket = localSocket;
		accountType = AccountType.ADMIN;
		nickname = "";

		init();
	}

	private void init() {
		try {
			printWriter = new PrintWriter(getSocket().getOutputStream(), true);
			buffReader = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
		} catch (IOException e) {
			System.out.println("B��d inicjalizacji streamu");
			e.printStackTrace();
		}
		commandController = new CommandController(this);
	}

	public void sendMessage(String msg) {
		if (msg == null)
			throw new NullPointerException();
		printWriter.println(msg);
	}

	private String getMessage() {
		try {
			return buffReader.readLine();
		} catch (IOException e) {
			disconnect();
			return null;
		}
	}

	public boolean canWrite() {
		return !isMuted() && getWarningsCount() < maxWarnings;
	}

	public void setMute(boolean mute) {
		isMuted = mute;
	}

	public boolean isMuted() {
		return isMuted;
	}

	public void addWarnings() {
		warnings++;
	}

	public int getWarningsCount() {
		return warnings;
	}

	public String getFullName() {
		if (postfix != null)
			return nickname + " (" + postfix + ")";
		return nickname;
	}

	public void setPrefix(String prefix) {
		this.postfix = prefix;
	}

	public String getPrefix() {
		return postfix;
	}

	public void setAccountType(AccountType type) {
		accountType = type;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public Socket getSocket() {
		return userSocket;
	}

	public boolean isConnected() {
		return !getSocket().isClosed();
	}

	public void kick(String msg) {
		sendMessage(msg);
		try {
			getSocket().close();
		} catch (IOException e) {
			System.out.println("B��d przy kicku u�ytkownika");
		}
	}

	public void disconnect() {
		if (Server.getUserList().contains(this)) {
			Server.getUserList().remove(this);
		}

		sendMessageToAll("<b>U�ytownik " + getFullName() + " opu�ci� czat.</b>");

		try {
			getSocket().close();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("Socket by� ju� wcze�niej zamkni�ty!");
		}
	}

	private String getCurrentTime() {
		return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	@Override
	public void run() {
		sendMessage("Pierwsza Twoja wiadomo��, zostanie ustawiona jako nick.");
		
		while (true) {
			 String nick = getMessage();
		     boolean localNickIsCorrect = true;
			 for(User u : Server.getUserList()) { 
				 if(u.getNickname().equals(nick)){
					 localNickIsCorrect = false;
					 break;
				 }
			 }
			 
			 if(!localNickIsCorrect) { 
				 sendMessage("Ten nick jest zaj�ty, wpisz inny");
			 }else{
				 setNickname(nick);
				 break;
			 }

		}

		sendMessage("Ustawili�my Tw�j nick na " + getNickname());
		
		while (true) {
			if (!isConnected()) {
				break;
			}

			String input = getMessage();
			if (input != null) {
				if (commandController.searchCommand(input))
					continue;
				if (isMuted()) {
					sendMessage("Nie mo�esz pisa�, masz mute!");
					continue;
				}

				sendMessageToAll("(" + getCurrentTime() + ") " + getFullName() + ": " + input);
			}
		}

	}

	private void sendMessageToAll(String msg) {
		for (User user : Server.getUserList()) {
			user.sendMessage(msg);
		}
	}

	public enum AccountType {
		ADMIN(), USER();
	}

}
