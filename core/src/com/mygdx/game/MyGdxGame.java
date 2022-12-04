package com.mygdx.game;

import com.badlogic.gdx.*;
import com.mygdx.game.screens.TitleScreen;
import com.mygdx.game.screens.MainScreen;

public class MyGdxGame extends Game {
	public static MyGdxGame ref;

	public MyGdxGame(){
		ref = this;
	}
	@Override
	public void create() {
		this.setScreen(new TitleScreen());
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}

	public void setGameScreen(){
		Screen s = this.getScreen();
		MyGdxGame.ref.setScreen(new MainScreen());
		s.dispose();
	}
}