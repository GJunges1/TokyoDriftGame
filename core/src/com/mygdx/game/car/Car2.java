package com.mygdx.game.car;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Car2 extends Sprite {
    public CarInputProcessor2 carInputProcessor2;
    static Car2 ref;

    // Constantes do carro
    static float carMaxAcceleration;
    static float carMaxBraking;
    static float carFriction;
    static float carHandling;

    // Variáveis do carro
    static float carAcceleration;
    static float carVelocity;
    static float carAngularVelocity;

    // Flags do carro
    boolean carIsFrictioning;
    boolean carIsBraking;

    // Timers do carro
    float timer;

    public Car2(Texture img, Texture img_braking, int i, int i1, int i2, int i3) {
        super(img_braking, i, i1, i2, i3);
        this.img = img;
        this.img_braking = img_braking;
        carInputProcessor2 = new CarInputProcessor2(this); //setinhas

        this.carMaxAcceleration = 100; // o quanto o carro acelera
        this.carMaxBraking = -300; // o quanto o carro freia
        this.carFriction = -40; // o quanto o carro sofre atrito do chão
        this.carHandling = 100; // o quanto o carro vira
        ref = this;
    }

    public void update(float delta){

        // se o carro estiver se movendo, então ele pode virar:
        if(Math.abs(this.carVelocity)>=0.05) {

            // sinal para inverter a rotação se o carro estiver indo de ré
            int v_signal = (int) (this.carVelocity / Math.abs(carVelocity));

            this.rotate(carAngularVelocity*delta*v_signal);
        }

        // se o carro estiver sem acelerar nem frear:
        if(this.carIsFrictioning){
            // e a magnitude de sua velocidade for menor que 0.5, pára-se o carro, então...
            if(Math.abs(this.carVelocity) < 0.5){
                this.carIsFrictioning = false; // ...encerra-se o atrito
                this.carVelocity = 0; // ...zera-se a velocidade
                this.carAcceleration=0; // ...zera-se a aceleração
            }

        }
        this.carVelocity += this.carAcceleration * delta;
        if(this.carIsBraking){
            if(carFinishedBraking(carVelocity)){
                carAcceleration = (int)(Math.abs(carAcceleration)/carAcceleration)*this.getCarMaxAcceleration();
                carVelocity=0;
                carIsBraking=false;
            }
        }
        float sine = (float)Math.sin(  Math.toRadians( -this.getRotation()) );
        float cosine = (float)Math.cos( Math.toRadians( -this.getRotation()) );
        this.setX(this.getX()+sine*this.carVelocity*delta);
        this.setY(this.getY()+cosine*this.carVelocity*delta);
        timer+=delta;
    }
    private boolean carFinishedBraking(float currV){
        return (Math.abs(currV)<0.5);
    }
    public float getCarMaxAcceleration(){
        return this.carMaxAcceleration;
    }
    public float getCarFriction(){
        return this.carFriction;
    }

    public float getCarMaxBraking(){
        return this.carMaxBraking;
    }

    public float getCarHandling(){
        return this.carHandling;
    }
    public void draw(SpriteBatch batch, float delta) {
        super.draw(batch);
    }

    public void carSetForward(){
        this.carIsFrictioning=false;
        if(this.carVelocity>=0){ // se o carro tiver indo pra frente ou parado
            this.carAcceleration = this.getCarMaxAcceleration();
        }
        else{ // se o carro tiver indo para trás
            this.carIsBraking = true;
            this.carAcceleration = - this.getCarMaxBraking();
        }
    }

    public void carSetBackwards(){
        this.carIsFrictioning=false;
        if(this.carVelocity>0){ // se o carro tiver indo pra frente
            this.carIsBraking = true;
            this.carAcceleration = this.getCarMaxBraking();
        }
        else{
            this.carAcceleration = - this.getCarMaxAcceleration();
        }
    }

    public void carSetIdle(){
        this.carIsFrictioning=true;
        if(this.carVelocity>0){
            this.carAcceleration = this.getCarFriction();
        }
        else if(this.carVelocity<0){
            this.carAcceleration = - this.getCarFriction();
        }
        else{
            this.carAcceleration = 0;
        }
    }

    public void carPressLeft(){
        this.carAngularVelocity += + this.getCarHandling();
    }

    public void carPressRight(){
        this.carAngularVelocity += - this.getCarHandling();
    }
    public void carReleaseLeft(){
        this.carAngularVelocity -= + this.getCarHandling();
    }

    public void carReleaseRight(){
        this.carAngularVelocity -= - this.getCarHandling();
    }
}
