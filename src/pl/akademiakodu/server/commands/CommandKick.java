package pl.akademiakodu.server.commands;

import pl.akademiakodu.server.Server;
import pl.akademiakodu.server.User;
import pl.akademiakodu.server.User.AccountType;

public class CommandKick implements Command {

	@Override
	public boolean requireAdmin() {
		return true;
	}

	@Override
	public void performAction(User executer, String... args) {
		
		if (args.length != 1) {
			executer.sendMessage(getInfo());
			return;
		}
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
		

		otherUser.kick("<b> Zosta³eœ wyrzucony z chatu przez " + executer.getFullName()+ ".</b>");
		executer.sendMessage("<b> Wyrzuci³eœ " + args[0] + " z czatu.</b>");
	
	}

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public String getInfo() {
		return "Wpisz /kick nick - aby kogos wyrzuciæ";
	}

}
