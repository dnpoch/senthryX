## Description

A username-IP based whitelist system server-side mod for offline mode servers that enforces access control based on both username and IP address.

### Features

- Access control based on username and IP address
  - Whitelist players using username and their IP address (ex: `whitelist_add player1 127.0.0.1` via discord command or `sentrix whitelist add player1 127.0.0.1` via console command)
  - Username-IP Binding: Each player has a list of associated IP addresses linked to their username and can only access the server from those addresses.
  - Strict Access Control: Only whitelisted players connecting from their approved IPs are allowed to join.
  - Unauthorized Attempt Blocking: Denies access to non-whitelisted usernames or IPs not associated with the user.
  - Can add new ip for user in case of dynamically changing ip address.
- Discord bot integration:
  - Access attempt alerts.
  - Basic server management commands for admins (e.g., add/remove whitelist entries).
  - Discord bot can also be disabled.

### Installation guide

1. Download the Mod

   - Download the mod from the official repository or a trusted source.

2. Install the Mod

   - Place the downloaded mod file into the `mods` folder within your Minecraft directory.

3. Initial Server Startup

   - Start or restart your server. On the first startup, an error is expected—this is normal. During this process, a file named `config.json` will be generated in the `config/sentrix` directory.

4. Configure the Discord bot if you want to enable the Discord integration feature, [see step by step guide here.](#configure-discord-bot).

5. Configure the Mod

   - Open the `config.json` file and adjust the settings as needed. Refer to the Configuration Values section below for guidance. [Configuration values](#configuration-values)

6. Restart the Server
   - Once you’ve configured the config.json file, restart your server to apply the changes.

---

#### Configuration Values

<details>
<summary><strong>Configuration Values</strong></summary>

To configure, navigate to `/config/setrix/config.json`

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
<summary><strong>Console Commands</strong></summary>

| Command                                    | Usage                                                                  |
| ------------------------------------------ | ---------------------------------------------------------------------- |
| /sentrix whitelist add `<username>` `<ip>` | Command for whitelisting a player.                                     |
| /sentrix whitelist remove `<username>`     | Command to remove a player from whitelist entry.                       |
| /sentrix ip add `<username>` `<ip>`        | Command to add a new ip for player.                                    |
| /sentrix reload                            | To reload the player list in case of editing player list file manually |
| /sentrix count                             | To count total whitelisted players.                                    |

</details>

### Discord Admin Commands

<details>
<summary><strong>Discord Admin Commands</strong></summary>

| Command                            | Usage                                                             |
| ---------------------------------- | ----------------------------------------------------------------- |
| /whitelist_add `<username>` `<ip>` | Command for whitelisting a player.                                |
| /whitelist_remove `<username>`     | Command to remove a player from whitelist entry.                  |
| /add_ip `<username>` `<ip>`        | Command to add a new ip for player.                               |
| /sentrix reload                    | To reload the player list in case of editing player list manually |
| /player_count                      | To count total whitelisteed players.                              |

</details>

### Configure Discord Bot

<details>
<summary><strong>Configure Discord Bot</strong></summary>

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

2 Enable: - Presence Intent

    - Server Members Intent

3. Click Save Changes at the bottom.

#### Step 4: Invite the Bot to Your Discord Server

1. Go to the OAuth2 → URL Generator section.

2. Under Scopes, check:

   - bot

3. Under Bot Permissions, check:

   - Send Messages

   - Read Message History

   - Manage Messages (if needed)

   - Manage Roles (if needed for whitelist commands)

4. Copy the generated URL and open it in your browser.

5. Select your server and click Authorize.

#### Step 5: Configure Your config.json File

1. Now edit the `config.json` file located in /config/setrix/. Here’s an example:

</details>
