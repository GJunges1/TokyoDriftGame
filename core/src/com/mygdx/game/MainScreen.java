package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.car.Car;
import com.mygdx.game.car.Car2;
import com.mygdx.game.circuit.Circuit;
import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class MainScreen implements Screen {
    public static MainScreen ref;
    public static Texture car1_img,img2,car2_img, car1Braking_img, car2Braking_img;
    OrthographicCamera camera;
    SpriteBatch batch;
    ShapeRenderer shape;
    Car car1;
    Car2 car2;
    Circuit circuit;
    Viewport carViewport1;
    Viewport carViewport2;
    InputMultiplexer inputMultiplexer;

    private Music music;

    private Viewport viewport;

    //private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    //private TileMapHelper tileMapHelper;

    @Override
    public void show() {
        //this.tileMapHelper = new TileMapHelper();
        //this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();

        ref = this;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        car1_img = new Texture("car1.png");
        car2_img = new Texture("car2.png");
        img2 = new Texture("map.png");

        // *** START GAMBIARRA CAR BREAKING TEXTURE ***//
        car1Braking_img = new Texture("car1_braking.png");
        car2Braking_img = new Texture("car2_braking.png");
        // *** END GAMBIARRA CAR BREAKING TEXTURE ***//

        //camera = new OrthographicCamera();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);

        // *** START VIEWPORT *** //

        carViewport1 = new FitViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight(), camera);
        carViewport1.setScreenBounds(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        carViewport2 = new FitViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight(), camera);
        carViewport2.setScreenBounds(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        // *** END VIEWPORT *** //

        circuit = new Circuit(img2, 0, 0, img2.getWidth(), img2.getHeight());
        circuit.setSize(circuit.getWidth(),circuit.getHeight());

        car2 = new Car2(car2_img, car2Braking_img, 0, 0, car2_img.getWidth(), car2_img.getHeight());
        car1 = new Car(car1_img, car1Braking_img, 0, 0, car1_img.getWidth(), car1_img.getHeight());

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(car1.carInputProcessor);
        inputMultiplexer.addProcessor(car2.carInputProcessor2);

        Gdx.input.setInputProcessor(inputMultiplexer);
        // setting car position to track starting line
        car1.setX(170f);
        car1.setY(400f);
        car2.setX(170f + car2.getWidth()/10 + 20f);
        car2.setY(400f);

        car1.setSize(car1.getWidth()/10,car1.getHeight()/10);
        car1.setOriginCenter();

        car2.setSize(car2.getWidth()/10,car2.getHeight()/10);
        car2.setOriginCenter();


        // *** START MUSIC *** //
        Music music = Gdx.audio.newMusic(Gdx.files.internal("tokyo_drift.wav"));
        music.setVolume(0.1f);
        music.setLooping(true);
        music.play();
        // *** END MUSIC *** //
    }

    private void update(){
        //orthogonalTiledMapRenderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        //this.update();
        ScreenUtils.clear(0, 0, 0, 1);
        //orthogonalTiledMapRenderer.render();

        // *** START BATCH CAR 1 ***
        batch.begin();

        carViewport1.apply();
        camera.position.set(car1.getX(),car1.getY(),0);
        camera.update();

        circuit.draw(batch,delta);
        car1.draw(batch, delta);
        car2.draw(batch, delta);

        batch.setProjectionMatrix(camera.combined);
        batch.end();
        // *** END BATCH CAR 1 ***

        // *** START BATCH CAR 2 ***
        batch.begin();

        carViewport2.apply();
        camera.position.set(car2.getX(),car2.getY(),0);
        camera.update();

        circuit.draw(batch,delta);
        car1.draw(batch, delta);
        car2.draw(batch, delta);

        car1.update(delta); // UPDATE CARS POSITIONS
        car2.update(delta); // '                   '

        batch.setProjectionMatrix(camera.combined);
        batch.end();
        // *** END BATCH CAR 2 ***
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        car1_img.dispose();
        car2_img.dispose();
        img2.dispose();
    }
}
