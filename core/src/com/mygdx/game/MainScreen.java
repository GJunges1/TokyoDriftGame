package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.car.Car;
import com.mygdx.game.car.Car2;
import com.mygdx.game.circuit.Circuit;

public class MainScreen implements Screen {
    public static MainScreen ref;
    public static Texture car1_img,img2,car2_img;
    OrthographicCamera camera;
    SpriteBatch batch;
    Car car1;
    Car2 car2;
    Circuit circuit;

    //private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    //private TileMapHelper tileMapHelper;

    @Override
    public void show() {
        //this.tileMapHelper = new TileMapHelper();
        //this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();

        ref = this;
        batch = new SpriteBatch();
        car1_img = new Texture("car1.png");
        car2_img = new Texture("car2.png");
        img2 = new Texture("map.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);

        circuit = new Circuit(img2, 0, 0, img2.getWidth(), img2.getHeight());
        circuit.setSize(circuit.getWidth(),circuit.getHeight());

        car2 = new Car2(car2_img, 0, 0, car2_img.getWidth(), car2_img.getHeight());
        car1 = new Car(car1_img, 0, 0, car1_img.getWidth(), car1_img.getHeight());

        // setting car position to track starting line
        car1.setX(170f);
        car1.setY(400f);
        car2.setX(170f + car2.getWidth()/10 + 20f);
        car2.setY(400f);

        car1.setSize(car1.getWidth()/10,car1.getHeight()/10);
        car1.setOriginCenter();

        car2.setSize(car2.getWidth()/10,car2.getHeight()/10);
        car2.setOriginCenter();
    }

    private void update(){
        //orthogonalTiledMapRenderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        //this.update();
        ScreenUtils.clear(0, 0, 0, 1);
        //orthogonalTiledMapRenderer.render();
        batch.begin();

        camera.position.set(car1.getX(),car1.getY(),0);
        camera.update();
        circuit.draw(batch,delta);

        car1.draw(batch, delta);
        car1.update(delta);

        car2.draw(batch, delta);
        car2.update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.end();
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