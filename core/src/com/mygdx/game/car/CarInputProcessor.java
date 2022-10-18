package com.mygdx.game.car;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class CarInputProcessor implements InputProcessor {
    Car car;
    CarInputProcessor(Car car){
        this.car = car;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
//            case Input.Keys.A:
            case Input.Keys.LEFT:
                car.carPressLeft();
//                System.out.printf("pA ");
                break;
            case Input.Keys.RIGHT:
//            case Input.Keys.D:
                car.carPressRight();
//                System.out.printf("pD ");
                break;
            case Input.Keys.A:
//            case Input.Keys.W:
                car.carSetForward();
//                System.out.printf("pW ");
                break;
            case Input.Keys.Z:
//            case Input.Keys.S:
                car.carSetBackwards();
//                System.out.printf("pS ");
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.A:
            case Input.Keys.Z:
//            case Input.Keys.W:
//            case Input.Keys.S:
//                System.out.printf("rWS ");
                car.carSetIdle();
                break;
            case Input.Keys.LEFT:
//            case Input.Keys.A:
                car.carReleaseLeft();
//                System.out.printf("rA ");
                break;
            case Input.Keys.RIGHT:
//            case Input.Keys.D:
                car.carReleaseRight();
//                System.out.printf("rD ");
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
