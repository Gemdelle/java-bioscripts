package com.brydiam.javabioscripts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.Animation;

public class MapGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture mapTexture;
    private Vector2 characterPosition;
    private OrthographicCamera camera;
    private static final int MAP_SIZE = 1000; // Tamaño del mapa en tiles
    private static final int TILE_SIZE = 32; // Tamaño de cada tile en píxeles
    private static final float MOVE_SPEED = 8f; // Velocidad de movimiento en píxeles por segundo
    private SoundManager soundManager;

    private Stage stage;
    private TextArea textArea;
    private boolean textAreaVisible = false;

    private static final int FRAME_COLS = 15; // Number of frames
    private Animation<TextureRegion> characterAnimation;
    private float stateTime;

    @Override
    public void create() {
        soundManager = SoundManager.getInstance();
        soundManager.loadSound("walk", Gdx.audio.newSound(Gdx.files.internal("sounds/grass.ogg")));

        batch = new SpriteBatch();
        mapTexture = new Texture("map.jpg"); // Imagen del mapa
        characterPosition = new Vector2(500, 500); // Posición inicial del personaje en el centro del mapa (en tiles)

        // Load character animation frames
        TextureRegion[] animationFrames = new TextureRegion[FRAME_COLS];
        for (int i = 0; i < FRAME_COLS; i++) {
            animationFrames[i] = new TextureRegion(new Texture(Gdx.files.internal("./monster_01/body_" + (i + 1) + ".png")));
        }
        characterAnimation = new Animation<>(0.1f, animationFrames);
        stateTime = 0f;

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(width, height);
        camera.position.set(characterPosition.x * TILE_SIZE, characterPosition.y * TILE_SIZE, 0);
        camera.update();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        BitmapFont defaultFont = new BitmapFont(); // O carga la fuente desde un archivo si ya tienes uno
        skin.add("default-font", defaultFont);

        // Crear una textura de un solo píxel blanca
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture pixmapTexture = new Texture(pixmap);
        skin.add("white", new TextureRegionDrawable(new TextureRegion(pixmapTexture)));

        // Crear una textura de un solo píxel azul
        Pixmap pixmapBlue = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapBlue.setColor(Color.BLUE);
        pixmapBlue.fill();
        Texture pixmapBlueTexture = new Texture(pixmapBlue);
        skin.add("blue", new TextureRegionDrawable(new TextureRegion(pixmapBlueTexture)));

        // Crear colores y añadirlos al skin
        skin.add("black", Color.BLACK);
        skin.add("blue", Color.BLUE);

        // Estilo del TextArea
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = skin.getFont("default-font");
        textFieldStyle.fontColor = skin.getColor("black");
        textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(pixmapTexture)); // Cursor en negro
        textFieldStyle.selection = new TextureRegionDrawable(new TextureRegion(pixmapBlueTexture)); // Color de selección en azul

        // Fondo del TextArea
        Texture textfieldBackgroundTexture = new Texture(Gdx.files.internal("textfield_background.png"));
        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(textfieldBackgroundTexture));

        // Crear el TextArea
        textArea = new TextArea("", textFieldStyle);
        textArea.setMessageText("Escribe aquí...");
        textArea.setSize(400, 400); // Ajusta el tamaño según tus necesidades
        textArea.setPosition(10, 10); // Posición en la esquina inferior izquierda
        textArea.setVisible(textAreaVisible);
        stage.addActor(textArea);
    }

    @Override
    public void render() {
        handleInput();

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = characterAnimation.getKeyFrame(stateTime, true);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(mapTexture, 0, 0, MAP_SIZE * TILE_SIZE, MAP_SIZE * TILE_SIZE); // Dibuja el mapa
        batch.draw(currentFrame, characterPosition.x * TILE_SIZE, characterPosition.y * TILE_SIZE, TILE_SIZE, TILE_SIZE); // Dibuja el personaje
        batch.end();

        camera.position.set(characterPosition.x * TILE_SIZE, characterPosition.y * TILE_SIZE, 0);
        camera.update();

        if (textAreaVisible) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            textAreaVisible = !textAreaVisible;
            textArea.setVisible(textAreaVisible);
            if (textAreaVisible) {
                Gdx.input.setInputProcessor(stage);
                textArea.setText("");
                textArea.setDisabled(false);
                textArea.setFocusTraversal(false);
                stage.setKeyboardFocus(textArea);
            } else {
                Gdx.input.setInputProcessor(null);
            }
        }

        if (!textAreaVisible) {
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

            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                soundManager.playSound("walk", 0.5f, 700);
            }
            characterPosition.x = Math.max(0, Math.min(MAP_SIZE * TILE_SIZE - TILE_SIZE, characterPosition.x));
            characterPosition.y = Math.max(0, Math.min(MAP_SIZE * TILE_SIZE - TILE_SIZE, characterPosition.y));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapTexture.dispose();
        for (TextureRegion frame : characterAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
        soundManager.dispose();
        stage.dispose();
    }
}
