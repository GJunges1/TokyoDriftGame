package com.mygdx.game.car;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class CarInputProcessor2 implements InputProcessor {
    Car2 car;
    public CarInputProcessor2(Car2 car){
        this.car = car;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.LEFT:
                car.carPressLeft();
                break;
            case Input.Keys.RIGHT:
                car.carPressRight();
                break;
            //case Input.Keys.A:
            case Input.Keys.UP:
                car.carSetForward();
                break;
            //case Input.Keys.Z:
            case Input.Keys.DOWN:
                car.carSetBackwards();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            //case Input.Keys.A:
            //case Input.Keys.Z:
            case Input.Keys.UP:
            case Input.Keys.DOWN:
                car.carSetIdle();
                break;
            case Input.Keys.LEFT:
               car.carReleaseLeft();
                break;
            case Input.Keys.RIGHT:
                car.carReleaseRight();
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) { return false; }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
