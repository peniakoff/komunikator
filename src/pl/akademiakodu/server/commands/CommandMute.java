package pl.akademiakodu.server.commands;

import pl.akademiakodu.server.Server;
import pl.akademiakodu.server.User;
import pl.akademiakodu.server.User.AccountType;

public class CommandMute implements Command {

	@Override
	public boolean requireAdmin() {
		return true;
	}

	@Override
	public void performAction(User executer, String... args) {

		// if (args.length > 0) {
		// executer.sendMessage(getInfo());
		// return;
		// }

		if (requireAdmin()) {
			if (executer.getAccountType() != AccountType.ADMIN) {
				executer.sendMessage("Musisz byæ administratorem, aby wywo³aæ t¹ funckjê");
				return;
			}
		}

		for (String user : args) {
			User otherUser = Server.getUserByName(user);

			if (otherUser == null) {
				executer.sendMessage("Taki u¿ytkownik nie istnieje!");
				continue;
			}

			if (executer.getNickname().equals(otherUser.getNickname())) {
				executer.sendMessage("Nie mo¿esz wykonaæ tej komendy na samym sobie!");
				continue;
			}

			if (otherUser.isMuted()) {
				executer.sendMessage("Ten u¿ytkownik ma ju¿ mute!");
				continue;
			}
			otherUser.setMute(true);
			otherUser.sendMessage("<b>Zosta³eœ zmutowany!</b>");
			executer.sendMessage("<b> Zmutowa³eœ " + otherUser.getFullName() + ".</b>");

		}
	}

	@Override
	public String getName() {
		return "mute";
	}

	@Override
	public String getInfo() {
		return "Wpisz /mute nick nick nick nick ... - aby kogos zmutowac";
	}

}
