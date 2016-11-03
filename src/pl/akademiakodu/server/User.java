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
	
	
	public User(Socket localSocket) { 
		userSocket = localSocket;
		accountType = AccountType.USER;
		
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
	
	}
	
	public void sendMessage(String msg) {
		if(msg == null) throw new NullPointerException();
		printWriter.println(msg);
	}
	
	private String getMessage() { 
		 try {
			return buffReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
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
		return !getSocket().isClosed() && getSocket().isConnected();
	}
	
	public void disconnect() { 
		if(Server.getUserList().contains(this)) {
			Server.getUserList().remove(this);
		}
		try {
			getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
	     while(true) { 
	    	  String input = getMessage();
	    	  // Pos³anie wiadomoœci do wszystkich aktualnie online.
	    	    if(input != null) { 
	    	    	for(User user : Server.getUserList()){ 
	    	    		user.sendMessage(input);
	    	    	}
	    	    }
	     }
		
	}
	
	public enum AccountType { 
		ADMIN(), USER();
	}

}

