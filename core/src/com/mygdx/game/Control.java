package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class Control extends InputAdapter {

    Snake serp;

    Control(Snake serp) {
        this.serp = serp;
    }

    //Classe per a controlar l'input de l'usuari, tant amb les teclas com amb "swipes"

    static boolean adalt,abaix,esquerra,dreta;

    @Override
    public boolean keyDown(int idTecla) {
        if (idTecla == Keys.LEFT) {
            esquerra = true;
            adalt = abaix = dreta = false;
            System.out.println("tecla esquerra");
        }
        if (idTecla == Keys.RIGHT) {
            dreta = true;
            adalt = abaix = esquerra = false;
            System.out.println("tecla dreta");
        }
        if (idTecla == Keys.DOWN) {
            abaix = true;
            adalt = esquerra = dreta = false;
            System.out.println("tecla abaix");
        }
        if (idTecla == Keys.UP) {
            adalt = true;
            abaix = abaix = dreta = false;
            System.out.println("tecla adalt");
        }
        return true;
    }

    @Override
    public boolean keyUp(int idTecla) {
        if (idTecla == Keys.LEFT) esquerra = false;
        if (idTecla == Keys.RIGHT) dreta = false;
        if (idTecla == Keys.DOWN) abaix = false;
        if (idTecla == Keys.UP) adalt = false;
        return true;
    }

    /*
    boolean swipeDreta() {
        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()>0) {
            dreta = true;
            return true;
        }
        return false;
    }

    boolean swipeEsquerra() {
        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()<0) {
            dreta = true;
            return true;
        }
        return false;
    }

    boolean swipeAdalt() {
        if(Gdx.input.isTouched()&&Gdx.input.getDeltaY()>0) {
            adalt = true;
            return true;
        }
        return false;
    }

    boolean swipeAbaix() {
        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()>0) {
            abaix = true;
            return true;
        }
        return false;
    }
    */


}
