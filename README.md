discord_sync
============

This is a Spigot plugin that will assign players a Discord role when they join the server.

## Installation

You need Maven and a JDK installed.

Clone the repository:

```bash
$ git clone https://github.com/190n/discord_sync
```

Compile it:

```bash
$ cd discord_sync
$ mvn install
```

Then simply copy `target/discord_sync-1.0-SNAPSHOT-jar-with-dependencies.jar` into your `plugins`
folder.

## Configuration

The config file is stored in `plugins/discord_sync/config.yml`. It has the following properties:

> Note: IDs for Discord things can be obtained easily by turning on "Developer Mode" under Discord's advanced settings. Then you you can right-click on a server, role, or user to get its ID.

### `roleId` (string)

ID of the role that the bot should assign.

### `guildId` (string)

ID of the guild (Discord server) in which the role should be assigned

### `players`

Mapping of Minecraft user IDs (not usernames!) to Discord user IDs, both as strings. You can determine the UUID of a Minecraft account by entering its username at [this website](https://mcuuid.net/).

### `discordToken` (string)

Token for a Discord bot account that the plugin should operate as. You must create an application [here](https://discord.com/developers/applications), add a bot to it, and add it to your server by following [this guide](https://discordjs.guide/preparations/adding-your-bot-to-servers.html). Make sure to give it the "Manage Roles" permission.

## Example configuration (not with real IDs)

All the quotes are required, otherwise YAML will think they are numbers.

```yml
roleId: '8273648234'
guildId: '2346876'
players:
  e4a3c8e7-74ba-42fc-b6f1-aa0f52247ffb: '23746'
  7a1ee036-2852-4dd3-826b-93c0449e01bf: '28736'
discordToken: XXXXXXXXX
```
