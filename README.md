Gamework
========
--- TO BE DONE ---


How to use
==========
1. Include Gamework library into your project
2. Create file with game scenario (*.xml)
  - Look at *ExampleGame / assets /* ***example.xml*** to understand scenario structure
3. Create your game service by inheriting *cz.robyer.gamework.game.GameService* class and implement needed methods:
  - *getGameNotification()*
  - *onGameStart()*
  - *onEvent()*
4. Don't forgot to add your service into *AndroidManifest.xml*
5. Now you can start game by startService(intent) with Extra String name "filename" with filename of scenario from asets

-
**Example of start game service:**
```java
Intent intent = new Intent(this, GameService.class);
intent.putExtra("filename", "example.xml");
startService(intent);
```

**Example of own listener for game events:**
```java
if (GameService.isRunning()) {
    GameService game = GameService.getInstance();
    game.registerListener(new GameEventListener() {
        @Override
        public void receiveEvent(GameEvent event) {
            if (event.type == UPDATED_LOCATION) {
                ...
            }
            ...
        }
    });
}
```
