package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;

import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
    int manState = 0;
    int pause = 0;
    float velocity=0;
    float gravity = 0.2f;
    int manY = 0;
	Random random;
    Texture coin;
    ArrayList<Integer> coinxs;
    ArrayList<Integer> coinys;
    int coinCount=0;

	Texture bomb;
	ArrayList<Integer> bombxs;
	ArrayList<Integer> bombys;
	int bombCount=0;
	ArrayList<Rectangle> coinRectacle;
	ArrayList<Rectangle> bombRectacle;
	Rectangle manRectagle;
	int score = 0;
	int gameState = 0;
	BitmapFont font;
	Texture dizzy;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man= new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight()/2;
		random = new Random();
		coin = new Texture("coin.png");
		coinxs = new ArrayList<Integer>();
		coinys = new ArrayList<Integer>();

		bomb = new Texture("bomb.png");
		bombxs = new ArrayList<Integer>();
		bombys = new ArrayList<Integer>();
		coinRectacle = new ArrayList<Rectangle>();
		bombRectacle = new ArrayList<Rectangle>();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		dizzy = new Texture("dizzy-1.png");
		String rishabh = "RISHABH";
	}
	//coins
	public void makeCoin(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		coinys.add((int) height);
		coinxs.add(Gdx.graphics.getWidth());
	}

	//bomb
	public void makeBomb(){
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		bombys.add((int) height);
		bombxs.add(Gdx.graphics.getWidth());
	}





	@Override
	public void render () {
	    batch.begin();
	    batch.draw(background,0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	    if(gameState==1){
	        //game started
            if(coinCount<100){
                coinCount++;
            }else{
                coinCount=0;
                makeCoin();
            }
            coinRectacle.clear();
            for(int i=0; i<coinys.size();i++){
                batch.draw(coin, coinxs.get(i), coinys.get(i));
                coinRectacle.add(new Rectangle(coinxs.get(i), coinys.get(i), coin.getWidth(), coin.getHeight()));
                coinxs.set(i, coinxs.get(i)-4);

            }


            //bomb
            if(bombCount<250){
                bombCount++;
            }else{
                bombCount=0;
                makeBomb();
            }
            bombRectacle.clear();
            for(int i=0; i<bombys.size();i++){
                batch.draw(bomb, bombxs.get(i), bombys.get(i));
                bombRectacle.add(new Rectangle(bombxs.get(i), bombys.get(i), bomb.getWidth(), bomb.getHeight()));
                bombxs.set(i, bombxs.get(i)-4);
            }



            if(pause<8){
                pause++;
            }else{
                pause=0;
                if(manState<3){
                    manState++;
                }else{
                    manState=0;
                }
            }
            velocity+=gravity;
            manY-=velocity;
            if(manY<=0){
                manY=0;
            }
            if(manY>1000){
                manY=1000;
            }
            if(Gdx.input.justTouched()){
                velocity= -10;
            }
        }else if(gameState==0){
	        //game is starting
            if(Gdx.input.justTouched()){
                gameState=1;

            }
			score = 0;
        }else if(gameState==2){
	        //game over
			manY = Gdx.graphics.getHeight()/2;
			coinxs.clear();
			coinys.clear();
			coinRectacle.clear();
			bombxs.clear();
			bombys.clear();
			bombRectacle.clear();
			velocity = 0;
			coinCount = 0;
			bombCount=0;
			if(Gdx.input.justTouched()){
				score = 0;
				gameState=1;
			}

        }

		//coin
		if(gameState==2){
			batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manState].getWidth()/2, manY);

		}else{
			batch.draw(man[manState],Gdx.graphics.getWidth()/2-man[manState].getWidth()/2, manY);
		}

	    manRectagle = new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2, manY, man[manState].getWidth(), man[manState].getHeight());

	    for(int i=0;i<coinRectacle.size();i++){
	    	if(Intersector.overlaps(manRectagle, coinRectacle.get(i))){
	    		Gdx.app.log("Coin!!", "collision");
	    		score+=1;
	    		coinys.remove(i);
	    		coinxs.remove(i);
	    		coinRectacle.remove(i);
	    		break;
			}
		}

		for(int i=0;i<bombRectacle.size();i++){
			if(Intersector.overlaps(manRectagle, bombRectacle.get(i))){
				Gdx.app.log("Bomb!!", "collision");
				bombys.remove(i);
				bombxs.remove(i);
				bombRectacle.remove(i);
				gameState=2;
				break;
			}
		}

		font.draw(batch,String.valueOf(score),100,200);


        batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
