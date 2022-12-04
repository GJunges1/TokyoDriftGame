package com.mygdx.game.screens;
//package com.mygdx.game; //se o codigo acima der erro

import com.badlogic.gdx.Gdx;
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
import com.mygdx.game.car.Car;
import helper.TileMapHelper;

public class MainScreen implements Screen {
    public static MainScreen ref;
    public static Texture car1_img, img2, car2_img, car1Braking_img, car2Braking_img;
    OrthographicCamera camera;
    SpriteBatch batch;
    Car car1,car2;
    Viewport carViewport1;
    Viewport carViewport2;
    InputMultiplexer inputMultiplexer;
    BitmapFont bitmapFont;
    long startTime, totalSEC, hours, min ,sec;

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

        // *** START VIEWPORT *** //

        carViewport1 = new FitViewport(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/2, camera);
        carViewport2 = new FitViewport(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/2, camera);
        carViewport1.setScreenBounds(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        carViewport2.setScreenBounds(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        // *** END VIEWPORT *** //

        this.tiledmap = new TmxMapLoader().load("core/src/map/map.tmx");

        checkpointLayers = new TiledMapTileLayer[3];
        checkpointLayers[0] = (TiledMapTileLayer) tiledmap.getLayers().get("CHEGADA");
        checkpointLayers[1] = (TiledMapTileLayer) tiledmap.getLayers().get("CHECKPOINT1");
        checkpointLayers[2] = (TiledMapTileLayer) tiledmap.getLayers().get("CHECKPOINT2");

        //circuit = new Circuit(img2, 0, 0, img2.getWidth(), img2.getHeight());
        //circuit.setSize(circuit.getWidth(),circuit.getHeight());

        car1 = new Car(car1_img,car1Braking_img, 0, 0, car1_img.getWidth(), car1_img.getHeight(),
                800,
                6,
                400,
                100,
                false,
                (TiledMapTileLayer) tiledmap.getLayers().get("PAREDE"),
                checkpointLayers);
        car2 = new Car(car2_img, car2Braking_img, 0, 0, car2_img.getWidth(), car2_img.getHeight(),
                800,
                6,
                400,
                100,
                true,
                (TiledMapTileLayer) tiledmap.getLayers().get("PAREDE"),
                checkpointLayers);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(car1.carInputProcessor);
        inputMultiplexer.addProcessor(car2.carInputProcessor);

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

        NameTAG1 = "daniel";
        NameTAG2 = "junges";

        // *** START MUSIC *** //
        Music music = Gdx.audio.newMusic(Gdx.files.internal("tokyo_drift.wav"));
        music.setVolume(0.1f);
        music.setLooping(true);
        music.play();
        // *** END MUSIC *** //

        // *** START FONT *** //
        bitmapFont = new BitmapFont(Gdx.files.internal("TokyoDrift_font.fnt"));
        // *** END MUSIC *** //

        startTime = System.currentTimeMillis();

        totalLaps = 1; // definindo número de voltas da corrida
    }

    private void update(){
        orthogonalTiledMapRenderer.setView(camera);
    }

    @Override
    public void render(float delta){
        totalSEC = (System.currentTimeMillis() - startTime)/1000;
        sec = totalSEC % 60;
        min = (totalSEC % 3600) / 60;
        hours = totalSEC / 3600;

        int alturaTexto = 140;

        car1Lap = car1.getCarLap();
        car2Lap = car2.getCarLap();
        checkFinishedRacers();

        ScreenUtils.clear(Color.GREEN);

        // *** START BATCH CAR 1 ***
        batch.begin();
        this.update();
        carViewport1.apply();
        camera.position.set(car1.getX(),car1.getY(),0);
        camera.update();
        orthogonalTiledMapRenderer.render();

        car1.draw(batch, delta);
        car2.draw(batch, delta);
        batch.setProjectionMatrix(camera.combined);


        printTime(car1,hours,min,sec,alturaTexto);
        bitmapFont.draw(batch,"VOLTA" + "     "+car2Lap+" de "+totalLaps, car1.getX()+70, car1.getY()+alturaTexto+15);
        //printNameTag(car2,NameTAG1);

        batch.end();

        // *** END BATCH CAR 1 ***

        batch.setProjectionMatrix(camera.combined);

        // *** START BATCH CAR 2 ***
        batch.begin();
        this.update();

        carViewport2.apply();
        camera.position.set(car2.getX(),car2.getY(),0);
        camera.update();
        orthogonalTiledMapRenderer.render();

        car2.draw(batch, delta);
        car1.draw(batch, delta);
        batch.setProjectionMatrix(camera.combined);

        printTime(car2,hours,min,sec,alturaTexto);
        bitmapFont.draw(batch,"VOLTA" + "     "+car1Lap+" de "+totalLaps, car2.getX()+70, car2.getY()+alturaTexto+15);
        //printNameTag(car1,NameTAG2);

        batch.end();
        // *** END BATCH CAR 2 ***

        batch.setProjectionMatrix(camera.combined);


        car1.update(delta,car2); // UPDATE CARS POSITIONS
        car2.update(delta,car1); // '                   '
    }

    private void checkFinishedRacers() {
        if(car1.getFinished() || car2.getFinished()){
            if(!car1.getFinished() && car1Lap>=totalLaps){
                System.out.println("Car1 Finished!");
                inputMultiplexer.removeProcessor(car1.carInputProcessor);
                car1.setFinished(true);
            }
            if(!car2.getFinished() && car2Lap>=totalLaps){
                System.out.println("Car2 Finished!");
                inputMultiplexer.removeProcessor(car2.carInputProcessor);
                car2.setFinished(true);
            }

        }
        else{
            if(car1Lap>=totalLaps){
                System.out.println("Car1 Finished!");
                inputMultiplexer.removeProcessor(car1.carInputProcessor);
                car1.setFinished(true);
            }
            if(car2Lap>=totalLaps){
                System.out.println("Car2 Finished!");
                inputMultiplexer.removeProcessor(car2.carInputProcessor);
                car2.setFinished(true);
            }
            if(car1.getFinished() && car2.getFinished()){
                System.out.println("Draw!");
            }
            else if(car1.getFinished()){
                System.out.println("Car1 won the race!");
            }
            else if(car2.getFinished()){
                System.out.println("Car2 won the race");
            }
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        car1_img.dispose();
        car2_img.dispose();
        img2.dispose();
    }

    //PRINTAR CORRETAMENTE O TEMPO NO FORMATO 00.00.00
    public void printTime(Car car,long hours, long min, long sec,int alturaTexto){
        if(hours<10){
            if(min<10){
                if(sec<10){
                    bitmapFont.draw(batch, "TEMPO     " + "0" + hours + ".0" + min + ".0" + sec, car.getX()+36, car.getY()+30+alturaTexto);
                }
                else{
                    bitmapFont.draw(batch, "TEMPO     " + "0" + hours + ".0" + min + "." + sec, car.getX()+36, car.getY()+30+alturaTexto);
                }
            }
            else{
                if(sec<10){
                    bitmapFont.draw(batch, "TEMPO     " + "0" + hours + "." + min + ".0" + sec, car.getX()+36, car.getY()+30+alturaTexto);
                }
                else{
                    bitmapFont.draw(batch, "TEMPO     " + "0" + hours + "." + min + "." + sec, car.getX()+36, car.getY()+30+alturaTexto);
                }
            }
        }
        else{
            if(min<10){
                if(sec<10){
                    bitmapFont.draw(batch, "TEMPO     " + "" + hours + ".0" + min + ".0" + sec, car.getX()+36, car.getY()+30+alturaTexto);
                }
                else{
                    bitmapFont.draw(batch, "TEMPO     " + "" + hours + ".0" + min + "." + sec, car.getX()+36, car.getY()+30+alturaTexto);
                }
            }
            else{
                if(sec<10){
                    bitmapFont.draw(batch, "TEMPO     " + "" + hours + "." + min + ".0" + sec, car.getX()+36, car.getY()+30+alturaTexto);
                }
                else{
                    bitmapFont.draw(batch, "TEMPO     " + "" + hours + "." + min + "." + sec, car.getX()+36, car.getY()+30+alturaTexto);
                }
            }
        }
    }

    public void printNameTag(Car car,String name){
        bitmapFont.draw(batch, "" + name, car.getX()-car.getWidth()/2, car.getY()+car.getHeight()+15);
    }

}
