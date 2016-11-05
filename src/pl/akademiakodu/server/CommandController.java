package pl.akademiakodu.server;

import java.util.ArrayList;
import java.util.List;

import pl.akademiakodu.server.commands.Command;
import pl.akademiakodu.server.commands.CommandKick;
import pl.akademiakodu.server.commands.CommandMute;
import pl.akademiakodu.server.commands.CommandSend;
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
		commands.add(new CommandSend());
	}

	public boolean searchCommand(String msg) {
		if (!msg.startsWith("/"))
			return false;

		for (Command cmd : commands) {
			           // /komenda <arg0> <arg1>                            /komenda
			if (msg.matches("/" + cmd.getName() + " " + ".*") || msg.matches("/" + cmd.getName())) {
				// TODO obs³uga bez argumentów
				// /setadmin
				if (msg.length() == cmd.getName().length() + 1) {
					cmd.performAction(ourUser, new String[] {});

				} else {
					// /komenda <arg0> <arg1> <arg2> ... <argN> /setadmin Oskar
					System.out.println("debug");
					String[] args = msg.substring(cmd.getName().length() + 2, msg.length()).split(" ");
					cmd.performAction(ourUser, args);

				}
				return true;
			}
		}
		return false;
	}

}
