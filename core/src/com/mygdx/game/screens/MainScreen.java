package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.car.Car;
import com.mygdx.game.circuit.Circuit;

public class MainScreen implements Screen {
    public static MainScreen ref;
    public static Texture img1,img2;
    OrthographicCamera camera;
    SpriteBatch batch;
    Car car;
    Circuit circuit;
    final static int CAR_WIDTH=248;
    final static int CAR_HEIGHT=480;

    final static int img2Width = 512;

    @Override
    public void show() {
        ref = this;
        batch = new SpriteBatch();
        img1 = new Texture("car.png");
        img2 = new Texture("circuit.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);

        circuit = new Circuit(img2, 0, 0, img2.getWidth(), img2.getHeight());
        circuit.setSize(circuit.getWidth()*2.25f,circuit.getHeight()*2);

        //car image: width = 244, height = 480
        car = new Car(img1, 0, 0, img1.getWidth(), img1.getHeight());

        // setting car position to track starting line
        car.setX(71.611237f);
        car.setY(268.462372f);

//        car.setY(Gdx.graphics.getHeight() / 2 - car.getHeight() / 2);
//        car.setX(Gdx.graphics.getWidth() / 2 - car.getWidth() / 2);
        car.setSize(car.getWidth()/10,car.getHeight()/10);
        car.setOriginCenter();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        camera.position.set(car.getX(),car.getY(),0);
        camera.update();
        circuit.draw(batch,delta);
        car.draw(batch, delta);
        car.update(delta);

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
        img1.dispose();
        img2.dispose();
    }
}
