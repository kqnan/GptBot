# GptBot聊天机器人



本项目志在把ChatGpt等更多ai聊天机器人接入Minecraft
让玩家少的服务器也能够活跃气氛

* 机器人会自动抓取玩家的聊天内容进行回复
* 可自定义机器人的聊天格式
* 玩家可以自定义机器人的回复信息的长度，避免刷屏
* 敏感词验证，包含政治，暴力，色情等敏感词的内容，机器人不予回复
* 支持以kether脚本返回值为条件的主动呼叫ai功能

**Windows:**

```
gradlew.bat clean build
```

**macOS/Linux:**

```
./gradlew clean build
```

Build artifacts should be found in `./build/libs` folder.