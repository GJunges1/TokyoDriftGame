package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class MyMultiplexer extends InputMultiplexer {
    public static MyMultiplexer ref;

    public MyMultiplexer(){
        this.ref = this;
    }

    @Override
    public void addProcessor(InputProcessor processor) {
        super.addProcessor(processor);
        Gdx.input.setInputProcessor(this);
    }
}
