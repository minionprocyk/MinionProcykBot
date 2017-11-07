# MinionProcykBot
A solely java implementation of a twitch bot piggybacking off of an irc tool called pircbot

The jar file is not already built. You will need to build this yourself. Add the pircbot-ppf.jar file to your classpath, build the jar, then
you can use the following to execute the program.
This application accepts a list of parameters for use:

1. IRCServerName - The name of the server you are connecting to
2. PORT - The port to use for the server you are trying to connect to
3. OAUTH - The oauth token that your server may request when connecting to the irc
4. COMMANDS_FILE - The file that contains the list of commands the bot has stored
5. MODS_FILE - The list of known mods
6. QUOTES_FILE - Currently not in use, can be separate from the COMMANDS_FILE
7. CHANNEL - The name of the channel to connect to on the server
8. BOT_NAME - The name that this program should alias itself as on the irc server

A typical usage of this on a linux machine would look like this:

java -jar /home/minionprocyk/projects/minionprocykbot/minionprocykbot.jar 
"irc.twitch.tv" "6667" "oauth:wubbalubadubdub" 
"/var/lib/minionprocykbot/Commands.txt" "/var/lib/minionprocykbot/Mods.txt" "/var/lib/minionprocykbot/Quotes.txt"
"#minionprocyk" "minionprocykbot"

I won't be supporting this project as this was made a very long time ago. You are free to use as little or as much of it as you want.
