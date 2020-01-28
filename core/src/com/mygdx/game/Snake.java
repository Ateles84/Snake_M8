package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Snake implements Screen {

    final SnakeJoc joc;

    //DECLARACIO DE CARACTERISTIQUES
    static final String titol = "Snake_jgomez";
    static final int ample = 500;
    static final int alt = 500;
    static final int escala = 5;

    //DECLARACIO D'OBJECTES UTILS
    private SpriteBatch font;
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Texture textura;
    private Pixmap pixmap;
    private Sprite sprite;

    //DECLARACIO DE INPUT I MOVIMENT
    private Array<VecPosicioSerp> serp = new Array<>();
    private Array<VecPosicioSerp> començaSerp = new Array<>();
    private boolean esquerra,dreta,adalt,abaix;

    //DECLARACIO DEL MENJAR
    private Array<VecPosicioSerp> menjar = new Array<>();
    private VecPosicioSerp cantonada;
    private boolean dibuixarDe9;
    private Random aleatori = new Random();

    //DECLARACIO EINES PUNTUACIO
    static int puntuacio;
    static boolean fiPartida;

    //EINES UTILS PER LIBGDX
    boolean renderitzat;
    int compteRender;
    private BitmapFont bmFont;

    Snake(final SnakeJoc joc) {
        this.joc = joc;
        System.out.println("hola");
        //INICIALITZACIO DE VARIABLES
        camera = new OrthographicCamera();
        camera.setToOrtho(true, ample, alt);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(true, ample, alt);
        joc.batch.setProjectionMatrix(camera.combined);
        font = new SpriteBatch();
        font.setProjectionMatrix(fontCamera.combined);
        pixmap = new Pixmap(ample,alt, Pixmap.Format.RGBA8888);
        textura = new Texture(pixmap);
        sprite = new Sprite(textura);
        bmFont = new BitmapFont();

        //Definim el proessador de Input
        Gdx.input.setInputProcessor(new Control(this));

        carregarSerpInicial();

        reinicia();
    }

    void reinicia() { //Metode per a reiniciar la partida
        //Es vuiden totes les posicions posibles de la serp i es carreguen de de nou
        serp.clear();
        serp.addAll(començaSerp);
        adalt = true;   //Per defecte anirem cap adalt
        abaix = esquerra = dreta = false;

        pixmap.setColor(Color.BLACK); // Es pinta tot negge de nou
        pixmap.fill();

        for (VecPosicioSerp vecPosicioSerp: serp) {
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(vecPosicioSerp.x, vecPosicioSerp.y);
        }
        nouMenjar();
    }

    void nouMenjar() {
        //Eliminem el menjar anterior
        pixmap.setColor(Color.BLACK);
        for (VecPosicioSerp vecPosicioSerp: menjar) {
            pixmap.drawPixel(vecPosicioSerp.x,vecPosicioSerp.y);
        }
        menjar.clear();
        //Generem un nou menjar
        cantonada = new VecPosicioSerp(aleatori.nextInt(ample-3) + 1, aleatori.nextInt(alt -3) +1);
        menjar.add(cantonada);
        menjar.add(new VecPosicioSerp(cantonada.x +1, cantonada.y));
        menjar.add(new VecPosicioSerp(cantonada.x , cantonada.y+1));
        menjar.add(new VecPosicioSerp(cantonada.x + 1 , cantonada.y + 1));
        dibuixarDe9 = true;
    }

    void carregarSerpInicial() {
        //Inicialitzem la serp amb X 5 i la succecio vertical del 1 al 10
        començaSerp.add(new VecPosicioSerp(5,1));
        començaSerp.add(new VecPosicioSerp(5,2));
        començaSerp.add(new VecPosicioSerp(5,3));
        començaSerp.add(new VecPosicioSerp(5,4));
        començaSerp.add(new VecPosicioSerp(5,5));
        començaSerp.add(new VecPosicioSerp(5,6));
        començaSerp.add(new VecPosicioSerp(5,7));
        començaSerp.add(new VecPosicioSerp(5,8));
        començaSerp.add(new VecPosicioSerp(5,9));
        començaSerp.add(new VecPosicioSerp(5,10));
    }

    void controlaInput() {
        //Metode per anar controlant el que envia el controlador de input
        if (Control.esquerra && !dreta) {
            esquerra = true;
            adalt = abaix = dreta = false;
        }
        if (Control.dreta && !esquerra) {
            dreta = true;
            adalt = abaix = esquerra = false;
        }
        if (Control.abaix && !adalt) {
            abaix = true;
            adalt = esquerra = dreta = false;
        }
        if (Control.adalt && !abaix) {
            adalt = true;
            esquerra = abaix = dreta = false;
        }
    }

    void actualitza() {
        //Metode per actualitzar constantment la posicio de la serp
        if (esquerra) {
            serp.add(new VecPosicioSerp(serp.get(serp.size - 1).x - 1 , serp.get(serp.size - 1).y));
        }
        if (dreta) {
            serp.add(new VecPosicioSerp(serp.get(serp.size - 1).x + 1 , serp.get(serp.size - 1).y));
        }
        if (adalt) {
            serp.add(new VecPosicioSerp(serp.get(serp.size - 1).x , serp.get(serp.size - 1).y + 1));
        }
        if (abaix) {
            serp.add(new VecPosicioSerp(serp.get(serp.size - 1).x , serp.get(serp.size - 1).y -1));
        }

        //Comprobacio si la serp esta fora del mapa
        for (int i = 0; i < serp.size -1 ; i++) {
            if (serp.get(serp.size - 1).x < 0 || serp.get(serp.size - 1).x > ample
                    || serp.get(serp.size - 1).y < 0 || serp.get(serp.size - 1).y > alt) {
                fiPartida = true;
                break;
            }
        }

        //Comrpovacio si la serp es dona a si mateixa
        for (int i = 0; i < serp.size - 1 ; i++) {
            if (serp.get(serp.size -1).x == serp.get(i).x && serp.get(serp.size - 1).y == serp.get(i).y) {
                fiPartida = true;
                break;
            }
        }

        //Comprobar si ens hem menjat el menjar
        for (int i = 0 ; i < menjar.size ; i++) {
            if (serp.get(serp.size -1).x == menjar.get(i).x && serp.get(serp.size - 1).y == menjar.get(i).y) {
                nouMenjar();
                puntuacio++;
                break;
            }
        }

    }


    void renderPuntuacio(){
        font.begin();
        bmFont.setColor(Color.WHITE);
        bmFont.draw(font, "Game Over", 50, 80);
        bmFont.draw(font, Integer.toString(puntuacio), 75, 65);
        bmFont.draw(font, "Press SPACE", 52, 50);
        font.end();
        compteRender++;
        if (compteRender == 2){
            renderitzat = true;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (fiPartida && !renderitzat) {
            renderPuntuacio();
        }

        if (!fiPartida) { //Mentre s'estigui jugant, es processen les seguents ordres
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            controlaInput();
            actualitza();

            //Pintar el cap de la serp
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(serp.get(serp.size - 1 ).x , serp.get(serp.size -1 ).y);

            //Pintar la cua de la serp si es necessari
            if (!dibuixarDe9) {
                pixmap.setColor(Color.BLACK);
                pixmap.drawPixel(serp.get(0).x,serp.get(0).y) ;
                serp.removeIndex(0);
            }

            //Pintar la cua si es necessari
            if (dibuixarDe9) {
                for (VecPosicioSerp vecPosicioSerp: menjar) {
                    pixmap.drawPixel(vecPosicioSerp.x,vecPosicioSerp.y);
                }
                dibuixarDe9 = false;
            }

            textura.draw(pixmap, 0,0);
            joc.batch.begin();
            sprite.draw(joc.batch);
            joc.batch.end();
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

    }
}
