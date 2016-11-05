package pl.akademiakodu.server;

import java.util.ArrayList;
import java.util.List;

import pl.akademiakodu.server.commands.Command;
import pl.akademiakodu.server.commands.CommandKick;
import pl.akademiakodu.server.commands.CommandMute;
import pl.akademiakodu.server.commands.CommandSetAdmin;
import pl.akademiakodu.server.commands.CommandUnmute;

public class CommandController {

	private User ourUser;
	private List<Command> commands;

	public CommandController(User user) {
		ourUser = user;
		commands = new ArrayList<Command>();
		init();
	}

	private void init() {
		commands.add(new CommandSetAdmin());
		commands.add(new CommandMute());
		commands.add(new CommandUnmute());
		commands.add(new CommandKick());
	}

	public boolean searchCommand(String msg) {
		if (!msg.startsWith("/"))
			return false;

		for (Command cmd : commands) {
			// /komenda <arg1> <arg2> /komenda
			if (msg.matches("/" + cmd.getName() + " " + ".*") || msg.matches("/" + cmd.getName())) {
				// TODO obs�uga bez argument�w
				// /setadmin
				if (msg.length() == cmd.getName().length() + 1) {
					cmd.performAction(ourUser, new String[] {});

				} else {
					// /komenda <arg1> <arg2> <arg3> ... <argN> /setadmin Oskar
					String[] args = msg.substring(cmd.getName().length() + 2, msg.length()).split(" ");
					cmd.performAction(ourUser, args);

				}
				return true;
			}
		}
		return false;
	}

}
