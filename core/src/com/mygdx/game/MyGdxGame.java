package com.mygdx.game;

import com.badlogic.gdx.*;
import com.mygdx.game.car.Car;
import com.mygdx.game.screens.TitleScreen;
import com.mygdx.game.screens.MainScreen;

public class MyGdxGame extends Game {
	public static MyGdxGame ref;
	Car car1,car2;

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
}