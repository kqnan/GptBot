
Language:&nbsp;[中文](README.md)&nbsp;&nbsp;[English](README_EN.md)
# GptBot



This project makes ChatGpt or other ai chat bots communicate with players in order to active atmosphere in minecraft server 

# Features
* Chat bot can grab players'chat message and reply automatically and periodically
* Support customizing chat bot's message format
* Players'can customize the chat bot's message length.Because messages that are too long interfere with conversation between players
* Bot ignores sensitive words
* Use the return value of kether scripts as condition when players call the bot


# Build
**Windows:**

```
gradlew.bat clean build
```

**macOS/Linux:**

```
./gradlew clean build
```

Build artifacts should be found in `./build/libs` folder.
