package com.mygdx.game.car;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    public Car(Texture img, Texture img_braking, int i, int i1, int i2, int i3,
               float carMaxVelocity,
               float carAccelerationTime,
               float carMaxBraking,
               float carHandling,
               final boolean isItTheRightCar) {
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
}
