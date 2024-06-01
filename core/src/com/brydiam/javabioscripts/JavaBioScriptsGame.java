package com.brydiam.javabioscripts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class JavaBioScriptsGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture mapTexture;
    private Texture characterTexture;
    private Vector2 characterPosition;
    private OrthographicCamera camera;
    private static final int MAP_SIZE = 1000; // Tamaño del mapa en tiles
    private static final int TILE_SIZE = 32; // Tamaño de cada tile en píxeles
    private static final float MOVE_SPEED = 8f; // Velocidad de movimiento en píxeles por segundo

    @Override
    public void create() {
        batch = new SpriteBatch();
        mapTexture = new Texture("map.jpg"); // Imagen del mapa
        characterTexture = new Texture("character.png"); // Imagen del personaje
        characterPosition = new Vector2(500, 500); // Posición inicial del personaje en el centro del mapa (en tiles)

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(width, height);
        camera.position.set(characterPosition.x * TILE_SIZE, characterPosition.y * TILE_SIZE, 0);
        camera.update();
    }

    @Override
    public void render() {
        handleInput();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(mapTexture, 0, 0, MAP_SIZE * TILE_SIZE, MAP_SIZE * TILE_SIZE); // Dibuja el mapa
        batch.draw(characterTexture, characterPosition.x * TILE_SIZE, characterPosition.y * TILE_SIZE, TILE_SIZE, TILE_SIZE); // Dibuja el personaje
        batch.end();

        camera.position.set(characterPosition.x * TILE_SIZE, characterPosition.y * TILE_SIZE, 0);
        camera.update();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterPosition.y += MOVE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterPosition.y -= MOVE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            characterPosition.x -= MOVE_SPEED * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            characterPosition.x += MOVE_SPEED * Gdx.graphics.getDeltaTime();
        }

        characterPosition.x = Math.max(0, Math.min(MAP_SIZE * TILE_SIZE - TILE_SIZE, characterPosition.x));
        characterPosition.y = Math.max(0, Math.min(MAP_SIZE * TILE_SIZE - TILE_SIZE, characterPosition.y));

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapTexture.dispose();
        characterTexture.dispose();
    }
}
