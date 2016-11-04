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
				executer.sendMessage("Musisz by� administratorem, aby wywo�a� t� funckj�");
				return;
			}
		}

		User otherUser = Server.getUserByName(args[0]);

		if (otherUser == null) {
			executer.sendMessage("Taki u�ytkownik nie istnieje!");
			return;
		}
		otherUser.setAccountType(AccountType.ADMIN);
		otherUser.sendMessage("<b>Zosta�e� administratorem!</b>");

	}

	@Override
	public String getName() {
		return "setadmin";
	}

	@Override
	public String getInfo() {
		return "Aby komenda zadzia�a�a u�yj : /setadmin <nick>";
	}

}
