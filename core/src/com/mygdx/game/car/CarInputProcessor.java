package com.mygdx.game.car;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class CarInputProcessor implements InputProcessor {
    Car car;
    public CarInputProcessor(Car car){
        this.car = car;
//        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.A:
                car.carPressLeft();
                break;
            case Input.Keys.D:
                car.carPressRight();
                break;
            //case Input.Keys.A:
            case Input.Keys.W:
                car.carSetForward();
                break;
            //case Input.Keys.Z:
            case Input.Keys.S:
                car.carSetBackwards();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            //case Input.Keys.A:
            //case Input.Keys.Z:
            case Input.Keys.W:
            case Input.Keys.S:
                car.carSetIdle();
                break;
            case Input.Keys.A:
               car.carReleaseLeft();
                break;
            case Input.Keys.D:
                car.carReleaseRight();
                break;
        }
        return false;
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
