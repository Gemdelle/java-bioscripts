package com.brydiam.javabioscripts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;

public class Splash extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private VideoPlayer videoPlayer;

    @Override
    public void create() {
        batch = new SpriteBatch();

        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setOnVideoSizeListener((width, height) -> {
        });
        if (Gdx.files.internal("splash_video.ogv").exists()) {
            try {
                videoPlayer.play(Gdx.files.internal("splash_video.ogv"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("File 'splash_video.mp4' does not exist!");
        }

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        videoPlayer.update();
        batch.begin();

        Texture frame = videoPlayer.getTexture();
        if(frame != null){
            batch.draw(frame,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        // Check if the video has finished playing to switch scenes
        if ( !videoPlayer.isPlaying() || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
            videoPlayer.dispose();
            JavaBioScriptsGame.setScene(Scene.MAP_GAME);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        videoPlayer.dispose();
        batch.dispose();
    }
}
