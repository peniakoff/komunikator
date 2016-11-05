package pl.akademiakodu.server.commands;

import pl.akademiakodu.server.Server;
import pl.akademiakodu.server.User;
import pl.akademiakodu.server.User.AccountType;

public class CommandSend implements Command{

	@Override
	public boolean requireAdmin() {
		return false;
	}

	@Override
	public void performAction(User executer, String... args) {
	 
	
		if (requireAdmin()) {
			if (executer.getAccountType() != AccountType.ADMIN) {
				executer.sendMessage("Musisz byæ administratorem, aby wywo³aæ t¹ funckjê");
				return;
			}
		}

		User otherUser = Server.getUserByName(args[0]);
		

		if (otherUser == null) {
			executer.sendMessage("Taki u¿ytkownik nie istnieje!");
			return;
		}


		
		StringBuilder builder = new StringBuilder(); 
		for(int i = 1; i < args.length; i++){ 
			builder.append(args[i]);
			builder.append(" ");
		}
		 otherUser.sendMessage(executer.getFullName() + " <b style='color:red;'> > </b> " + builder.toString());
		 executer.sendMessage(executer.getFullName() + " <b style='color:red;'> > </b> " + builder.toString());
		// otherUser.sendMessage("Aby odpowiedzieæ, u¿yj /send " + executer.getNickname() + " 'tresc wiadomosci'");
		
	}

	@Override
	public String getName() {
		return "send";
	}

	@Override
	public String getInfo() {
		return "Wpisz w czat /send nick 'a tutaj tresc wiadomosci'";
	}

}
