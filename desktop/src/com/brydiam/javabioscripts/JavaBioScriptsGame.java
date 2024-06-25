package com.brydiam.javabioscripts;

import com.badlogic.gdx.ApplicationAdapter;

public class JavaBioScriptsGame extends ApplicationAdapter {
    private static Scene currentScene;
    private MapGame mapGame;
    private Splash splash;

    @Override
    public void create() {
        currentScene = Scene.SPLASH; // Start with the splash screen
        mapGame = new MapGame();
        splash = new Splash();
        splash.create();
        mapGame.create();
    }

    @Override
    public void render() {
        switch (currentScene) {
            case MAP_GAME:
                System.out.println("MAP_GAME");
                mapGame.render();
                break;
            case SPLASH:
                System.out.println("SPLASH");
                splash.render();
                break;
        }
    }

    @Override
    public void dispose() {
        mapGame.dispose();
        splash.dispose();
    }

    public static void setScene(Scene scene) {
        currentScene = scene;
    }
}
