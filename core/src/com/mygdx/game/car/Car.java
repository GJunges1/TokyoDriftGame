package com.mygdx.game.car;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
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
        ref = this;
    }

    public void update(float delta){
//        System.out.printf("v%.0f\t%.0f\n",carVelocity,carAcceleration);

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

        float oldX = getX(), oldY = getY(), tileWidht = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        if(this.carVelocity > 0.5){
            // *** CHECANDO COLISOES EM X ***

            //top left
            collisionX = collisionLayer.getCell((int)(getX() / tileWidht),(int)((getY() + getHeight()) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            //middle left
            if(!collisionX){ //se nao colidiu ainda, checar colisao
                collisionX = collisionLayer.getCell((int)(getX() / tileWidht),(int)((getY() + getHeight()/2) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            //bottom left
            if(!collisionX){ //se nao colidiu ainda, checar colisao
                collisionX = collisionLayer.getCell((int) (getX() / tileWidht), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            //top right
            if(!collisionX) { //se nao colidiu ainda, checar colisao
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidht), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            //middle right
            if(!collisionX) { //se nao colidiu ainda, checar colisao
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidht), (int) (((getY() + getHeight()) / 2) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            //bottom right
            if(!collisionX) { //se nao colidiu ainda, checar colisao
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidht), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // reagir a colisao em X
            if(collisionX){
                setX(oldX);
                this.carVelocity = 0;
            }

            // *** CHECANDO COLISOES EM Y ***
            //bottom left
            collisionY = collisionLayer.getCell((int) (getX() / tileWidht), (int) (getY() / tileHeight))
                    .getTile().getProperties().containsKey("blocked");

            //bottom middle
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()/2) / tileWidht), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            //bottom right
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidht), (int) (getY() / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            //top left
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) (getX() / tileWidht), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }
            //top middle
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()/2) / tileWidht), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            //top right
            if(!collisionY){
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidht), (int) ((getY() + getHeight()) / tileHeight))
                        .getTile().getProperties().containsKey("blocked");
            }

            // reagir a colisao em Y
            if(collisionY){
                setY(oldY);
                this.carVelocity = 0;
            }
        }

        timer+=delta;
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
