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
				executer.sendMessage("Musisz by� administratorem, aby wywo�a� t� funckj�");
				return;
			}
		}

		User otherUser = Server.getUserByName(args[0]);
		

		if (otherUser == null) {
			executer.sendMessage("Taki u�ytkownik nie istnieje!");
			return;
		}
		
		otherUser.setMute(false);
		otherUser.sendMessage("<b>Zosta�e� odmutowany!</b>");
		executer.sendMessage("<b> Odmutowa�e� " + otherUser.getFullName() + ".</b>");
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
