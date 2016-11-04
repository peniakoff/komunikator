package pl.akademiakodu.server;

import java.util.ArrayList;
import java.util.List;

import pl.akademiakodu.server.commands.Command;
import pl.akademiakodu.server.commands.CommandSetAdmin;

public class CommandController {

	 private User ourUser; 
	 private List<Command> commands; 
 	  
	 public CommandController(User user){  
		 ourUser = user;
		 commands = new ArrayList<Command>();
		 init();
	 }
	 
	 private void init() { 
		 commands.add(new CommandSetAdmin());
	 }
	 
	 public boolean searchCommand(String msg) { 
		    if(!msg.startsWith("/")) return false; 
		    
		    for(Command cmd : commands){ 
		    	if(msg.contains(cmd.getName())){ 
		    		String[] args = msg.substring(cmd.getName().length()+2, msg.length()).split(" ");
		    		cmd.performAction(ourUser, args);
		    		return true;
		    	}
		    }
		 return false;
	 }
	 
	 
	 
}
