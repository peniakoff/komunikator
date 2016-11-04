package pl.akademiakodu.server.commands;

import pl.akademiakodu.server.Server;
import pl.akademiakodu.server.User;
import pl.akademiakodu.server.User.AccountType;

public class CommandSetAdmin implements Command {

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
		otherUser.setAccountType(AccountType.ADMIN);
		otherUser.sendMessage("<b>Zosta³eœ administratorem!</b>");

	}

	@Override
	public String getName() {
		return "setadmin";
	}

	@Override
	public String getInfo() {
		return "Aby komenda zadzia³a³a u¿yj : /setadmin <nick>";
	}

}
