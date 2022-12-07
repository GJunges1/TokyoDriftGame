package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import static com.badlogic.gdx.Gdx.input;

public class MainScreenInputProcessor implements InputProcessor {
    private final MainScreen mainScreen;
    boolean inPause;
    public MainScreenInputProcessor(MainScreen mainScreen){
        this.mainScreen = mainScreen;
    }
    @Override
    public boolean keyDown(int i) {
        switch(i){
            case Input.Keys.ESCAPE:
                if(!inPause){
                    mainScreen.pause();
                    inPause=true;
                }
                else{
                    mainScreen.resume();
                    inPause=false;
                }
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        if(mainScreen.restartButton.isWithin(i,i1)){
            mainScreen.restartGame();
        }
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
