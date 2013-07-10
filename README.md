Gamework
========
An easy to use Android framework for creating geolocation games.

Definition of game scenario is located in *.xml file which allows easy reconfiguration - often without changing any application code.

How to use
==========
1. Create your Android application (with Activities, etc.)
2. Include Gamework library into your project
3. Create file with game scenario (*.xml)
  - Look at *GameworkApp / assets /* ***example.xml*** to understand scenario structure
4. Create your game service by inheriting *cz.robyer.gamework.game.GameService* class and implement needed methods:
  - *getGameNotification()*
  - *onGameStart()*
  - *onEvent()*
5. Don't forget to add definition of your service into *AndroidManifest.xml*
6. Now you can start game by *startService(intent)* with Extra String name "filename", whose value represents filename of scenario in your *assets* folder

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

License
=======
Gamework library is licensed under *Apache License Version 2.0*.