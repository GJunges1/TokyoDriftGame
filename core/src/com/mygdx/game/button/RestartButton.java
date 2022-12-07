package com.mygdx.game.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class RestartButton extends Sprite {
    public RestartButton(Texture texture) {
        super(texture);
    }

    public boolean isWithin(float x, float y){
        y = Gdx.graphics.getHeight() - y;
        return (x > this.getX()) && (y > this.getY())
                && (x < this.getX() + this.getWidth())
                && (y < this.getY() + this.getHeight());
    }

    public void resize(float mult) {
        this.setScale(mult);
    }
}




