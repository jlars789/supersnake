import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Gamepanel extends JPanel implements Runnable, KeyListener { 
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 500, HEIGHT = 540; //window size
	
	private Thread thread;
	
	private boolean running;
	private boolean gameRunning; //allows the game to be started/stopped
	
	private Image slowMo;
	private Image highVoltage;
	private Image levelGround;
	
	private boolean right = true, left = false, up = false, down = false; //setting default movement
	
	private boolean x2points = false;
	private boolean phantom = false;
	private boolean alchem = false;
	private boolean scisDisp = false;
	private boolean feasts = false;
	private boolean phoenix = false;
	private boolean iFrames = false;
	private boolean autoPlay = false;
	
	private boolean tortoise = false;
	private boolean voltage = false;
	private boolean level = false;
	private boolean safety = false;
	private boolean diet = false;
	private boolean fortitude = false;
	
	private boolean saveVal = false;
	private boolean xpVal = false;
	private boolean leaderboard = false;
	private boolean pause = false;
	private boolean powerScreen = false;
	private boolean perkScreen;
	private boolean mainScreen = true;
	
	private boolean phoeBool = false;
	
	private int selector = 0;
	private int horiSelector = 0;
	
	private static int power1 = powerCall()[0];
	private static int power2 = powerCall()[1];
	private static int power3 = powerCall()[2];
	
	private static int perk1 = perkCall()[0];
	private static int perk2 = perkCall()[1];
	
	private ArrayList<String> powerString = new ArrayList<String>();
	private ArrayList<String> perkString = new ArrayList<String>();
	private ArrayList<Image> powerIMG = new ArrayList<Image>();
	private ArrayList<Image> perkIMG = new ArrayList<Image>();
	
	private BodyPart b;
	private ArrayList<BodyPart> snake;
	
	private Food food;
	private ArrayList<Food> foods;
	
	private Bomb bomb;
	private ArrayList<Bomb> bombs;
	
	private GoldFood x2point;
	private ArrayList<GoldFood> pwrUp1;
	
	private Phantom invis;
	private ArrayList<Phantom> pwrUp2;
	
	private Alchemy alchemy;
	private ArrayList<Alchemy>pwrUp3;
	
	private Scissors scissors;
	private ArrayList<Scissors>pwrUp4;
	
	private Feast feast;
	private ArrayList<Feast>pwrUp5;
	
	private Phoenix firebird;
	private ArrayList<Phoenix>pwrUp6;
	
	private SnakeBot snakeBot;
	private ArrayList<SnakeBot>pwrUp7;
	
	private Mystery mystery;
	private ArrayList<Mystery>pwrUp8;
	
	private int xCoor = 10, yCoor = 10, size = 5; //setting location and coordinate size, along with snake length
	private int ticks = 0;
	
	private static int points = 0;
	
	//powerup variables (durations)
	private int x2Dur = 0;
	private int x2Time = 0;
	private int phanDur = 0;
	private int phanTime = 0;
	private int alchDur = 0;
	private int alchTime = 0;
	private int scisTime = 0;
	private int scisDur = 0;
	private int feastTime = 0;
	private int feastDur = 0;
	private int phoeDur = 0;
	private int phoeTime = 0;
	private int iFrameDur = 0;
	private int phoeGFX = 0;
	private int botDur = 0;
	private int botTime = 0;
	private int shatterDur = 0;
	private int mystTime = 0;
	private int iFrameDurMult = 1;
	
	//extra variables
	private int chance = 0;
	private int bombVal = 0;
	private int hiddenFood = 0;
	private int feastsVal = 0;
	private Bomb shattered;
	private Bomb shattered2;
	
	boolean foodChase = true;
	boolean wallDodge = false;
	boolean bodyDodge = false;
	
	//level xp requirements
	private int playerLvl = 1;
	private float lvl2Req = 30000;
	private float lvl3Req = 75000;
	private float lvl4Req = 130000;
	private float lvl5Req = 200000;

	public Gamepanel() {
		
		setFocusable(true);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); //window size
		addKeyListener(this); //allows key input from user
		
		start();
		
		snake = new ArrayList <BodyPart>();
		foods = new ArrayList <Food>();
		bombs = new ArrayList <Bomb>();
		pwrUp1 = new ArrayList <GoldFood>();
		pwrUp2 = new ArrayList <Phantom>();
		pwrUp3 = new ArrayList <Alchemy>();
		pwrUp4 = new ArrayList <Scissors>();
		pwrUp5 = new ArrayList <Feast>();
		pwrUp6 = new ArrayList <Phoenix>();
		pwrUp7 = new ArrayList <SnakeBot>();
		pwrUp8 = new ArrayList <Mystery>();
	}
	
	public void start() {
		running = true; //allows the game to start
		thread = new Thread(this);
		thread.start();
	}
	
	public void gameStart() {
		gameRunning = true;
		mainScreen = false;
	}
	
	public void stop() {
		running = false; //stops the game from gameRunning
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Stopped");
	}
	
	public void gameStop() {
		gameRunning = false;
	}
	
	public void tick() throws InterruptedException {
		if(snake.size() == 0) { //sets location
			b = new BodyPart(xCoor, yCoor, 10);
			snake.add(b);
		}
		ticks++; //constant tick increase
		if(ticks > 750000) { //sets frame rate (higher = slower)
			
			try {
			Thread.sleep(60);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if(gameRunning && pause == false) {
				
				if(tortoise == false) {
					if(right) xCoor ++;
					if(left) xCoor --;
					if(up) yCoor --;
					if(down) yCoor ++;
				}
				else if(tortoise) {
					try {
						Thread.sleep(7);
					}catch(Exception e) {
						e.printStackTrace();
					}	
					if(right) xCoor ++;
					if(left) xCoor --;
					if(up) yCoor --;
					if(down) yCoor ++;
				}
				
				if(x2points) x2Dur++;
				if(phantom) phanDur++;
				if(alchem) alchDur++;
				if(scisDisp) scisDur++;
				if(feasts) feastDur++;
				if(phoenix) phoeDur++;
				if(autoPlay) botDur++;
				if(bombs.contains(shattered))shatterDur++;
				
				if(pwrUp1.size() > 0) x2Time++;
				if(pwrUp2.size() > 0) phanTime++;
				if(pwrUp3.size() > 0) alchTime++;
				if(pwrUp4.size() > 0) scisTime++;
				if(pwrUp5.size() > 0) feastTime++;
				if(pwrUp6.size() > 0) phoeTime++;
				if(pwrUp7.size() > 0) botTime++;
				if(pwrUp8.size() > 0) mystTime++;
				
				if(iFrames) iFrameDur++;
				
				if(phoeBool) phoeGFX++;
				
				b = new BodyPart(xCoor, yCoor, 10);
				snake.add(b);
				
				if(snake.size() > size) {
					snake.remove(0); //makes movement rather than "drawing"
				}	
			}
		}
		if(foods.size() == 0) { //sets food in window range(multiplies by 10)
			int xCoor = randomRange(0, 49);
			int yCoor = randomRange(4, 52);
			
			if(safety) {
				if(xCoor == 49) xCoor--;
				if(xCoor == 0) xCoor++;
				if(yCoor == 52) yCoor --;
				if(yCoor == 4) yCoor ++;
			}
			
			if(voltage == false) {
				chance = randomRange(0, 30);
			}
			else if(voltage) {
				chance = randomRange(0, 24);
			}
			
			food = new Food(xCoor, yCoor, 10);
			foods.add(food);
			
			if(bombs.size() < 50 && points % 4 == 0 && level == false || bombVal == 2 && level == false) {
				
				int xCoor2 = randomRange(0, 49);
				int yCoor2 = randomRange(4, 52);
				
				bomb = new Bomb(xCoor2, yCoor2, 10);
				bombs.add(bomb);
				bombVal = 0;
			}
			else if(bombs.size() < 50 && points % 5 == 0 && level || bombVal == 3 && level) {
				
				int xCoor2 = randomRange(0, 49);
				int yCoor2 = randomRange(4, 52);
				
				bomb = new Bomb(xCoor2, yCoor2, 10);
				bombs.add(bomb);
				bombVal = 0;
			}
		}
		if(alchem == false) {
			if(bombs.size() == 0) {
			bombs.add(bomb);
			}
		}
		if(power1 == 1 || power2 == 1 || power3 == 1) { //file value for double points is 1
			if(chance == 1) {
				
				int xCoor = randomRange(0, 49);
				int yCoor = randomRange(4, 52);
				
				x2point = new GoldFood(xCoor, yCoor, 10);
				pwrUp1.add(x2point);
				
				if(voltage == false) {
					chance = randomRange(0, 30);
				}
				else if(voltage) {
					chance = randomRange(0, 24);
				}
			}
		}	
		if(power1 == 2 || power2 == 2 || power3 == 2) { //file value for phantom is 2
			if(chance == 2) {
				int xCoor = randomRange(0, 49);
				int yCoor = randomRange(4, 52);
				
				invis = new Phantom(xCoor, yCoor, 10);
				pwrUp2.add(invis);
				
				if(voltage == false) {
					chance = randomRange(0, 30);
				}
				else if(voltage) {
					chance = randomRange(0, 24);
				}
			}
		}	
		if(power1 == 3 || power2 == 3 || power3 == 3) { //file value for alchemy is 3
			if(chance == 3) {
				int xCoor = randomRange(0, 49);
				int yCoor = randomRange(4, 52);
				
				alchemy = new Alchemy(xCoor, yCoor, 10);
				pwrUp3.add(alchemy);
				
				if(voltage == false) {
					chance = randomRange(0, 30);
				}
				else if(voltage) {
					chance = randomRange(0, 24);
				}
			}
		}
		if(power1 == 4 || power2 == 4 || power3 == 4) { //file value for scissors is 4
			if(snake.size() > 5) {
				if(chance == 4) {
					int xCoor = randomRange(0, 49);
					int yCoor = randomRange(4, 52);
					
					scissors = new Scissors(xCoor, yCoor, 10);
					pwrUp4.add(scissors);
					
					if(voltage == false) {
						chance = randomRange(0, 30);
					}
					else if(voltage) {
						chance = randomRange(0, 24);
					}
				}
			}
		}
		
		if(power1 == 5 || power2 == 5 || power3 == 5) { //file value for feast is 5
			if(chance == 5) {
				int xCoor = randomRange(0, 49);
				int yCoor = randomRange(4, 52);
				
				feast = new Feast(xCoor, yCoor, 10);
				pwrUp5.add(feast);
				
				if(voltage == false) {
					chance = randomRange(0, 30);
				}
				else if(voltage) {
					chance = randomRange(0, 24);
				}
				
			}
		}
		
		if(power1 == 6 || power2 == 6 || power3 == 6) { //file value for phoenix is 6
			if(chance == 6) {
				int xCoor = randomRange(0, 49);
				int yCoor = randomRange(4, 52);
				
				firebird = new Phoenix(xCoor, yCoor, 10);
				pwrUp6.add(firebird);
				
				if(voltage == false) {
					chance = randomRange(0, 30);
				}
				else if(voltage) {
					chance = randomRange(0, 24);
				}
			}
		}
		
		if(power1 == 7 || power2 == 7 || power3 == 7) { //file value for snakeBot is 7
			if(chance == 7) {
				int xCoor = randomRange(0, 49);
				int yCoor = randomRange(4, 52);
				
				snakeBot = new SnakeBot(xCoor, yCoor, 10);
				pwrUp7.add(snakeBot);
				
				if(voltage == false) {
					chance = randomRange(0, 30);
				}
				else if(voltage) {
					chance = randomRange(0, 24);
				}
			}
		}
		if(power1 == 8 || power2 == 8 || power3 == 8) {
			if(chance == 8) {
				int xCoor = randomRange(0, 49);
				int yCoor = randomRange(4, 52);
				
				mystery = new Mystery(xCoor, yCoor, 10);
				pwrUp8.add(mystery);
				
				if(voltage == false) {
					chance = randomRange(0, 30);
				}
				else if(voltage) {
					chance = randomRange(0, 24);
				}
			}
		}
		
		if(perk1 == 1|| perk2 == 1) tortoise = true;
		else tortoise = false;
		
		if(perk1 == 2|| perk2 == 2) voltage = true;
		else voltage = false;
		
		if(perk1 == 3|| perk2 == 3) level = true;
		else level = false;
		
		if(perk1 == 4 || perk2 == 4) safety = true;
		else safety = false;
		
		if(perk1 == 5 || perk2 == 5) diet = true;
		else diet = false;
		
		if(perk1 == 6 || perk2 == 5) fortitude = true;
		else fortitude = false;
		
		if(power1 == 1|| power2 == 1 || power3 == 1) {
			for (int i = 0 ; i < pwrUp1.size(); i++) { //detects contact with double points
				if(xCoor == pwrUp1.get(i).getxCoor() && yCoor == pwrUp1.get(i).getyCoor()) {
					x2Dur = 0;
					x2Time = 0;
					pwrUp1.remove(i);
					x2points = true;
				}
			}
		}
		if(power1 == 2 || power2 == 2 || power3 == 2) {
			for (int i = 0 ; i < pwrUp2.size(); i++) { //detects contact with phantom
				if(xCoor == pwrUp2.get(i).getxCoor() && yCoor == pwrUp2.get(i).getyCoor()) {
					phanDur = 0;
					phanTime = 0;
					pwrUp2.remove(i);
					phantom = true;
				}
			}
		}
		if(power1 == 3 || power2 == 3 || power3 == 3) {
			for (int i = 0 ; i < pwrUp3.size(); i++) { //detects contact with alchemy
				if(xCoor == pwrUp3.get(i).getxCoor() && yCoor == pwrUp3.get(i).getyCoor()) {
					alchDur = 0;
					alchTime = 0;
					pwrUp3.remove(i);
					alchem = true;
				}
			}
		}
		if(power1 == 4 || power2 == 4 || power3 == 4) {
			for (int i = 0 ; i < pwrUp4.size(); i++) { //detects contact with scissors
				if(xCoor == pwrUp4.get(i).getxCoor() && yCoor == pwrUp4.get(i).getyCoor()) {
					pwrUp4.remove(i);
					scisTime = 0;
					scisDur = 0;
					scisDisp = true;
					if(snake.size() >= 13 && size >= 13) {
						for(int j = 0; j < 7; j++) {
							snake.remove(j);
							size--;
						}
						
					}
					else {
						for(int v = 0; v < 2; v++) {
							snake.remove(v);
							size--;
						}
					}
				}
			}
		}
		if(power1 == 6 || power2 == 6 || power3 == 6) {
			for (int i = 0 ; i < pwrUp6.size(); i++) { //detects contact with phoenix
				if(xCoor == pwrUp6.get(i).getxCoor() && yCoor == pwrUp6.get(i).getyCoor()) {
					phoeDur = 0;
					phoeTime = 0;
					pwrUp6.remove(i);
					phoenix = true;
				}
			}
		}
		if(power1 == 5 || power2 == 5 || power3 == 5) {
			for (int i = 0 ; i < pwrUp5.size(); i++) { //detects contact with feasts
				if(xCoor == pwrUp5.get(i).getxCoor() && yCoor == pwrUp5.get(i).getyCoor()) {
					feastDur = 0;
					feastTime = 0;
					pwrUp5.remove(i);
					feasts = true;
				}
			}
		}
		if(power1 == 7 || power2 == 7 || power3 == 7) {
			for (int i = 0 ; i < pwrUp7.size(); i++) { //detects contact with snakeBot
				if(xCoor == pwrUp7.get(i).getxCoor() && yCoor == pwrUp7.get(i).getyCoor()) {
					botDur = 0;
					botTime = 0;
					pwrUp7.remove(i);
					autoPlay = true;
				}
			}
		}
		if(power1 == 8 || power2 == 8 || power3 == 8) {
			for(int i = 0; i < pwrUp8.size(); i++) { //detects contact with mystery
				if(xCoor == pwrUp8.get(i).getxCoor() && yCoor == pwrUp8.get(i).getyCoor()) {
					int mystVar = randomRange(0, 70);
					pwrUp8.remove(i);
					mystTime = 0;
					if(mystVar >= 60) {
						botDur = 0;
						autoPlay = true;
					}
					else if(mystVar >= 50) {
						phoeDur = 0;
						phoenix = true;
					}
					else if(mystVar >= 40) {
						feastDur = 0;
						feasts = true;
					}
					else if(mystVar >= 30) {
						scisDur = 0;
						scisDisp = true;
						if(snake.size() >= 13 && size >= 13) {
							for(int j = 0; j < 7; j++) {
								snake.remove(j);
								size--;
							}
							
						}
						else {
							for(int v = 0; v < 2; v++) {
								snake.remove(v);
								size--;
							}
						}
					}
					else if(mystVar >= 20) {
						alchDur = 0;
						alchem = true;
					}
					else if(mystVar >= 10) {
						phanDur = 0;
						phantom = true;
					}
					else if(mystVar >= 0) {
						x2Dur = 0;
						x2points = true;
					}
					
				}
			}
		}
		
		if(feasts) {
			int xCoor = randomRange(0, 49);
			int yCoor = randomRange(4, 52);
			food = new Food(xCoor, yCoor, 10);
			foods.add(1, food);
			if(bombs.size() < 50 && feastsVal == 2 && level == false|| bombVal == 2 && level == false) {
				
				int xCoor2 = randomRange(0, 49);
				int yCoor2 = randomRange(4, 52);
					
				bomb = new Bomb(xCoor2, yCoor2, 10); 
				bombs.add(bomb);
				bombVal = 0;
				feastsVal = 0;
			}
			else if(bombs.size() < 50 && feastsVal == 3 && level|| bombVal == 3 && level == false) {
				
				int xCoor2 = randomRange(0, 49);
				int yCoor2 = randomRange(4, 52);
					
				bomb = new Bomb(xCoor2, yCoor2, 10); 
				bombs.add(bomb);
				bombVal = 0;
				feastsVal = 0;
			}
		}
		if(feasts == false && hiddenFood == 0) {
			if(foods.size() > 1) {
				foods.remove(1);
			}
		}
		if(feasts == false && hiddenFood > 0) {
			if(foods.size() > (hiddenFood + 1)) {
				foods.remove(hiddenFood - 1);
			}
		}
		
		if(autoPlay) {
				if(foodChase && bodyDodge == false) {
					for(int i = 0 ; i < foods.size(); i++) {
						if(xCoor > foods.get(i).getxCoor()) {
							if(!right) left();
							if(right) down();
						}
						else if(xCoor < foods.get(i).getxCoor()) {
							if(!left) right();
							if(left) up();
						}
						else if(xCoor == foods.get(i).getxCoor()) {
							if(yCoor > foods.get(i).getyCoor()) {
								if(!down) up();
								if(down) right();
							}
							else if(yCoor < foods.get(i).getyCoor()) {
								if(!up) down();
								if(up) left();
							}
						}
						for(int s = 0 ; (s < snake.size() - 2); s++) {
							if((foods.get(i).getxCoor() - xCoor) < 0 && right) {
								bodyDodge = true;
							}
							if((foods.get(i).getxCoor() - xCoor) > 0 && left) {
								bodyDodge = true;
							}
							if((foods.get(i).getyCoor() - yCoor) < 0 && down) {
								bodyDodge = true;
							}
							if((foods.get(i).getyCoor() - yCoor) > 0 && up) {
								bodyDodge = true;
							}
							if(foods.get(i).getxCoor() == snake.get(s).getxCoor() && foods.get(i).getyCoor() == snake.get(s).getyCoor()) {
								bodyDodge = true;
							}
							if(yCoor == snake.get(s).getyCoor()) {
								if(xCoor - 1 == snake.get(s).getxCoor() && left) {
									bodyDodge = true;
								}
								if(xCoor + 1 == snake.get(s).getxCoor() && right) {
									bodyDodge = true;
								}				
							}
							if(xCoor == snake.get(s).getxCoor()) {
								if(yCoor - 1 == snake.get(s).getyCoor() && up){
									bodyDodge = true;
								}
								if(yCoor + 1 == snake.get(s).getyCoor() && down) {
									bodyDodge = true;
								}
							}
						}
						if((xCoor == 0 && left)|| (xCoor == 49 && right) || (yCoor == 4 && up) || (yCoor == 54 && down)) {
							wallDodge = true;
						}
					}
				}
				
				if(bodyDodge && wallDodge == false) {
					for(int i = 0; i < snake.size() - 1; i++) {
						if(yCoor == snake.get(i).getyCoor()) {
							if(xCoor + 1 == snake.get(i).getxCoor() || xCoor - 1 == snake.get(i).getxCoor()) {
								for(int j = snake.indexOf(snake.get(i)); j < (snake.size() - 2); j++) {
									int result = yCoor - snake.get(j).getyCoor();
									if((result < 0) && !down) {
										up();
									}
									else if((result > 0) && !up) {
										down();
									}
								}
							}
						}
						else {
							bodyDodge = false;
						}
						if(xCoor == snake.get(i).getxCoor()) {
							if(yCoor + 1 == snake.get(i).getyCoor() || yCoor - 1 == snake.get(i).getyCoor()) {
								for(int j = snake.indexOf(snake.get(i)); j < (snake.size() - 2); j++) {
									int result = xCoor - snake.get(j).getxCoor();
									if((result < 0) && !right) {
										left();
									}
									else if((result > 0) && !left) {
										right();
									}
								}
							}
							else {
								bodyDodge = false;
							}
						}
					}
				}
				if(wallDodge) {
					if(xCoor == 0 && yCoor > 4 && yCoor < 54 && left && !down) {
						up();
					}
					else if(xCoor == 0 && yCoor == 4 && !left) {
						right();
					}
					else if(xCoor == 0 && yCoor == 4 && !up) {
						down();
					}
					
					if(xCoor == 49 && yCoor > 4 && yCoor < 54 && right && !up) {
						down();
					}
					else if(xCoor == 49 && yCoor == 54 && !right) {
						left();
					}
					else if(xCoor == 49 && yCoor == 54 && !down) {
						up();
					}
					
					if(yCoor == 4 && up && !left) {
						right();
					}
					else if(yCoor == 4 && xCoor == 49 && !up) {
						down();
					}
					else if(yCoor == 4 && xCoor == 49 && !right) {
						left();
					}
					
					if(yCoor == 54 && down && !right) {
						left();
					}
					else if(yCoor == 54 && xCoor == 0 && !down) {
						up();
					}
					else if(yCoor == 54 && xCoor == 0 && !left) {
						right();
					}
					else {
						wallDodge = false;
					}
				}	
			}				
		//powerup timers
		
			if(x2Dur > 250) {
				x2points = false;
				bombVal = 0;
			}
			if(x2Time > 175) {
				pwrUp1.clear();
				x2Time = 0;
			}
			
			if(phanDur > 250) {
				phantom = false;
			}
			if(phanTime > 175) {
				pwrUp2.clear();
				phanTime = 0;
			}
			
			if(alchDur > 83) {
				alchem = false;
			}
			if(alchTime > 175) {
				pwrUp3.clear();
				alchTime = 0;
			}
			
			if(scisDur > 60) {
				scisDisp = false;
			}
			if(scisTime > 175) {
				pwrUp4.clear();
				scisTime = 0;
			}
			
			if(feastDur > 60) {
				feasts = false;
			}
			if(feastTime > 175) {
				pwrUp5.clear();
				feastTime = 0;
			}
			
			if(phoeDur > 1000) {
				phoenix = false;
			}
			if(phoeTime > 175) {
				pwrUp6.clear();
				phoeTime = 0;
			}
			
			if(iFrameDur > (10 * iFrameDurMult)) {
				iFrames = false;
				iFrameDurMult = 1;
			}
			
			if(phoeGFX > 50) {
				phoeBool = false;
			}
			if(botDur > 340) {
				iFrames = true;
				autoPlay = false;
				botDur = 0;
			}
			if(botTime > 175) {
				pwrUp7.clear();
				botTime = 0;
			}
			if(shatterDur > 50) {
				bombs.remove(shattered);
				bombs.remove(shattered2);
			}
		
		if(iFrames == false) iFrameDur = 0;
		if(phoeBool == false) phoeGFX = 0;
		
		//prevents two of the same powerup from existing at once
		if(pwrUp2.size() > 1) pwrUp2.remove(pwrUp2.size() - 1);
		if(pwrUp1.size() > 1) pwrUp1.remove(pwrUp1.size() - 1);
		if(pwrUp3.size() > 1) pwrUp3.remove(pwrUp3.size() - 1);
		if(pwrUp4.size() > 1) pwrUp4.remove(pwrUp4.size() - 1);
		if(pwrUp5.size() > 1) pwrUp5.remove(pwrUp5.size() - 1);
		if(pwrUp6.size() > 1) pwrUp6.remove(pwrUp6.size() - 1);
		if(pwrUp7.size() > 1) pwrUp7.remove(pwrUp7.size() - 1);
		if(pwrUp8.size() > 1) pwrUp8.remove(pwrUp8.size() - 1);
		
		for (int i = 0 ; i < foods.size(); i++) { //detects contact with food
			if(xCoor == foods.get(i).getxCoor() && yCoor == foods.get(i).getyCoor()) {
				int chanceVar = 4;
				if(diet) chanceVar = 0;
				int dietChance = randomRange(chanceVar, 8);
				
				if(dietChance > 2) {
					size ++;
				}
				if(fortitude) {
					iFrames = true;
					iFrameDur = 0;
				}
				
				foods.remove(i);
				i++;
				if(feasts) {
					feastsVal++;
				}
				else if(feasts == false) {
					feastsVal = 0;
				}
				
				if(x2points == false) {
					points++;
				}
				else if(x2points) {
					points = points + 2;
					
					if(points % 2 != 0) {
						bombVal++;
					}
				}
			}
			
		}
		
		if(phantom == false && iFrames == false) {
			
			if(alchem == false) {
			
				for (int i = 0 ; i < bombs.size(); i++) { //detects contact with bombs
					for (int j = 0 ; j < foods.size(); j++) {
						if(foods.get(j).getxCoor() == bombs.get(i).getxCoor() && foods.get(j).getyCoor() == bombs.get(i).getyCoor()) {
							foods.add(food);
							hiddenFood++;
						}
					}
					if(power1 == 1 || power2 == 1|| power3 == 1) {
						for (int j = 0 ; j < pwrUp1.size(); j++) {
							if(pwrUp1.get(j).getxCoor() == bombs.get(i).getxCoor() && pwrUp1.get(j).getyCoor() == bombs.get(i).getyCoor()) {
								pwrUp1.clear();
							}
						}
					}
					if(power1 == 2 || power2 == 2|| power3 == 2) {
						for (int j = 0 ; j < pwrUp2.size(); j++) {
							if(pwrUp2.get(j).getxCoor() == bombs.get(i).getxCoor() && pwrUp2.get(j).getyCoor() == bombs.get(i).getyCoor()) {
								pwrUp2.clear();
							}
						}
					}
					if(power1 == 3 || power2 == 3 || power3 == 3){
						for (int j = 0 ; j < pwrUp3.size(); j++) {
							if(pwrUp3.get(j).getxCoor() == bombs.get(i).getxCoor() && pwrUp3.get(j).getyCoor() == bombs.get(i).getyCoor()) {
								pwrUp3.clear();
							}
						}
					}
					if(power1 == 4||power2 == 4|| power3 == 4) {
						for (int j = 0 ; j < pwrUp4.size(); j++) {
							if(pwrUp4.get(j).getxCoor() == bombs.get(i).getxCoor() && pwrUp4.get(j).getyCoor() == bombs.get(i).getyCoor()) {
								pwrUp4.clear();
							}
						}
					}
					if(power1 == 5 || power2 == 5 || power3 == 5) {
						for (int j = 0 ; j < pwrUp5.size(); j++) {
							if(pwrUp5.get(j).getxCoor() == bombs.get(i).getxCoor() && pwrUp5.get(j).getyCoor() == bombs.get(i).getyCoor()) {
								pwrUp5.clear();
							}
						}
					}
					if(power1 == 6 || power2 == 6 || power3 == 6) {
						for (int j = 0 ; j < pwrUp6.size(); j++) {
							if(pwrUp6.get(j).getxCoor() == bombs.get(i).getxCoor() && pwrUp6.get(j).getyCoor() == bombs.get(i).getyCoor()) {
								pwrUp6.clear();
							}
						}
					}
					if(power1 == 7 || power2 == 7|| power3 == 7) {
						for (int j = 0 ; j < pwrUp7.size(); j++) {
							if(pwrUp7.get(j).getxCoor() == bombs.get(i).getxCoor() && pwrUp7.get(j).getyCoor() == bombs.get(i).getyCoor()) {
								pwrUp7.clear();
							}
						}
					}
					if(power1 == 8|| power2 == 8|| power3 == 8) {
						for (int j = 0 ; j < pwrUp8.size(); j++) {
							if(pwrUp8.get(j).getxCoor() == bombs.get(i).getxCoor() && pwrUp8.get(j).getyCoor() == bombs.get(i).getyCoor()) {
								pwrUp8.clear();
							}
						}
					}
					if(autoPlay == false) {
						if(xCoor == bombs.get(i).getxCoor() && yCoor == bombs.get(i).getyCoor()) {
							if(phoenix == false) {
								gameRunning = false;
								if(xpFetch() <= lvl5Req) {
									if(xpVal == false) {
										xpTrack();
										xpVal = true;
									}
								}
							}
							if(phoenix) {
								respawn();
							}						
						}
					}
					else if(autoPlay) {
						if(xCoor == bombs.get(i).getxCoor() && yCoor == bombs.get(i).getyCoor()) {
							shatterDur = 0;
							shattered = bombs.get(i);
							if(bombs.contains(shattered)) {
								shattered2 = bombs.get(i);
							}
						}
					}
				}
			}
			
			if(alchem) {
				for (int i = 0 ; i < bombs.size(); i++) { //bombs grant points during alchemy
					if(xCoor == bombs.get(i).getxCoor() && yCoor == bombs.get(i).getyCoor()) {
						size ++;
						bombs.remove(i);
						i++;
						points++;
					}
				}
			}
			
			//player body collision
			for(int i = 0 ; (i < snake.size() - 1); i++) {
				if(xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
					if(i != snake.size() - 1) {
						if(phoenix == false) {
							gameRunning = false;
							if(xpFetch() <= lvl5Req) {
								if(xpVal == false) {
									xpTrack();
									xpVal = true;
								}
							}
						}
						else if(phoenix) {
							respawn();
						}
					}
				}
			}
			//border collision
			if(xCoor < 0 || xCoor > 49 || yCoor < 4 || yCoor > 53) {
				if(phoenix == false) {
					gameRunning = false;
					if(xpFetch() <= lvl5Req) {
						if(xpVal == false) {
							xpTrack();
							xpVal = true;
						}
					}
				}
				else if(phoenix) {
					respawn();
				}
			}
		}
		if(phantom || iFrames) {
			if(xCoor < 0) {
				xCoor = 50;
			}
			if(xCoor > 50) {
				xCoor = 0;
			}
			if(yCoor < 3) {
				yCoor = 54;
			}
			if(yCoor > 54) {
				yCoor = 3;
			}
		}
	}

	//gfx method
	public void paintComponent(Graphics g) throws ArrayIndexOutOfBoundsException, NullPointerException{
		Image doublePointsSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\doublePointsSprite.png").getImage();
		Image phantomSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phantomSprite.png").getImage();
		Image alchemySprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\alchemySprite.png").getImage();
		Image scissorsSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\scissorsSprite.png").getImage();
		Image feastSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\feastSprite.png").getImage();
		Image phoeSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixSprite.png").getImage();
		Image snakeBotSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\snakeBotSprite.png").getImage();
		Image mysterySprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\mysterySprite.png").getImage();
		
		Image tortoiseSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\SlowMoIcon.png").getImage();
		Image highVoltageSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\highVoltageIcon.png").getImage();
		Image levelGroundSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\levelGroundIcon.png").getImage();
		Image safeZoneSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\safeZoneIcon.png").getImage();
		Image dietSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\dietIcon.png").getImage();
		Image fortitudeSprite = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\fortitudeIcon.png").getImage();
		
		Image descripBackdrop = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\descriptionBackdrop.png").getImage();
		Image dimmer = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\dimmer.png").getImage();
		
		//power 1 string set
		powerString.add(0, "None");
		powerString.add(1, "Double Points");
		powerString.add(2, "Phantom");
		powerString.add(3, "Alchemy");
		powerString.add(4, "Scissors");
		powerString.add(5, "Feast");
		powerString.add(6, "Phoenix");
		powerString.add(7, "SnakeBot");
		powerString.add(8, "Mystery");
		
		perkString.add(0, "None");
		perkString.add(1, "Tortoise");
		perkString.add(2, "High Voltage");
		perkString.add(3, "Level Ground");
		perkString.add(4, "Safe Zone");
		perkString.add(5, "Low Calorie");
		perkString.add(6, "Fortitude");
		
		powerIMG.add(0, null);
		powerIMG.add(1, doublePointsSprite);
		powerIMG.add(2, phantomSprite);
		powerIMG.add(3, alchemySprite);
		powerIMG.add(4, scissorsSprite);
		powerIMG.add(5, feastSprite);
		powerIMG.add(6, phoeSprite);
		powerIMG.add(7, snakeBotSprite);
		powerIMG.add(8, mysterySprite);
		
		perkIMG.add(0, null);
		perkIMG.add(1, tortoiseSprite);
		perkIMG.add(2, highVoltageSprite);
		perkIMG.add(3, levelGroundSprite);
		perkIMG.add(4, safeZoneSprite);
		perkIMG.add(5, dietSprite);
		perkIMG.add(6, fortitudeSprite);
		
		//setting coordinate grid and coloring background
		super.paintComponent(g);
		if(gameRunning == false) {
			Image mainBackground = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\mainBackground.png").getImage();
			g.drawImage(mainBackground, 0, 0, null);
		}
		
		if(mainScreen == false) {
			Image background = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\desertBackground.png").getImage();
			g.drawImage(background, 0, 40, null);
			
			if(phoeBool) {
				if(phoeGFX < 2) {
					Image phoenix = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixWingDown.png").getImage();
					g.drawImage(phoenix, 200, 250, null);
				}
				else if(phoeGFX < 4) {
					Image phoenix = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixWingSide.png").getImage();
					g.drawImage(phoenix, 200, 210, null);
				}
				else if(phoeGFX < 6) {
					Image phoenix = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixWingUp.png").getImage();
					g.drawImage(phoenix, 200, 170, null);
				}
				else if(phoeGFX < 8) {
					Image phoenix = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixWingUpFade1.png").getImage();
					g.drawImage(phoenix, 200, 130, null);
				}
				else if(phoeGFX < 10) {
					Image phoenix = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixWingUpFade2.png").getImage();
					g.drawImage(phoenix, 200, 90, null);
				}
				else if(phoeGFX < 12) {
					Image phoenix = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixWingUpFade3.png").getImage();
					g.drawImage(phoenix, 200, 50, null);
				}
				else if(phoeGFX < 14) {
					Image phoenix = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixWingUpFade4.png").getImage();
					g.drawImage(phoenix, 200, 10, null);
				}
			}
			
			if(iFrameDur % 5 < 4) {
				
				if(snake.size() > 1) {
					
					if(snake.get(0).getxCoor() < snake.get(1).getxCoor()) { //if tail xCoor is less than next segment
						if(phantom == false && autoPlay == false) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).tailRight(g); //set tail segment to right
							}
						}
						else if(autoPlay) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).botTailRight(g);
							}
						}
						
						else if(phantom){
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).phanTailRight(g);
							}
						}
					}
					if(snake.get(0).getxCoor() > snake.get(1).getxCoor()) { //if tail xCoor is greater than next segment
						if(phantom == false && autoPlay == false) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).tailLeft(g); //set tail segment to left
							}
						}
						else if(autoPlay) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).botTailLeft(g);
							}
						}
						
						else if(phantom) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).phanTailLeft(g);
							}
						}
					}
					if(snake.get(0).getyCoor() < snake.get(1).getyCoor()) { //if tail segment yCoor is less than next segment
						if(phantom == false && autoPlay == false) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).tailDown(g); //set tail segment to down
							}
						}
						else if(autoPlay) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).botTailDown(g); 
							}
						}
						
						else if(phantom) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).phanTailDown(g); 
							}
						}
					}
					if(snake.get(0).getyCoor() > snake.get(1).getyCoor()) { //if tail segment yCoor is greater than next segment
						if(phantom == false && autoPlay == false) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).tailUp(g); //set tail segment to up
							}
						}
						else if(autoPlay) {
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).botTailUp(g); 
							}
						}
						
						else if (phantom){
							for(int i = 0 ; i < 1 ; i++) {
								snake.get(i).phanTailUp(g); 
							}
						}
					}
					
					for(int i = 1 ; i < (snake.size() - 1); i++) {
						if(snake.get(i - 1).getxCoor() > snake.get(i).getxCoor() || snake.get(i - 1).getxCoor() < snake.get(i).getxCoor()) {
							if(phantom == false && autoPlay == false) {
								snake.get(i).bodyHori(g);
							}
							else if(autoPlay){
								snake.get(i).botBodyHori(g);
							}
							
							else if(phantom) {
								snake.get(i).phanBodyHori(g);
							}
						}
						if(snake.get(i - 1).getyCoor() > snake.get(i).getyCoor() || snake.get(i - 1).getyCoor() < snake.get(i).getyCoor()) {
							if(phantom == false && autoPlay == false) {
								snake.get(i).bodyVert(g);
							}
							else if(autoPlay) {
								snake.get(i).botBodyVert(g);
							}
							
							else if(phantom) {
								snake.get(i).phanBodyVert(g);
							}
						}
						if((snake.get(i).getxCoor() > snake.get(i+1).getxCoor() && snake.get(i).getyCoor() < snake.get(i-1).getyCoor()) || (snake.get(i).getxCoor() > snake.get(i-1).getxCoor() && snake.get(i).getyCoor() < snake.get(i+1).getyCoor())) {
							if(phantom == false && autoPlay == false) {
								snake.get(i).DfL(g);
							}
							else if(autoPlay) {
								snake.get(i).botDfL(g);
							}
							
							else if(phantom) {
								snake.get(i).phanDfL(g);
							}
						}
						if((snake.get(i).getxCoor() < snake.get(i+1).getxCoor() && snake.get(i).getyCoor() < snake.get(i-1).getyCoor()) || (snake.get(i).getxCoor() < snake.get(i-1).getxCoor() && snake.get(i).getyCoor() < snake.get(i+1).getyCoor())) {
							if(phantom == false && autoPlay == false) {
								snake.get(i).DfR(g);
							}
							else if(autoPlay) {
								snake.get(i).botDfR(g);
							}
							
							else if(phantom) {
								snake.get(i).phanDfR(g);
							}
						}
						if((snake.get(i).getxCoor() > snake.get(i-1).getxCoor() && snake.get(i).getyCoor() > snake.get(i+1).getyCoor()) || (snake.get(i).getxCoor() > snake.get(i+1).getxCoor() && snake.get(i).getyCoor() > snake.get(i-1).getyCoor())) {
							if(phantom == false && autoPlay == false) {
								snake.get(i).UfL(g);
							}
							else if(autoPlay) {
								snake.get(i).botUfL(g);
							}
							
							else if(phantom) {
								snake.get(i).phanUfL(g);
							}
						}
						if((snake.get(i).getxCoor() < snake.get(i-1).getxCoor() && snake.get(i).getyCoor() > snake.get(i+1).getyCoor()) || (snake.get(i).getxCoor() < snake.get(i+1).getxCoor() && snake.get(i).getyCoor() > snake.get(i-1).getyCoor())) {
							if(phantom == false && autoPlay == false) {
								snake.get(i).UfR(g);
							}
							else if(autoPlay) {
								snake.get(i).botUfR(g);
							}
							
							else if(phantom) {
								snake.get(i).phanUfR(g);
							}
						}
					}
				}
				
				if(left) {
					
					if(phantom == false && autoPlay == false) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).headLeft(g); //head turns left while movement is left
						}
					}
					else if(autoPlay) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).botHeadLeft(g); 
						}
					}
					
					else if(phantom) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).phanHeadLeft(g); 
						}
					}
				}
				if(right) {
					if(phantom == false && autoPlay == false) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).headRight(g); //head turns right while movement is right
						}
					}
					else if(autoPlay) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).botHeadRight(g);
						}
					}
					
					else if(phantom) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).phanHeadRight(g);
						}
					}
				}
				if(up) {
					if(phantom == false && autoPlay == false) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).headUp(g); //head turns up while movement is up
						}
					}
					else if(autoPlay) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).botHeadUp(g); 
						}
					}
					
					else if(phantom) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).phanHeadUp(g); 
						}
					}
				}
				if(down) {
					if(phantom == false && autoPlay == false) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).headDown(g); //head turns down while movement is down
						}					
					}
					else if(autoPlay) {
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).botHeadDown(g);
						}
					}
					
					else if(phantom){
						for(int i = snake.size() - 1 ; i < snake.size() ; i++) {
							snake.get(i).phanHeadDown(g); //head turns down while movement is down
						}
					}
				}
			}
			
			
			for(int i = 0 ; i < foods.size(); i++) { //food gfx
				foods.get(i).draw(g);
			}
			
			if(alchem == false) {
				for(int i = 0 ; i < bombs.size(); i++) { //bomb gfx
					if(shattered != bombs.get(i)) {
						bombs.get(i).reg(g);
					}
					else if(shattered == bombs.get(i)) {
						bombs.get(i).shatter(g);
					}
					else if(shattered2 == bombs.get(i)) {
						bombs.get(i).shatter2(g);
					}
				}
			}
			
			if(alchem) {
				for(int i = 0 ; i < bombs.size(); i++) {
					if(alchDur < 50 || alchDur % 3 != 0) {
						bombs.get(i).alch(g);
					}
					else if(alchDur > 50 || alchDur % 3 == 0) {
						bombs.get(i).reg(g);
					}
				}
				
			}
			
			if(power1 == 1||power2 == 1||power3 == 1) {
				for(int i = 0; i < pwrUp1.size(); i++) { //gfx for x2
					if(x2Time < 135 || x2Time % 5 != 0) { //blinks near expiration
						pwrUp1.get(i).draw(g);
					}
				}
			}
			
			for(int i = 0; i < pwrUp2.size(); i++) { //gfx for phantom
				if(phanTime < 135 || phanTime % 5 != 0) {
					pwrUp2.get(i).draw(g);
				}
			}
			
			for(int i = 0; i < pwrUp3.size(); i++) { //gfx for alchemy
				if(alchTime < 135 || alchTime % 5 != 0) {
					pwrUp3.get(i).draw(g);
				}
			}
			
			for(int i = 0; i < pwrUp4.size(); i++) {//gfx for scissors
				if(scisTime < 135 || scisTime % 5 != 0) {
					pwrUp4.get(i).draw(g);
					}
				}
			
			for(int i = 0; i < pwrUp5.size(); i++) {//gfx for feast
				if(feastTime < 135 || feastTime % 5 != 0) {
					pwrUp5.get(i).draw(g);
					}
				}
			
			for(int i = 0; i < pwrUp6.size(); i++) {//gfx for phoenix
				if(phoeTime < 135 || phoeTime % 5 != 0) {
					pwrUp6.get(i).draw(g);
				}
			}
			
			for(int i = 0; i < pwrUp7.size(); i++) {//gfx for bot
				if(botTime < 135 || botTime % 5 != 0) {
					pwrUp7.get(i).draw(g);
				}
			}

			for(int i = 0; i < pwrUp8.size(); i++) {//gfx for mystery
				if(mystTime < 135 || mystTime % 5 != 0) {
					pwrUp8.get(i).draw(g);
				}
			}
			
			Image cactus = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\cactusShadow.png").getImage();
			g.drawImage(cactus, 0, 30, null);
			
			if(autoPlay) {
				Image terminal = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\snakeBotTerminal.png").getImage();
				g.drawImage(terminal, 63, 200, null);
			}
		}
		if(gameRunning == false && mainScreen && powerScreen == false && perkScreen == false) { //title that displays initially
			Image superTitle = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\super.png").getImage();
			g.drawImage(superTitle, 70, 140, null);
			Image snakeTitle = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\snake.png").getImage();
			g.drawImage(snakeTitle, 250, 140, null);
		}
		Font font = new Font("Verdana", Font.PLAIN, 19); //sets size and font of other text
		g.setFont(font);
		g.setColor(Color.WHITE);
		
		if(points >= 0 && mainScreen == false) { //displays during the game
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 500, 40);
			g.setColor(Color.WHITE);
			g.drawString("Points: " + (points), 10, 25);
			g.drawString("High Score: " + highScore(), 350, 25);
			g.drawLine(0, 40, 500, 40);
			if(x2points == true && (x2Dur < 200 || x2Dur % 5 != 0)) {
				if(power1 == 1 || power1 == 8) {
					g.drawImage(doublePointsSprite, 160, 5, null);
				}
				else if(power2 == 1 || power2 == 8) {
					g.drawImage(doublePointsSprite, 205, 5, null);
				}
				else if(power3 == 1 || power3 == 8) {
					g.drawImage(doublePointsSprite, 250, 5, null);
				}
			}
			if(phantom == true && (phanDur < 200 || phanDur % 5 != 0)) {
				if(power1 == 2 || power1 == 8) {
					g.drawImage(phantomSprite, 160, 5, null);
				}
				else if(power2 == 2 || power2 == 8) {
					g.drawImage(phantomSprite, 205, 5, null);
				}
				else if(power3 == 2 || power3 == 8) {
					g.drawImage(phantomSprite, 250, 5, null);
				}
			}
			if(alchem == true && (alchDur < 60 || alchDur % 5 != 0)) {
				if(power1 == 3 || power1 == 8) {
					g.drawImage(alchemySprite, 160, 5, null);
				}
				else if(power2 == 3 || power2 == 8) {
					g.drawImage(alchemySprite, 205, 5, null);
				}
				else if(power3 == 3 || power3 == 8) {
					g.drawImage(alchemySprite, 250, 5, null);
				}
			}
			if(scisDisp == true) {
				if(power1 == 4 || power1 == 8) {
					g.drawImage(scissorsSprite, 160, 5, null);
				}
				else if(power2 == 4 || power2 == 8) {
					g.drawImage(scissorsSprite, 205, 5, null);
				}
				else if(power3 == 4 || power3 == 8) {
					g.drawImage(scissorsSprite, 250, 5, null);
				}
			}
			if(feasts == true && (feastDur < 45 || feastDur % 5 != 0)) {
				if(power1 == 5 || power1 == 8) {
					g.drawImage(feastSprite, 160, 5, null);
				}
				else if(power2 == 5 || power2 == 8) {
					g.drawImage(feastSprite, 205, 5, null);
				}
				else if(power3 == 5 || power3 == 8) {
					g.drawImage(feastSprite, 250, 5, null);
				}
			}
			if(phoenix == true && (phoeDur < 950 || phoeDur % 5 != 0)) {
				if(power1 == 6 || power1 == 8) {
					g.drawImage(phoeSprite, 160, 5, null);
				}
				else if(power2 == 6 || power2 == 8) {
					g.drawImage(phoeSprite, 205, 5, null);
				}
				else if(power3 == 6 || power3 == 8) {
					g.drawImage(phoeSprite, 250, 5, null);
				}
			}
			if(autoPlay == true && (botDur < 300 || botDur % 5 != 0)) {
				if(power1 == 7 || power1 == 8) {
					g.drawImage(snakeBotSprite, 160, 5, null);
				}
				else if(power2 == 7 || power2 == 8) {
					g.drawImage(snakeBotSprite, 205, 5, null);
				}
				else if(power3 == 7 || power3 == 8) {
					g.drawImage(snakeBotSprite, 250, 5, null);
				}
			}
			if(autoPlay) {
				Font code = new Font("Courier", Font.PLAIN, 14);
				g.setFont(code);
				g.setColor(Color.GREEN);
				
				g.drawString("> SNAKEBOT.exe v1.0.1.1", 95, 235);
				g.drawString("> User Status: Guest", 95, 260);
				g.drawString("> Success Rate: 96."+Math.abs((xCoor + yCoor)-10) +"%", 95, 285);
				g.drawString("> Prime Directive: Consume", 95, 310);
				g.drawString("> Press [BACKSPACE] to resume control", 95, 335);	
			} 
		}
		if(gameRunning == false && mainScreen == false) { //only displays if the player has lost at least once
			g.setFont(font);
			g.setColor(Color.WHITE);
			if(selector < 0) {
				selector = 1;
			}
			if(selector > 1) {
				selector = 0;
			}
			g.drawString("You scored " + points + " points!", 140, 230);
			g.drawString("Replay", 210, 260);
			g.drawString("Home Screen", 185, 290);
			if(selector == 0) {
				g.drawLine(205, 268, 279, 268);
				g.drawLine(205, 268, 205, 240);
				g.drawLine(205, 240, 279, 240);
				g.drawLine(279, 268, 279, 240);
			}
			if(selector == 1) {
				g.drawLine(180, 298, 315, 298);
				g.drawLine(180, 298, 180, 270);
				g.drawLine(180, 270, 315, 270);
				g.drawLine(315, 270, 315, 298);
			}
			if(points < highScore()) {
				if(saveVal == false) {
					saveGame();
					saveVal = true;
					}
				}
			else if(points >= highScore()) {
				g.drawString("New High Score: " + points, 155, 320);
				if(saveVal == false) {
					saveGame();
					saveVal = true;
					}
			}
		}
		else if(gameRunning == false && powerScreen == false && perkScreen == false) { //start screen
			if(selector > 2) {
				selector = 0;
			}
			if(selector < 0) {
				selector = 2;
			}
			Image play = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\play.png").getImage();
			g.drawImage(play, 207, 230, null);
			g.drawString("Power Ups", 193, 280);
			g.drawString("Perks", 215, 310);
			Font small = new Font("Verdana", Font.PLAIN, 15);
			g.setFont(small);
			if(selector == 0) {
				g.drawRect(205, 230, 70, 30);
			}
			if(selector == 1) {
				g.drawLine(188, 286, 300, 286);
				g.drawLine(188, 286, 188, 261);
				g.drawLine(188, 261, 300, 261);
				g.drawLine(300, 261, 300, 286);
			}
			if(selector == 2) {
				g.drawRect(210, 291, 63, 22);
			}
			Image xpBackdrop = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\woodXpBack.png").getImage();
			g.drawImage(xpBackdrop, 38, 420, null);
			g.drawString("Level " + playerLvl, 220, 445);
			//exp bar/amount display
			g.setColor(Color.GRAY);
			g.fillRect(50, 450, 400, 10);
			g.setColor(Color.ORANGE);
			if(xpFetch() < lvl2Req) {
				g.fillRect(51, 451, Math.round((xpFetch()/lvl2Req) * 399.0f), 8);
				playerLvl = 1;
				g.setFont(small);
				g.setColor(Color.WHITE);
				g.drawString(xpFetch() + " / 30000", 200, 475);
			}
			else if(xpFetch() < lvl3Req){
				g.fillRect(51, 451, Math.round(((xpFetch() - lvl2Req)/(lvl3Req - lvl2Req)) * 399.0f), 8);
				playerLvl = 2;
				g.setColor(Color.WHITE);
				g.drawString((xpFetch() - 30000) + " / 45000", 200, 475);
			}
			
			else if(xpFetch() < lvl4Req){
				g.fillRect(51, 451, Math.round(((xpFetch() - lvl3Req)/(lvl4Req - lvl3Req)) * 399.0f), 8);
				playerLvl = 3;
				g.setColor(Color.WHITE);
				g.drawString((xpFetch() - 75000) + " / 55000", 200, 475);
			}
			
			else if(xpFetch() < lvl5Req){
				g.fillRect(51, 451, Math.round(((xpFetch() - lvl4Req)/(lvl5Req - lvl4Req)) * 399.0f), 8);
				playerLvl = 4;
				g.setColor(Color.WHITE);
				g.drawString((xpFetch() - 130000) + " / 70000", 200, 475);
			}
			else if(xpFetch() > lvl5Req) {
				g.fillRect(51, 451, 399, 8);
				playerLvl = 5;
				g.drawString("70000 / 70000", 195, 475);
			}

		}
		
		else if(powerScreen && gameRunning == false && perkScreen == false) {
			g.drawImage(dimmer, 0, 0, null);
			Font small = new Font("Verdana", Font.PLAIN, 14);
			g.setFont(small);
			g.setColor(Color.WHITE);
			g.drawString("Home [esc]", 30, 30);
			g.drawString("Clear powers [backspace]", 300, 30);
			g.drawString("Double Points", 40, 120);
			g.drawString("Phantom", 165, 120);
			g.drawString("Alchemy", 278, 120);
			g.drawString("Scissors", 385, 120);
			g.drawString("Feast", 65, 220);
			g.drawString("Phoenix", 170, 220);
			g.drawString("SnakeBot", 276, 220);
			g.drawString("Mystery", 390, 220);
			g.drawImage(descripBackdrop, 40, 390, null);
			//double points menu display
			g.drawImage(doublePointsSprite, 70, 70, null);
			//phantom menu display
			g.drawImage(phantomSprite, 180, 70, null);
			//alchemy menu display
			g.drawImage(alchemySprite, 290, 70, null);
			//scissors menu display
			g.drawImage(scissorsSprite, 400, 70, null);
			//feast menu display
			if(playerLvl > 1) {
				g.drawImage(feastSprite, 70, 170, null);
			}
			else {
				Image feastLocked = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\feastLocked.png").getImage();
				g.drawImage(feastLocked, 70, 170, null);
			}
			//phoenix menu display
			if(playerLvl > 2) {
				g.drawImage(phoeSprite, 180, 170, null);
			}
			else {
				Image phoeLocked = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\phoenixLocked.png").getImage();
				g.drawImage(phoeLocked, 180, 170, null);
			}
			//snakebot menu display
			if(playerLvl > 3) {
				g.drawImage(snakeBotSprite, 290, 170, null);
			}
			else {
				Image snakeBotLocked = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\snakeBotLocked.png").getImage();
				g.drawImage(snakeBotLocked, 290, 170, null);
			}
			if(playerLvl > 4) {
				g.drawImage(mysterySprite, 400, 170, null);
			}
			else {
				Image mysteryLocked = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\mysteryLocked.png").getImage();
				g.drawImage(mysteryLocked, 400, 170, null);
			}
			
			g.setColor(Color.WHITE);
			g.setFont(font);
			if(horiSelector == 0 && selector == 0) {
				g.drawLine(35, 125, 135, 125);
				g.drawString("Doubles all points earned while active", 73, 430);
				g.drawString("Lasts 15 seconds", 170, 460);
			}
			if(horiSelector == 1 && selector == 0) {
				g.drawLine(160, 125, 227, 125);
				g.drawString("Makes snake invincible and able to warp", 60, 430);
				g.drawString("Lasts 15 seconds", 170, 460);
			}
			if(horiSelector == 2 && selector == 0) {
				g.drawLine(273, 125, 338, 125);
				g.drawString("Turns all rocks into food", 130, 430);
				g.drawString("Lasts 5 seconds", 170, 460);
			}
			if(horiSelector == 3 && selector == 0) {
				g.drawLine(382, 125, 448, 125);
				g.drawString("Removes 7 segments", 145, 430);
				g.drawString("Happens instantly", 165, 460);
			}
			if(horiSelector == 0 && selector == 1) {
				g.drawLine(63, 225, 105, 225);
				if(playerLvl > 1) {
					g.drawString("Spawns food everywhere", 130, 430);
				}
				else {
					g.drawString("Spawns food everywhere (Lvl 2)", 90, 430);
				}
				g.drawString("Happens instantly", 165, 460);
			}
			if(horiSelector == 1 && selector == 1) {
				g.drawLine(169, 225, 225, 225);
				if(playerLvl > 2) {
					g.drawString("Revives snake if it dies", 125, 430);
				}
				else {
					g.drawString("Revives snake if it dies (Lvl 3)", 90, 430);
				}
				g.drawString("Lasts 1 minute", 165, 460);
			}
			if(horiSelector == 2 && selector == 1) {
				g.drawLine(275, 225, 340, 225);
				if(playerLvl > 3) {
					g.drawString("SnakeBot plays the game", 125, 430);
				}
				else {
					g.drawString("SnakeBot plays the game (Lvl 4)", 90, 430);
				}
				g.drawString("Lasts 20 seconds", 165, 460);
			}
			if(horiSelector == 3 && selector == 1) {
				g.drawLine(389, 225, 440, 225);
				if(playerLvl > 4) {
					g.drawString("Grants a mystery power", 125, 430);
				}
				else {
					g.drawString("Grants a mystery power (Lvl 5)", 90, 430);
				}
				g.drawString("Happens instantly", 165, 460);
			}
			
			g.drawString("Power Up 1: " + powerString.get(power1), 50, 300);
			g.drawString("Power Up 2: " + powerString.get(power2), 50, 330);
			g.drawString("Power Up 3: " + powerString.get(power3), 50, 360);
			
			if(horiSelector > 3) {
				horiSelector = 0;
			}
			if(horiSelector < 0) {
				horiSelector = 3;
			}
			if(selector > 1) {
				selector = 0;
			}
			if(selector < 0) {
				selector = 1;
			}
		}
		
		else if(perkScreen && gameRunning == false && powerScreen == false) { //perk display
			g.drawImage(dimmer, 0, 0, null);
			Font small = new Font("Verdana", Font.PLAIN, 14);
			g.setFont(small);
			g.setColor(Color.WHITE);
			g.drawString("Home [esc]", 30, 30);
			g.drawString("Clear Perks [backspace]", 300, 30);
			g.drawString("Tortoise", 65, 135);
			g.drawString("High Voltage", 198, 135);
			g.drawString("Level Ground", 350, 135);
			g.drawString("Safe Zone", 55, 270);
			g.drawString("Low Calorie", 200, 270);
			g.drawString("Fortitude", 362, 270);
			g.drawImage(descripBackdrop, 40, 390, null);
			slowMo = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\SlowMo.png").getImage();
			g.drawImage(slowMo, 40, 40, null);
			highVoltage = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\highVoltage.png").getImage();
			g.drawImage(highVoltage, 189, 37, null);
			levelGround = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\levelGround.png").getImage();
			g.drawImage(levelGround, 340, 39, null);
			Image safeZone = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\safeZone.png").getImage();
			Image safeLocked = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\safeZone.png").getImage();
			if(playerLvl > 2) {
				g.drawImage(safeZone, 40, 170, null);
			}
			else {
				g.drawImage(safeLocked, 40, 170, null);
			}
			Image diet = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\diet.png").getImage();
			Image dietLocked = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\dietLocked.png").getImage();
			if(playerLvl > 3) {
				g.drawImage(diet, 189, 170, null);
			}
			else {
				g.drawImage(dietLocked, 189, 170, null);
			}
			Image fort = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\fortitude.png").getImage();
			Image fortLocked = new ImageIcon("C:\\Program Files (x86)\\SuperSnake\\GFX\\fortitudeLocked.png").getImage();
			
			if(playerLvl > 4) {
				g.drawImage(fort, 340, 170, null);
			}
			else {
				g.drawImage(fortLocked, 340, 170, null);
			}
			
			g.setColor(Color.WHITE);
			g.setFont(font);
			if(horiSelector == 0 && selector == 0) {
				g.drawLine(64, 140, 120, 140);
				g.drawString("Slows movement of the Snake", 100, 430);
			}
			if(horiSelector == 1 && selector == 0) {
				g.drawLine(197, 140, 280, 140);
				g.drawString("Increased power up spawns", 110, 430);
			}
			if(horiSelector == 2 && selector == 0) {
				g.drawLine(349, 140, 437, 140);
				g.drawString("Lower rock spawn rate", 130, 430);
			}
			if(horiSelector == 0 && selector == 1) {
				g.drawLine(55, 275, 120, 275);
				g.drawString("Food will not spawn on the edges", 90, 430);
				if(playerLvl < 2) {
					g.drawString("Unlocked at Level 2", 150, 460);
				}
			}
			if(horiSelector == 1 && selector == 1) {
				g.drawLine(197, 275, 280, 275);
				g.drawString("Chance to not gain size after eating", 80, 430);
				if(playerLvl < 3) {
					g.drawString("Unlocked at Level 3", 150, 460);
				}
			}
			if(horiSelector == 2 && selector == 1) {
				g.drawLine(359, 275, 422, 275);
				g.drawString("Grants short invincibility after eating", 70, 430);
				if(playerLvl < 4) {
					g.drawString("Unlocked at Level 4", 150, 460);
				}
			}
			
			g.drawString("Primary: " + perkString.get(perk1), 50, 330);
			g.drawString("Secondary: " + perkString.get(perk2), 50, 360);
			
			if(horiSelector > 2) {
				horiSelector = 0;
			}
			if(horiSelector < 0) {
				horiSelector = 2;
			}
			if(selector > 1) {
				selector = 0;
			}
			if(selector < 0) {
				selector = 1;
			}
		}
		
		if(pause) {
			g.setColor(Color.WHITE);
			g.drawString("Game paused", 180, 230);
			g.drawString("Resume", 210, 260);
			g.drawString("Home Screen", 190, 290);
			if(selector == 0) {
				g.drawLine(205, 267, 290, 267);
				g.drawLine(205, 267, 205, 240);
				g.drawLine(205, 240, 290, 240);
				g.drawLine(290, 240, 290, 267);
			}
			if(selector == 1) {
				g.drawRect(185, 270, 135, 25);
			}
			if(perk1 > 0 || perk2 > 0) {
				g.drawImage(perkIMG.get(perk1), 40, 40, null);
				g.drawImage(perkIMG.get(perk2), 40, 80, null);
			}
			
			if(selector > 1) {
				selector = 0;
			}
			if(selector < 0) {
				selector = 1;
			}
		}
	}

	@Override
	public void run() {
		while(running) {
			try {
				tick();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //runs ticks while gameRunning is true
			repaint(); 
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(gameRunning) { //what keys do while game is running
			if(key == KeyEvent.VK_RIGHT && !left && snake.get(snake.size() - 1).getyCoor() != snake.get((snake.size() - 2)).getyCoor() && autoPlay == false) { //right key = right movement
				right = true;
				up = false;
				down = false;
			}
			
			if(key == KeyEvent.VK_LEFT && !right && snake.get(snake.size() - 1).getyCoor() != snake.get((snake.size() - 2)).getyCoor() && autoPlay == false) { //left key = left movement
				left = true;
				up = false;
				down = false;
			}
			
			if(key == KeyEvent.VK_UP && !down && snake.get(snake.size() - 1).getxCoor() != snake.get((snake.size() - 2)).getxCoor() && autoPlay == false) { //up key = up movement 
				up = true;
				right = false;
				left = false;
			}
			
			if(key == KeyEvent.VK_DOWN && !up && snake.get(snake.size() - 1).getxCoor() != snake.get((snake.size() - 2)).getxCoor() && autoPlay == false) { //down key = down movement
				down = true;
				right = false;
				left = false;
			}
			
			if(key == KeyEvent.VK_ESCAPE) {
				pause = true;
			}
			if(key == KeyEvent.VK_BACK_SPACE && autoPlay) {
				autoPlay = false;
				botDur = 0;
			}
		}
		
		if(gameRunning == false || pause) { //what keys do while game is not running
			if(key == KeyEvent.VK_UP) {
				selector--;
			}
			if(key == KeyEvent.VK_DOWN) {
				selector++;
			}
		}
		
		if(running == false) { //emergency start
			if(key == KeyEvent.VK_SPACE) {
				start();
			}
		}
		
		if(pause) { //pause menu selection
			if(selector == 0) {
				if(key == KeyEvent.VK_ENTER) {
					pause = false;
				}
			}
			if(selector == 1) {
				if(key == KeyEvent.VK_ENTER) {
					gameRunning = false;
					pause = false;
				}
			}
		}
		//game over screen menu selection
		if(gameRunning == false && mainScreen == false) {
			if(selector == 0) {
				if(key == KeyEvent.VK_ENTER) {
					reset();
				}
			}
			if(selector == 1) { //main menu
				if(key == KeyEvent.VK_ENTER) {
					selector = -4;
					mainScreen = true;
					powerScreen = false;
					perkScreen = false;
				}
			}
		}
		//keys in main menu
		if(gameRunning == false && mainScreen && powerScreen == false && perkScreen == false) {
			if(selector == 0) {
				if(key == KeyEvent.VK_ENTER) {
					reset();
				}
			}
			if(selector == 1) {
				if(key == KeyEvent.VK_ENTER) {
					horiSelector = 4;
					selector = 4;
					powerScreen = true;
				}
			}
			if(selector == 2) {
				if(key == KeyEvent.VK_ENTER) {
					horiSelector = 4;
					selector = 4;
					perkScreen = true;
				}
			}
			if(powerScreen == true || leaderboard == true || perkScreen == true) {
				if(key == KeyEvent.VK_ESCAPE) {
					powerScreen = false;
					leaderboard = false;
					perkScreen = false;
				}
			}
		}
		
		if(powerScreen || perkScreen) {
			if(key == KeyEvent.VK_LEFT) {
				horiSelector--;
			}
			if(key == KeyEvent.VK_RIGHT) {
				horiSelector++;
			}
		}
		
		//keys in powerup menu
		if(powerScreen && gameRunning == false) {
			if(power1 == 0) {
				if(horiSelector == 0 && selector == 0) {
					if(key == KeyEvent.VK_ENTER) {
						power1 = 1;
					}
				}
				if(horiSelector == 1 && selector == 0) {
					if(key == KeyEvent.VK_ENTER) {
						power1 = 2;
					}
				}
				if(horiSelector == 2 && selector == 0) {
					if(key == KeyEvent.VK_ENTER) {
						power1 = 3;
					}
				}
				if(horiSelector == 3 && selector == 0) {
					if(key == KeyEvent.VK_ENTER) {
						power1 = 4;
					}
				}
				if(horiSelector == 0 && selector == 1) {
					if(playerLvl > 1) {
						if(key == KeyEvent.VK_ENTER) {
							power1 = 5;
						}
					}
				}
				if(horiSelector == 1 && selector == 1) {
					if(playerLvl > 2) {
						if(key == KeyEvent.VK_ENTER) {
							power1 = 6;
						}
					}
				}
				if(horiSelector == 2 && selector == 1) {
					if(playerLvl > 3) {
						if(key == KeyEvent.VK_ENTER) {
							power1 = 7;
						}
					}
				}
				if(horiSelector == 3 && selector == 1) {
					if(playerLvl > 4) {
						if(key == KeyEvent.VK_ENTER) {
							power1 = 8;
						}
					}
				}
			}
			
			else if(power2 == 0) {
				if(horiSelector == 0 && selector == 0 && power1 != 1) {
					if(key == KeyEvent.VK_ENTER) {
						power2 = 1;
					}
				}
				if(horiSelector == 1 && selector == 0 && power1 != 2) {
					if(key == KeyEvent.VK_ENTER) {
						power2 = 2;
					}
				}
				if(horiSelector == 2 && selector == 0 && power1 != 3) {
					if(key == KeyEvent.VK_ENTER) {
						power2 = 3;
					}
				}
				if(horiSelector == 3 && selector == 0 && power1 != 4) {
					if(key == KeyEvent.VK_ENTER) {
						power2 = 4;
					}
				}
				if(horiSelector == 0 && selector == 1 && power1 != 5) {
					if(playerLvl > 1) {
						if(key == KeyEvent.VK_ENTER) {
							power2 = 5;
						}
					}
				}
				if(horiSelector == 1 && selector == 1 && power1 != 6) {
					if(playerLvl > 2) {
						if(key == KeyEvent.VK_ENTER) {
							power2 = 6;
						}
					}
				}
				if(horiSelector == 2 && selector == 1 && power1 != 7) {
					if(playerLvl > 3) {
						if(key == KeyEvent.VK_ENTER) {
							power2 = 7;
						}
					}
				}
				if(horiSelector == 3 && selector == 1 && power1 != 8) {
					if(playerLvl > 4) {
						if(key == KeyEvent.VK_ENTER) {
							power2 = 8;
						}
					}
				}
			}
			
			else if(power3 == 0) {
				if(horiSelector == 0 && selector == 0 && power1 != 1 && power2 != 1) {
					if(key == KeyEvent.VK_ENTER) {
						power3 = 1;
					}
				}
				if(horiSelector == 1 && selector == 0 && power1 != 2 && power2 != 2) {
					if(key == KeyEvent.VK_ENTER) {
						power3 = 2;
					}
				}
				if(horiSelector == 2 && selector == 0 && power1 != 3 && power2 != 3) {
					if(key == KeyEvent.VK_ENTER) {
						power3 = 3;
					}
				}
				if(horiSelector == 3 && selector == 0 && power1 != 4 && power2 != 4) {
					if(key == KeyEvent.VK_ENTER) {
						power3 = 4;
					}
				}
				if(horiSelector == 0 && selector == 1 && power1 != 5 && power2 != 5) {
					if(playerLvl > 1) {
						if(key == KeyEvent.VK_ENTER) {
							power3 = 5;
						}
					}
				}
				if(horiSelector == 1 && selector == 1 && power1 != 6 && power2 != 6) {
					if(playerLvl > 2) {
						if(key == KeyEvent.VK_ENTER) {
							power3 = 6;
						}
					}
				}
				if(horiSelector == 2 && selector == 1 && power1 != 7 && power2 != 7) {
					if(playerLvl > 3) {
						if(key == KeyEvent.VK_ENTER) {
							power3 = 7;
						}
					}
				}
				if(horiSelector == 3 && selector == 1 && power1 != 8 && power2 != 8) {
					if(playerLvl > 4) {
						if(key == KeyEvent.VK_ENTER) {
							power3 = 8;
						}
					}
				}
			}
			//clears powers
			if(power1 != 0) {
				if(key == KeyEvent.VK_BACK_SPACE) {
					power1 = 0;
				}
			}
			if(power2 != 0) {
				if(key == KeyEvent.VK_BACK_SPACE) {
					power2 = 0;
				}
			}
			if(power3 != 0) {
				if(key == KeyEvent.VK_BACK_SPACE) {
					power3 = 0;
				}
			}
			if(key == KeyEvent.VK_ESCAPE) { //saves powers when exiting power menu
				try {
					powerSave();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				powerScreen = false;
			}
		}
		//perk screen controls
		if(perkScreen && gameRunning == false) {
			
			if(perk1 == 0) {
				if(horiSelector == 0 && selector == 0) {
					if(key == KeyEvent.VK_ENTER) {
						perk1 = 1;
					}
				}
				if(horiSelector == 1 && selector == 0) {
					if(key == KeyEvent.VK_ENTER) {
						perk1 = 2;
					}
				}
				if(horiSelector == 2 && selector == 0) {
					if(key == KeyEvent.VK_ENTER) {
						perk1 = 3;
					}
				}
				if(horiSelector == 0 && selector == 1 && playerLvl > 1) {
					if(key == KeyEvent.VK_ENTER) {
						perk1 = 4;
					}
				}
				if(horiSelector == 1 && selector == 1 && playerLvl > 2) {
					if(key == KeyEvent.VK_ENTER) {
						perk1 = 5;
					}
				}
				if(horiSelector == 2 && selector == 1 && playerLvl > 3) {
					if(key == KeyEvent.VK_ENTER) {
						perk1 = 6;
					}
				}
			}
			
			else if(perk2 == 0) {
				if(horiSelector == 0 && selector == 0 && perk1 != 1) {
					if(key == KeyEvent.VK_ENTER) {
						perk2 = 1;
					}
				}
				if(horiSelector == 1 && selector == 0 && perk1 != 2) {
					if(key == KeyEvent.VK_ENTER) {
						perk2 = 2;
					}
				}
				if(horiSelector == 2 && selector == 0 && perk1 != 3) {
					if(key == KeyEvent.VK_ENTER) {
						perk2 = 3;
					}
				}
				if(horiSelector == 0 && selector == 1 && playerLvl > 1 && perk1 != 4) {
					if(key == KeyEvent.VK_ENTER) {
						perk2 = 4;
					}
				}
				if(horiSelector == 1 && selector == 1 && playerLvl > 2 && perk1 != 5) {
					if(key == KeyEvent.VK_ENTER) {
						perk2 = 5;
					}
				}
				if(horiSelector == 2 && selector == 1 && playerLvl > 3 && perk1 != 6) {
					if(key == KeyEvent.VK_ENTER) {
						perk2 = 6;
					}
				}
			}
			
			if(perk1 != 0) {
				if(key == KeyEvent.VK_BACK_SPACE) {
					perk1 = 0;
				}
			}
			if(perk2 != 0) {
				if(key == KeyEvent.VK_BACK_SPACE) {
					perk2 = 0;
				}
			}
			
			if(key == KeyEvent.VK_ESCAPE) {
				try {
					perkSave();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				perkScreen = false;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {	
	}
	
	public void reset() {
		snake.clear(); //resets snake size
		gameStart(); //begins the game
		foods.clear();
		foods.add(food);
		size = 5; //sets size to default 
		points = 0; //resets points
		xCoor = 10; 
		yCoor = 10; //sets coordinates for initialization
		bombs.clear();
		right = true;
		down = false;
		left = false;
		up = false;
		saveVal = false;
		xpVal = false;
		x2points = false;
		scisDisp = false;
		feasts = false;
		pwrUp1.clear();
		pwrUp2.clear();
		pwrUp3.clear();
		pwrUp4.clear();
		pwrUp5.clear();
		pwrUp6.clear();
		pwrUp7.clear();
		pwrUp8.clear();
		alchem = false;
		autoPlay = false;
		phoenix = false;
		iFrames = false;
		phantom = false; //clears all values from previous game
		pause = false;
		hiddenFood = 0;
		x2Time = 0; x2Dur = 0;
		phanTime = 0; phanDur = 0;
		alchTime = 0; alchDur = 0;
		scisDur = 0; scisTime = 0;
		feastTime = 0; feastDur = 0;
		phoeTime = 0; phoeDur= 0;
		botTime = 0; botDur = 0;
		mystTime = 0;
		
	}
	public void respawn() {
		xCoor = 10;
		yCoor = 10;
		iFrameDurMult = 4;
		left = false;
		up = false;
		down = false;
		right = true;
		phoenix = false;
		iFrames = true;
		iFrameDur = 0;
		phoeBool = true;
	}
	
	public static void saveGame() { //saves the current score
		
		String pointString = new Integer(points).toString(); //converts points to string
		
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter("C:\\Program Files (x86)\\SuperSnake\\SaveData\\scores.txt", true)); //writes the players score
			bw.write(pointString);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			System.out.println("Error");
		}
	}
	public static int highScore() { //finds the high score
		
		ArrayList<Integer>scores = new ArrayList<Integer>(); //creates a list the computer can read	
		
		try {
			File x = new File("C:\\Program Files (x86)\\SuperSnake\\SaveData\\scores.txt"); //path to file
			Scanner sc = new Scanner(x);
			while(sc.hasNext()) {
				scores.add(sc.nextInt()); //adds numbers to the list
			}
			sc.close();		
		} catch(FileNotFoundException e) {
			System.out.println("Error");
		}
		int high_score = Collections.max(scores); //returns max value from high scores
		return high_score;
	}
	
	public static void powerSave() throws FileNotFoundException, UnsupportedEncodingException { //saves power preferences
		PrintWriter powerSave = new PrintWriter("C:\\Program Files (x86)\\SuperSnake\\SaveData\\powers.txt", "UTF-8");
		powerSave.println(power1);
		powerSave.println(power2);
		powerSave.println(power3);
		powerSave.close();
	}
	public static int[] powerCall() { //finds the players preferences for powers
		ArrayList<Integer>powerArr = new ArrayList<Integer>();
		
		try {
			File pw1 = new File("C:\\Program Files (x86)\\SuperSnake\\SaveData\\powers.txt");
			Scanner sc = new Scanner(pw1);
			while(sc.hasNext()) {
				powerArr.add(sc.nextInt());
			}
			sc.close();
		} catch(FileNotFoundException e) {
			System.out.println("Error");
		}
		
		if(powerArr.size() > 3 || powerArr.size() <= 0) {
			powerArr.clear();
			powerArr.add(0);
			powerArr.add(0);
			powerArr.add(0);
		}
		
		int[] powerVal = new int[3];
		
		powerVal [0] = powerArr.get(0);
		powerVal [1] = powerArr.get(1);
		powerVal [2] = powerArr.get(2);
		
		return powerVal;
	}
	
	public static void perkSave() throws FileNotFoundException, UnsupportedEncodingException { //saves perk preferences
		PrintWriter perkSave = new PrintWriter("C:\\Program Files (x86)\\SuperSnake\\SaveData\\perks.txt", "UTF-8");
		perkSave.println(perk1);
		perkSave.println(perk2);
		perkSave.close();
	}
	public static int[] perkCall() { //finds the players preferences for perks
		ArrayList<Integer>perkArr = new ArrayList<Integer>();
		
		try {
			File pw1 = new File("C:\\Program Files (x86)\\SuperSnake\\SaveData\\perks.txt");
			Scanner sc = new Scanner(pw1);
			while(sc.hasNext()) {
				perkArr.add(sc.nextInt());
			}
			sc.close();
		} catch(FileNotFoundException e) {
			System.out.println("Error");
		}
		
		if(perkArr.size() > 2 || perkArr.size() <= 0) {
			perkArr.clear();
			perkArr.add(0);
			perkArr.add(0);
		}
		
		int[] perkVal = new int[2];
		
		perkVal [0] = perkArr.get(0);
		perkVal [1] = perkArr.get(1);
		
		return perkVal;
	}
	
	public static void xpTrack() { //writes the score earned
		float tempExp = (points * (1 + (points/100.0f))) * 100;
		int intExp = (int) tempExp;
		String tempString = new Integer(intExp).toString();
		
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter("C:\\Program Files (x86)\\SuperSnake\\SaveData\\xp.txt", true)); //writes the players xp
			bw.write(tempString);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			System.out.println("Error");
		}
	}
	
	public static int xpFetch() { //totals all xp earned
		
		ArrayList<Integer>playerXp = new ArrayList<Integer>(); 	
		
		try {
			File x = new File("C:\\Program Files (x86)\\SuperSnake\\SaveData\\xp.txt"); //path to file
			Scanner sc = new Scanner(x);
			while(sc.hasNext()) {
				playerXp.add(sc.nextInt()); //adds numbers to the list
			}
			sc.close();		
		} catch(FileNotFoundException e) {
			System.out.println("Error");
		}
		int exp = 0;
		for(int i = 0; i < playerXp.size(); i++) {
			exp += playerXp.get(i);
		}
		return exp;
	}
	
	
	public static int randomRange(int min, int max) { //creates a random integer range
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}
	
	public void sleep() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	public void left() {
		right = false;
		up = false;
		down = false;
		left = true;
	}
	public void right() {
		left = false;
		up = false;
		down = false;
		right = true;
	}
	public void up() {
		down = false;
		right = false;
		left = false;
		up = true;
	}
	public void down() {
		up = false;
		right = false;
		left = false;
		down = true;
	}
}