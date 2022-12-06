package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.MyMultiplexer;
import com.mygdx.game.button.StartButton;
import com.mygdx.game.button.StartButtonController;
import com.mygdx.game.car.Car;

public class ScoreboardScreen implements Screen {
    Texture img, img2;
    Sprite backGround;
    StartButton button;
    SpriteBatch batch;
    Sound sound;
    BitmapFont bitmapFont;
    Car car1,car2;

    public ScoreboardScreen(Car car1, Car car2){
        this.car1 = car1;
        this.car2 = car2;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        img = new Texture("ScoreboardScreen.png");
        img2 = new Texture("ContinueButton.png");

        backGround = new Sprite(img, 0, 0, img.getWidth(), img.getHeight());

        button = new StartButton(img2);
        button.setSize(293, 100);
        button.setPosition(Gdx.graphics.getWidth()/2+button.getWidth()/2,Gdx.graphics.getHeight()/13);

        this.sound = Gdx.audio.newSound(Gdx.files.internal("ButtonClick.wav"));

        // *** START FONT *** //
        bitmapFont = new BitmapFont(Gdx.files.internal("TokyoDrift_font.fnt"));
        bitmapFont.getData().setScale(2.5f);
        // *** END MUSIC *** //

//        this.car1 = MainScreen.ref.car1;
//        this.car2 = MainScreen.ref.car2;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);

        batch.begin();
        backGround.draw(batch);
        button.draw(batch);

        if(car1.getTotalSEC()<car2.getTotalSEC()){
            bitmapFont.draw(batch,"1st",  Gdx.graphics.getWidth()/2-430, Gdx.graphics.getHeight()/2+20);
            bitmapFont.draw(batch,"" + car1.getNameTag(),Gdx.graphics.getWidth()/2-40, Gdx.graphics.getHeight()/2+20);
            bitmapFont.draw(batch,"" + car1.formatTimeScoreboard() , Gdx.graphics.getWidth()/2+280, Gdx.graphics.getHeight()/2+20);

            bitmapFont.draw(batch,"2st", Gdx.graphics.getWidth()/2-430, Gdx.graphics.getHeight()/2-65);
            bitmapFont.draw(batch,"" + car2.getNameTag(),Gdx.graphics.getWidth()/2-40, Gdx.graphics.getHeight()/2-65);
            bitmapFont.draw(batch,"" + car2.formatTimeScoreboard(), Gdx.graphics.getWidth()/2+280, Gdx.graphics.getHeight()/2-65);
        }
        else{
            bitmapFont.draw(batch,"1st",  Gdx.graphics.getWidth()/2-430, Gdx.graphics.getHeight()/2+20);
            bitmapFont.draw(batch,"" + car2.getNameTag(),Gdx.graphics.getWidth()/2-40, Gdx.graphics.getHeight()/2+20);
            bitmapFont.draw(batch,"" + car2.formatTimeScoreboard() , Gdx.graphics.getWidth()/2+280, Gdx.graphics.getHeight()/2+20);

            bitmapFont.draw(batch,"2nd", Gdx.graphics.getWidth()/2-430, Gdx.graphics.getHeight()/2-65);
            bitmapFont.draw(batch,"" + car1.getNameTag(),Gdx.graphics.getWidth()/2-40, Gdx.graphics.getHeight()/2-65);
            bitmapFont.draw(batch,"" + car1.formatTimeScoreboard(), Gdx.graphics.getWidth()/2+280, Gdx.graphics.getHeight()/2-65);
        }

        batch.end();

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && button.isWithin(Gdx.input.getX(),Gdx.input.getY())){
            this.sound.play(0.3f);
            MyGdxGame.ref.setScreen(new TitleScreen());
            this.dispose();
        }
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

    }
}
