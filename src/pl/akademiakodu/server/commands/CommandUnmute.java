package pl.akademiakodu.server.commands;

import pl.akademiakodu.server.Server;
import pl.akademiakodu.server.User;
import pl.akademiakodu.server.User.AccountType;

public class CommandUnmute implements Command {

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
		
		otherUser.setMute(false);
		otherUser.sendMessage("<b>Zosta³eœ odmutowany!</b>");
		executer.sendMessage("<b> Odmutowa³eœ " + otherUser.getFullName() + ".</b>");
	}

	@Override
	public String getName() {
		return "unmute";
	}

	@Override
	public String getInfo() {
		return "Wpisz /unmute nick - aby kogos zmutowac";
	}

}
