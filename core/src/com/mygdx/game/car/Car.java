package com.mygdx.game.car;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Car extends Sprite {
    public CarInputProcessor carInputProcessor;
    Car ref;

    // car constraints
    final float carMaxVelocity;
    final float carAccelerationTime;
    final float carMaxBraking;
    final float carFriction;
    final float carHandling;

    // car variables
    float carAcceleration;
    float carVelocity;
    float carAngularVelocity;

    float topL_X,topL_Y;
    float topR_X,topR_Y;
    float midL_X,midL_Y;
    float midR_X,midR_Y;
    float botL_X,botL_Y;
    float botR_X,botR_Y;
    float[] vertices;
    float oldX, oldY, oldRotation;
    float tileWidth,tileHeight;

    // car flags
    int carState;
    static final int carIsIdle=0;
    static final int carIsAccelerating=1;
    static final int carIsBraking=2;
    static final int carIsFrictioning=3;
    int movingDirection;

    // car timers
    float timer;
    Texture img, img_braking;
    TiledMapTileLayer collisionLayer; //TiledMapTileLayer

    public Car(Texture img, Texture img_braking, int i, int i1, int i2, int i3,
               float carMaxVelocity,
               float carAccelerationTime,
               float carMaxBraking,
               float carHandling,
               final boolean isItTheRightCar,
               TiledMapTileLayer collisionLayer) {
        super(img_braking, i, i1, i2, i3);
        this.img = img;
        this.img_braking = img_braking;
        carInputProcessor = new CarInputProcessor(this, isItTheRightCar); //WASD

        this.carMaxVelocity = Math.abs(carMaxVelocity); // a velocidade máxima
        this.carAccelerationTime=carAccelerationTime; // o tempo que o carro demora pra acelerar
        this.carHandling = Math.abs(carHandling); // o quanto o carro vira
        this.carMaxBraking = -Math.abs(carMaxBraking); // o quanto o carro freia
        this.carFriction = -40; // o quanto o carro sofre atrito do chão
        this.movingDirection=1;
        this.collisionLayer = collisionLayer;
        tileWidth = collisionLayer.getTileWidth();
        tileHeight = collisionLayer.getTileHeight();
        oldX = getX();
        oldY = getY();
        oldRotation = getRotation();
        ref = this;
    }

    public void update(float delta){
        // se o carro estiver se movendo, então ele pode virar:
        if(Math.abs(this.carVelocity)>=0.05) {
            this.rotate(carAngularVelocity*delta*movingDirection);
        }
        switch(carState){
            case carIsIdle:
                break;
            case carIsAccelerating:
                updateCarAcceleration();
                break;
            case carIsBraking:
                carAcceleration=movingDirection*carMaxBraking;
                if(carFinishedBrakingFrictioning() || (int)(Math.abs(carVelocity)/carVelocity)!=movingDirection){
                    carSetAccelerating();
                    movingDirection*=-1;
                    carVelocity=0;
                    carAcceleration=0;
                }
                break;
            case carIsFrictioning:
                carAcceleration=carFriction*movingDirection;
                if(carFinishedBrakingFrictioning()){
                    this.carState = carIsIdle; // ...encerra-se o atrito
                    this.carVelocity = 0; // ...zera-se a velocidade
                    this.carAcceleration = 0; // ...zera-se a aceleração
                }
                break;
        }
        this.carVelocity += this.carAcceleration * delta;
        float sine = (float)Math.sin(  Math.toRadians( -this.getRotation()) );
        float cosine = (float)Math.cos( Math.toRadians( -this.getRotation()) );
        this.setX(this.getX()+sine*this.carVelocity*delta);
        this.setY(this.getY()+cosine*this.carVelocity*delta);

        // Calculando vertices do carro
        vertices = this.getVertices();
        botL_X = vertices[SpriteBatch.X1];
        botL_Y = vertices[SpriteBatch.Y1];
        topL_X = vertices[SpriteBatch.X2];
        topL_Y = vertices[SpriteBatch.Y2];
        topR_X = vertices[SpriteBatch.X3];
        topR_Y = vertices[SpriteBatch.Y3];
        botR_X = vertices[SpriteBatch.X4];
        botR_Y = vertices[SpriteBatch.Y4];
        midR_X = (topR_X + botR_X)/2;
        midR_Y = (topR_Y + botR_Y)/2;
        midL_X = (topL_X + botL_X)/2;
        midL_Y = (topL_Y + botL_Y)/2;


        boolean collision;

//        if(this.carVelocity > 0.5){
        if(carState != carIsIdle){
            
            // *** CHECANDO COLISOES ***
            //top left

            collision = checkCollision(topL_X,topL_Y);

            //middle left
            if(!collision){ //se nao colidiu ainda, checar colisao
                collision = checkCollision(midL_X,midL_Y);
            }

            //bottom left
            if(!collision){ //se nao colidiu ainda, checar colisao
                collision = checkCollision(botL_X,botL_Y);
            }

            //top right
            if(!collision) { //se nao colidiu ainda, checar colisao
                collision = checkCollision(topR_X,topR_Y);
            }

            //middle right
            if(!collision) { //se nao colidiu ainda, checar colisao
                collision = checkCollision(midR_X,midR_Y);
            }

            //bottom right
            if(!collision) { //se nao colidiu ainda, checar colisao
                collision = checkCollision(botR_X,botR_Y);
            }

            // reagir a colisao em X
            if(collision){
                restoreLastCarState();
                carVelocity = 0;
                carAcceleration = 0;
                carState = carIsIdle;
            }
        }
        oldX = getX();
        oldY = getY();
        oldRotation = getRotation();
        timer+=delta;
    }
    void restoreLastCarState(){
        setX(oldX);
        setY(oldY);
        setRotation(oldRotation);
    }
    boolean checkCollision(float X, float Y){
        boolean value = collisionLayer.getCell((int)(X / tileWidth),
                (int)(Y / tileHeight))!=null ? true : false;
        return value;
    }
    private boolean carFinishedBrakingFrictioning(){
        return (Math.abs(carVelocity)<0.5);
    }

    public void draw(SpriteBatch batch, float delta) {
        super.draw(batch);
    }
    private void updateCarAcceleration(){
        // se o carro tiver mais rápido que a velocidade máxima, a aceleração é zero
        if(Math.abs(this.carVelocity)>=this.carMaxVelocity){
            this.carAcceleration=0;
            this.carVelocity = movingDirection * carMaxVelocity;
        }
        // se não, é dependente da velocidade
        else {
            this.carAcceleration = movingDirection * (carMaxVelocity - Math.abs(carVelocity)) /carAccelerationTime;
        }
    }

    private void carSetBraking(){
        carState=carIsBraking;
        this.setTexture(img_braking);
    }
    private void carSetAccelerating(){
        carState=carIsAccelerating;
        this.setTexture(this.img);
    }
    public void carSetForward(){
        if(this.carVelocity>-0.5){ // se o carro tiver indo pra frente ou parado
            movingDirection=1;
            carSetAccelerating();
        }
        else{ // se o carro tiver indo para trás
            movingDirection=-1;
            carSetBraking();
        }
    }

    public void carSetBackwards(){
        if(this.carVelocity>0.5){ // se o carro tiver indo pra frente
            movingDirection=1;
            carSetBraking();
        }
        else{ // se o carro tiver indo pra trás ou parado
            movingDirection=-1;
            carSetAccelerating();
        }
    }

    public void carSetIdle(){
        carState=carIsFrictioning;
        this.setTexture(this.img);
    }

    public void carPressLeft(){
        this.carAngularVelocity += + carHandling;
    }

    public void carPressRight(){
        this.carAngularVelocity += - carHandling;
    }
    public void carReleaseLeft(){
        this.carAngularVelocity -= + carHandling;
    }

    public void carReleaseRight(){
        this.carAngularVelocity -= - carHandling;
    }

    public void checkCollision(){

    }
}
