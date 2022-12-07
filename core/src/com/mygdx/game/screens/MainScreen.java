package com.mygdx.game.screens;
//package com.mygdx.game; //se o codigo acima der erro

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.button.RestartButton;
import com.mygdx.game.button.StartButton;
import com.mygdx.game.car.Car;
import helper.TileMapHelper;
import jdk.tools.jmod.Main;

public class MainScreen implements Screen {
    public static MainScreen ref;
    public static Texture car1_img, car2_img, car1Braking_img, car2Braking_img;
    OrthographicCamera camera;
    SpriteBatch batch;
    public static Car car1,car2;
    Viewport carViewport1;
    Viewport carViewport2;
    InputMultiplexer inputMultiplexer;
    BitmapFont bitmapFont;

    private Music music;

    private Viewport viewport;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    private TiledMap tiledmap;
    private TiledMapTileLayer[] checkpointLayers;
    private int car1Lap;
    private int car2Lap;
    private int totalLaps;
    private String NameTAG1,NameTAG2;
    private boolean isPaused;
    private OrthographicCamera independentCamera;
    public RestartButton restartButton;

    @Override
    public void show() {
        // *** START TILED MAP ***//
        this.tileMapHelper = new TileMapHelper();
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();
        // *** END TILED MAP ***//

        ref = this;
        batch = new SpriteBatch();
        car1_img = new Texture("car1.png");
        car2_img = new Texture("car2.png");

        // *** START GAMBIARRA CAR BREAKING TEXTURE ***//
        car1Braking_img = new Texture("car1_braking.png");
        car2Braking_img = new Texture("car2_braking.png");
        // *** END GAMBIARRA CAR BREAKING TEXTURE ***//

        //camera = new OrthographicCamera();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
        independentCamera = new OrthographicCamera();
        independentCamera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // *** START VIEWPORT *** //

        carViewport1 = new FitViewport(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/2, camera);
        carViewport2 = new FitViewport(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/2, camera);
        carViewport1.setScreenBounds(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        carViewport2.setScreenBounds(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), independentCamera);
        viewport.setScreenBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // *** END VIEWPORT *** //

        this.tiledmap = new TmxMapLoader().load("core/src/map/map.tmx");

        checkpointLayers = new TiledMapTileLayer[3];
        checkpointLayers[0] = (TiledMapTileLayer) tiledmap.getLayers().get("CHEGADA");
        checkpointLayers[1] = (TiledMapTileLayer) tiledmap.getLayers().get("CHECKPOINT1");
        checkpointLayers[2] = (TiledMapTileLayer) tiledmap.getLayers().get("CHECKPOINT2");

        this.NameTAG1 = "jogador 1";
        this.NameTAG2 = "jogador 2";

        car1 = new Car(car1_img,car1Braking_img, 0, 0, car1_img.getWidth(), car1_img.getHeight(),
                800,
                6,
                400,
                100,
                false,
                this.NameTAG1,
                (TiledMapTileLayer) tiledmap.getLayers().get("PAREDE"),
                checkpointLayers);
        car2 = new Car(car2_img, car2Braking_img, 0, 0, car2_img.getWidth(), car2_img.getHeight(),
                800,
                6,
                400,
                100,
                true,
                this.NameTAG2,
                (TiledMapTileLayer) tiledmap.getLayers().get("PAREDE"),
                checkpointLayers);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(car1.carInputProcessor);
        inputMultiplexer.addProcessor(car2.carInputProcessor);
        inputMultiplexer.addProcessor(new MainScreenInputProcessor(this));

        Gdx.input.setInputProcessor(inputMultiplexer);
        // setting car position to track starting line
        float positionX = 137;
        float positionY = 380;
        car1.setX(positionX);
        car1.setY(positionY);
        car2.setX(positionX + car2.getWidth()/10 + 35f);
        car2.setY(positionY);

        car1.setSize(car1.getWidth()/10,car1.getHeight()/10);
        car1.setOriginCenter();

        car2.setSize(car2.getWidth()/10,car2.getHeight()/10);
        car2.setOriginCenter();

        // *** START MUSIC *** //
        this.music = Gdx.audio.newMusic(Gdx.files.internal("tokyo_drift.wav"));
        this.music.setVolume(0.3f);
        this.music.setLooping(true);
        this.music.play();
        // *** END MUSIC *** //

        // *** START FONT *** //
        bitmapFont = new BitmapFont(Gdx.files.internal("TokyoDrift_font.fnt"));
        // *** END MUSIC *** //
        restartButton = new RestartButton(new Texture("restart.png"));
        restartButton.setPosition(100,100);
        restartButton.resize(10f);

        setStartTime(car1,car2,System.currentTimeMillis());
        totalLaps = 1; // definindo número de voltas da corrida
    }

    private void setStartTime(Car car1, Car car2, long startTime) {
        car1.setStartTime(startTime);
        car2.setStartTime(startTime);
    }

    private void update(){
        orthogonalTiledMapRenderer.setView(camera);
    }

    @Override
    public void render(float delta){
        if(!isPaused) {
            int alturaTexto = 140;

            car1Lap = car1.getCarLap();
            car2Lap = car2.getCarLap();
            checkFinishedRacers();

            ScreenUtils.clear(Color.BLACK);

            // *** START BATCH CAR 1 ***
            batch.begin();
            this.update();
            carViewport1.apply();
            camera.position.set(car1.getX(), car1.getY(), 0);
            camera.update();
            orthogonalTiledMapRenderer.render();

            car1.draw(batch, delta);
            car2.draw(batch, delta);

            // imprimindo tempo para o carro 2:
            bitmapFont.draw(batch, car2.formatTime(), car2.getX() + 36, car2.getY() + 30 + alturaTexto);

            // e voltas para o carro 2:
            bitmapFont.draw(batch, "VOLTA" + "     " + car2Lap + " de " + totalLaps, car2.getX() + 50, car2.getY() + alturaTexto + 15);


            if (car2.getFinished()) {
                bitmapFont.draw(batch, "" + car2.getNameTag() + " TERMINOU " + car2.getFormattedEndPos() + "!", car2.getX() - 15, car2.getY());
            }
            printNameTag(car2, this.NameTAG2);

            batch.end();
            // *** END BATCH CAR 1 ***


            // *** START BATCH CAR 2 ***
            batch.begin();

            carViewport2.apply();
            this.update();
            camera.position.set(car2.getX(), car2.getY(), 0);
            camera.update();
            orthogonalTiledMapRenderer.render();

            car2.draw(batch, delta);
            car1.draw(batch, delta);

            // imprimindo tempo para o carro 1:
            bitmapFont.draw(batch, car1.formatTime(), car1.getX() + 36, car1.getY() + 30 + alturaTexto);

            // e voltas para o carro 2:
            bitmapFont.draw(batch, "VOLTA" + "     " + car1Lap + " de " + totalLaps, car1.getX() + 50, car1.getY() + alturaTexto + 15);

            if (car1.getFinished()) {
                bitmapFont.draw(batch, "" + car1.getNameTag() + " TERMINOU " + car1.getFormattedEndPos() + "!", car1.getX() - 15, car1.getY());
            }
            printNameTag(car1, this.NameTAG1);

            batch.end();
            // *** END BATCH CAR 2 ***

            car1.update(delta, car2); // UPDATE CARS POSITIONS
            car2.update(delta, car1); // '                   '

            // Aqui a ideia é trocar de tela quando os dois terminam a corrida
            // Tem que pular pra ScoreboardScreen()
            checkRaceEnded();
        }
        else{
            viewport.apply();
            batch.begin();
            bitmapFont.draw(batch,"jogo pausado!",Gdx.graphics.getWidth()/2-50,Gdx.graphics.getHeight()/5);
            batch.draw(restartButton,restartButton.getX(),restartButton.getY());
            batch.end();
        }
    }
    public void restartGame(){
        this.dispose();
        MyGdxGame.ref.setScreen(new MainScreen());
    }

    private void checkRaceEnded() {
        if(car1.getFinished() && car2.getFinished()){
            this.music.stop();
            this.dispose();
            MyGdxGame.ref.setScreen(new ScoreboardScreen(car1,car2));
        }
    }

    private void checkFinishedRacers() {
        if(car1.getFinished() || car2.getFinished()){
            if(!car1.getFinished() && car1Lap>=totalLaps){
                car1.setFinalPosition(2);
                inputMultiplexer.removeProcessor(car1.carInputProcessor);
                car1.setFinished(true);
            }
            if(!car2.getFinished() && car2Lap>=totalLaps){
                car2.setFinalPosition(2);
                inputMultiplexer.removeProcessor(car2.carInputProcessor);
                car2.setFinished(true);
            }

        }
        else{
            if(car1Lap>=totalLaps){
                car1.setFinalPosition(1);
                inputMultiplexer.removeProcessor(car1.carInputProcessor);
                car1.setFinished(true);
            }
            if(car2Lap>=totalLaps){
                car2.setFinalPosition(1);
                inputMultiplexer.removeProcessor(car2.carInputProcessor);
                car2.setFinished(true);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        car1_img.dispose();
        car2_img.dispose();
        car1Braking_img.dispose();
        car2Braking_img.dispose();
        batch.dispose();
        music.dispose();
        bitmapFont.dispose();
        tiledmap.dispose();
        orthogonalTiledMapRenderer.dispose();
    }

    public void printNameTag(Car car,String name){
        bitmapFont.draw(batch, "" + name, car.getX()-car.getWidth()/2, car.getY()+car.getHeight()+15);
    }

}
