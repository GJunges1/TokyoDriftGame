package com.mygdx.game.car;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class CarInputProcessor implements InputProcessor {
    Car car;
    final boolean isItTheRightCar;
    public CarInputProcessor(Car car, final boolean isItTheRightCar){
        this.car = car;
        this.isItTheRightCar = isItTheRightCar;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(!isItTheRightCar) {
            switch (keycode) {
                case Input.Keys.A:
                    car.carPressLeft();
                    break;
                case Input.Keys.D:
                    car.carPressRight();
                    break;
                case Input.Keys.W:
                    car.carSetForward();
                    break;
                case Input.Keys.S:
                    car.carSetBackwards();
                    break;
            }
        }
        else{
            switch (keycode) {
                case Input.Keys.LEFT:
                    car.carPressLeft();
                    break;
                case Input.Keys.RIGHT:
                    car.carPressRight();
                    break;
                case Input.Keys.UP:
                    car.carSetForward();
                    break;
                case Input.Keys.DOWN:
                    car.carSetBackwards();
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(!isItTheRightCar) {
            switch (keycode) {
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
        }
        else{
            switch (keycode) {
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
