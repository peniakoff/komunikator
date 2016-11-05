package pl.akademiakodu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
		
		init();
	}
	
	private void init(){ 
		try {
			printWriter = new PrintWriter(getSocket().getOutputStream(), true);
			buffReader = new BufferedReader(
					new InputStreamReader(getSocket().getInputStream()));
		} catch (IOException e) {
			System.out.println("B³¹d inicjalizacji streamu");
			e.printStackTrace();
		}
		commandController  = new CommandController(this);
	}
	
	public void sendMessage(String msg) {
		if(msg == null) throw new NullPointerException();
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
	
	public void setMute(boolean mute){ 
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
		if(postfix != null) return nickname + " (" + postfix + ")";
		return nickname;
	}
	
	public void setPrefix(String prefix) { 
		 this.postfix = prefix;
	}
	
	public String getPrefix(){ 
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
	
	public Socket getSocket()  { 
		return userSocket;
	}
	
	public boolean isConnected() { 
		return  !getSocket().isClosed();
	}
	
	public void disconnect() { 
		if(Server.getUserList().contains(this)) {
			Server.getUserList().remove(this);
		}
		
		sendMessageToAll("<b>U¿ytownik " + getFullName() + " opuœci³ czat.</b>");
		
	
		
		try {
			getSocket().close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Socket by³ ju¿ wczeœniej zamkniêty!");
		}
	}
	
	
	
	@Override
	public void run() {
		
		if(getNickname() == null) { 
    		 sendMessage("Pierwsza Twoja wiadomoœæ, zostanie ustawiona jako nick.");
    		 setNickname(getMessage());
    		 sendMessage("Twój nick od teraz to: " + getNickname());
    	}
    	
		
	     while(true) { 
	    	  if(!isConnected()) {  break; }
	    	  
	    	
	    	  
	    	  String input = getMessage();
	    	    if(input != null) { 
	    	    	if(commandController.searchCommand(input)) continue; 
	    	    	
	    	    	sendMessageToAll(getFullName() + ": " + input);
	    	    }
	     }
		
	}
	
	private void sendMessageToAll(String msg) {
		for(User user : Server.getUserList()){ 
    		user.sendMessage(msg);
    	}
	}
	
	public enum AccountType { 
		ADMIN(), USER();
	}

}

