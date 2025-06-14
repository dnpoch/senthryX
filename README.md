## Description

**SenthryX** is a server-side whitelist mod that binds usernames to specific IP addresses. It enforces strict access control by allowing only whitelisted and trusted players to join the server. This prevents unauthorized access, blocks login attempts from unbound IP addresses, and protects against username spoofing. SenthryX is especially recommended for servers running in **offline mode**. It also supports multiple IP bindings per player, making it compatible with dynamic IP addresses. For added convenience, SenthryX integrates with a Discord bot that provides real-time alerts for failed login attempts and enables server management through bot commands, the bot integration can also be disabled.

### Installation guide

1. Download the Mod

   - Download the mod [modrinth](https://modrinth.com/mod/senthryx) or in the official [GitHub repository](https://github.com/dnpoch/senthryX/releases).

2. Install the Mod

   - Place the downloaded mod file into the `mods` folder within your Minecraft directory.

   - In `server.properties`, it’s recommended to disable the built in whitelist (white-list=false) to allow the mod to fully handle access control based on usernames and IPs.
     - Optionally, you can leave the built in whitelist enabled for an additional layer of security, but this is not required.

3. Initial Server Startup

   - Start or restart your server. On the first startup, an error is expected—this is normal. During this process, a file named `config.json` will be generated in the `config/senthryX` directory.

4. Configure the Discord bot if you want to enable the Discord integration feature. **_See the details for step by step process below_**.

5. Configure the Mod

   - Open the `config.json` file and adjust the settings as needed. Refer to the **_Configuration Values_** section below for guidance.

6. Restart the Server
   - Once you’ve configured the config.json file, restart your server to apply the changes.

---

### Configuration Values

<details>
<summary><strong>Click to expand</strong></summary>

To configure, navigate to `/config/senthryX/config.json`

| Key                   | Value            | Description                                                                                               |
| --------------------- | ---------------- | --------------------------------------------------------------------------------------------------------- |
| activate              | boolean          | Enables or disables the mod. Set to `true` to enable, or `false` to disable it.                           |
| enable_discord        | boolean          | Enables or disables Discord integration. Set to `true` to use the Discord bot, or `false` to turn it off. |
| bot_token             | String           | Your Discord bot token. Required for the bot to connect to your Discord server.                           |
| admin_channel_id      | String           | ID of the Discord channel where admins can run commands like whitelisting players.                        |
| enable_public_logging | Boolean          | Enables or disables public logging. Set to `true` to post logs in a public channel.                       |
| public_log_channel    | String           | ID of the Discord channel where public logs will be sent.                                                 |
| admin_ids             | Array of Strings | A list of Discord user IDs who have admin permissions for using bot commands.                             |
| presence              | String           | Custom status text shown by the Discord bot (e.g., “Watching the server”).                                |
| kick_message          | String           | Message shown to players who are not whitelisted. You can customize this.                                 |
| unknown_ip_message    | String           | Message shown to players connecting from an unknown IP address. You can customize this.                   |

</details>

### Console Commands

<details>
<summary><strong>Click to expand</strong></summary>

| Command                                     | Usage                                                                  |
| ------------------------------------------- | ---------------------------------------------------------------------- |
| /senthryx whitelist add `<username>` `<ip>` | Command for whitelisting a player.                                     |
| /senthryx whitelist remove `<username>`     | Command to remove a player from whitelist entry.                       |
| /senthryx ip add `<username>` `<ip>`        | Command to add a new ip for player.                                    |
| /senthryx reload                            | To reload the player list in case of editing player list file manually |
| /senthryx count                             | To count total whitelisted players.                                    |

</details>

### Discord Admin Commands

<details>
<summary><strong>Click to expand</strong></summary>

| Command                            | Usage                                            |
| ---------------------------------- | ------------------------------------------------ |
| /whitelist_add `<username>` `<ip>` | Command for whitelisting a player.               |
| /whitelist_remove `<username>`     | Command to remove a player from whitelist entry. |
| /add_ip `<username>` `<ip>`        | Command to add a new ip for player.              |
| /player_count                      | To count total whitelisted players.              |

</details>

### Configure Discord Bot

<details>
<summary><strong>Click to expand</strong></summary>

#### Step 1: Create a Discord Bot

1. Go to the [Discord Developer Portal](https://discord.com/developers/applications)

2. Click "New Application", give it a name (e.g., MinecraftBot), and click Create.

3. In the left sidebar, go to Bot → Click Add Bot → Confirm by clicking Yes, do it!.

#### Step 2: Get the Bot Token

1. Under the Bot section, click "Reset Token" or "Copy" under the Token field.

2. Save this token somewhere secure — you'll need it in the config file(Do not share this token. It gives full control of your bot.).

#### Step 2: Get the Bot Token

Still under the Bot section:

1. Scroll down to Privileged Gateway Intents.

2. Enable: - Presence Intent

   - Prensence Intent

   - Server Members Intent

   - Message Content Intent

3. Click Save Changes at the bottom.

#### Step 4: Invite the Bot to Your Discord Server

1. Go to the OAuth2 → URL Generator section.

2. Under Scopes, check:

   - bot

3. Under Bot Permissions, check:

   - Send Messages

   - Read Message History

4. Copy the generated URL and open it in your browser.

5. Select your server and click Authorize.

#### Step 5: Configure Your config.json File

1. Now edit the `config.json` file located in /config/setrix/. Here’s an example:

</details>
