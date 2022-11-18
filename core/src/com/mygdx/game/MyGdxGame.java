package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	public static MainScreen ref1;
	
	@Override
	public void create () {
		this.setScreen(new MainScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose (){
	}
}
