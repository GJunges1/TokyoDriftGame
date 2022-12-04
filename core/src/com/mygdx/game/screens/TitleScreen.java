package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyMultiplexer;
import com.mygdx.game.button.StartButton;
import com.mygdx.game.button.StartButtonController;

public class TitleScreen implements Screen {
    Texture img, img2;
    Sprite backGround;
    StartButton button;
    SpriteBatch batch;

    static MyMultiplexer multiplexer;
    StartButtonController startButtonController;

    @Override
    public void show() {
        batch = new SpriteBatch();
        img = new Texture("TitleScreen.png");
        img2 = new Texture("StartButton.png");

        multiplexer = new MyMultiplexer();
        startButtonController = new StartButtonController();

        backGround = new Sprite(img, 0, 0, img.getWidth(), img.getHeight());

        button = new StartButton(img2);
        button.setSize(601, 272);
        button.setPosition(Gdx.graphics.getWidth()/2-button.getWidth()/2,Gdx.graphics.getHeight()/8);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(1, 0, 0, 1);

        batch.begin();
        backGround.draw(batch);
        button.draw(batch);
        batch.end();

        if(button.isWithin(Gdx.input.getX(),Gdx.input.getY())){
            System.out.println("INICIAR");
            MyGdxGame.ref.setScreen(new MainScreen());
            this.dispose();
        }
    }

    @Override
    public void resize(int i, int i1) {

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
        batch.dispose();
        img.dispose();
        img2.dispose();
    }
}