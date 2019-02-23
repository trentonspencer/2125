//main program entry point
//loads all assets and begins the game
//also handles game mechanics

package game.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import game.base.Animation;
import game.base.AnimationHandler;
import game.base.PhysicsListener;
import game.base.Projectile;
import game.base.Sound;
import game.base.Sprite;
import game.base.Typemasks;
import game.base.Window;
import game.gui.GUIButton;
import game.gui.GUIGroup;
import game.gui.GUIImage;
import game.gui.GUIMLText;
import game.gui.GUIProgressBar;
import game.gui.GUIRect;
import game.gui.GUIText;
import game.scene.Scene;
import game.scene.SceneObject;

public class Main {
	private static final int WIN_WIDTH = 800;
	private static final int WIN_HEIGHT = 600;
	
	private static final Font smallText = new Font("Consolas", Font.PLAIN, 18);
	
	private static final Color brown = new Color(56, 36, 32);
	private static final Color lightBrown = brown.brighter().brighter();
	private static final Color red = new Color(255, 71, 71);
	private static final Color green = new Color(83, 122, 71);
	
	private static final Random random = new Random();
	
	private static Window window;
	private static Scene scene;
	
	private static Scene mainMenu;
	private static Sprite titleScreen;
	
	private static GUIGroup menuGroup;
	private static GUIGroup playerSelectGroup;
	
	private static GUIMLText credits;
	
	private static Sprite[] playerShips;
	private static Sprite[] alienShips;
	private static Sprite[] projectiles;
	private static Sprite[] pickups;
	private static Sprite[] stars;
	
	private static Sprite explosion;
	
	private static Sound buttonSound;
	
	private static Sound hitSound;
	private static Sound explodeSound;
	private static Sound pickupSound;
	
	private static KeyListener keyboard;
	
	private static Ship player;
	private static int player_id;
	private static boolean player_openingSequence;
	private static int player_dir;
	private static boolean player_firing;
	private static int player_score;
	private static double player_speed;
	private static double player_maxHealth;
	private static int player_firePower;
	private static double player_damage;
	private static boolean player_allowInput;
	
	private static int alienSpawnRate;
	
	public static void main(String[] args) {
		//Load sprites & animations
		playerShips = new Sprite[3];
		
		playerShips[0] = new Sprite("/ships/00/coalitionmk1.png", true);
		playerShips[0].addAnimation("left", new Animation("/ships/00/left/", 1, true));
		playerShips[0].addAnimation("right", new Animation("/ships/00/right/", 1, true));
		
		playerShips[1] = new Sprite("/ships/01/marinemkI.png", true);
		playerShips[1].addAnimation("left", new Animation("/ships/01/left/", 1, true));
		playerShips[1].addAnimation("right", new Animation("/ships/01/right/", 1, true));
		
		playerShips[2] = new Sprite("/ships/02/modulemk1.png", true);
		playerShips[2].addAnimation("left", new Animation("/ships/02/left/", 1, true));
		playerShips[2].addAnimation("right", new Animation("/ships/02/right/", 1, true));
		
		pickups = new Sprite[4];
		pickups[0] = new Sprite("/powerups/health.png", true);
		pickups[1] = new Sprite("/powerups/gunpowerup.png", true);
		pickups[2] = new Sprite("/powerups/damagepowerup.png", true);
		pickups[3] = new Sprite("/powerups/nukepowerup.png", true);
		
		alienShips = new Sprite[5];
		alienShips[0] = new Sprite("/aliens/00/enemy1.png", true);
		alienShips[1] = new Sprite("/aliens/01/fighterace.png", true);
		alienShips[2] = new Sprite("/aliens/02/bigfella.png", true);
		alienShips[3] = new Sprite("/aliens/03/pentaboi.png", true);
		alienShips[4] = new Sprite("/aliens/04/LARGELAD.png", true);
		
		projectiles = new Sprite[4];
		projectiles[0] = new Sprite("/projectiles/bullet.png", true);
		projectiles[1] = new Sprite("/projectiles/greenlaser.png", true);
		projectiles[2] = new Sprite("/projectiles/laser.png", true);
		projectiles[3] = new Sprite("/projectiles/plasmaball.png", true);
		
		explosion = new Sprite("/explosion/1.png", false);
		explosion.addAnimation("explode", new Animation("/explosion/", 8, false));
		
		stars = new Sprite[4];
		stars[0] = new Sprite("/stars/smallstar.png", false);
		stars[1] = new Sprite("/stars/smallstarblue.png", false);
		stars[2] = new Sprite("/stars/star.png", false);
		stars[3] = new Sprite("/stars/starblue.png", false);
		
		//Load audio
		buttonSound = new Sound("/sounds/buttonsound.wav");
//		hitSound = new Sound("/sounds/hit.wav");
//		explodeSound = new Sound("/sounds/explode.wav");
//		pickupSound = new Sound("/sounds/pickup.wav");
		
		//Init window
		window = new Window("2125", WIN_WIDTH, WIN_HEIGHT);
		
		//load up all potential window icons.
		//different settings and OSes rely upon different resolutions
		ArrayList<Image> icons = new ArrayList<Image>();
		try {
			Main main = new Main();
			icons.add(ImageIO.read(main.getClass().getResourceAsStream("/icons/icon16.png")));
			icons.add(ImageIO.read(main.getClass().getResourceAsStream("/icons/icon32.png")));
			icons.add(ImageIO.read(main.getClass().getResourceAsStream("/icons/icon64.png")));
			icons.add(ImageIO.read(main.getClass().getResourceAsStream("/icons/icon128.png")));
			icons.add(ImageIO.read(main.getClass().getResourceAsStream("/icons/icon256.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		window.setIconImages(icons);
		
		window.pack();
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		
		window.createBufferStrategy(3);
		
		scene = new Scene(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		scene.setBackground(Color.black);
		
		mainMenu = new Scene(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		mainMenu.setBackground(Color.black);
		
		window.setScene(mainMenu);
		window.start();
		
		loadMainMenuGUI();
		loadPlayerSelectGUI();
		
		initKeyboard();
		
		window.showFPS(true);
	}
	
	//returns a random double from min to max inclusive
	public static double getRandom(double min, double max) {
		return min + random.nextDouble() * ((max-min) + 1);
	}
	
	public static void loadMainMenuGUI() {
		menuGroup = new GUIGroup();
		
		GUIText loading = new GUIText(0, 0, "loading...");
		loading.setPos(WIN_WIDTH/2-loading.getWidth()/2, WIN_HEIGHT/2-loading.getHeight()/2);
		loading.setTextColor(Color.white);
		mainMenu.add(loading);
		
		//Title screen GUI
		titleScreen = new Sprite("/mainmenu/1.png", false);
		titleScreen.addAnimation("main", new Animation("/mainmenu/", 500, false));
		
		credits = new GUIMLText(0, 50, "E v e r y t h i n g\nTrenton");
		credits.setPos(WIN_WIDTH/2-credits.getWidth()/2, WIN_HEIGHT/2-credits.getHeight()/2+50);
		credits.setTextColor(lightBrown);
		credits.setVisible(false);
		
		int padding = 90;
		int x = WIN_WIDTH-200;
		int y = WIN_HEIGHT/2-(2*(32+padding))/2;
		
		GUIButton startButton = new GUIButton(x, y, 100, 32, "Play") {
			public void onClick(MouseEvent e) {
				buttonSound.play();
				menuGroup.setVisible(false);
				playerSelectGroup.setVisible(true);
			}
		};
		startButton.setBorderColor(lightBrown);
		startButton.setTextColor(lightBrown);
		
		GUIRect separator = new GUIRect(x, y+padding/2+16, 100, 3);
		separator.setColor(lightBrown);
		
		y += padding;
		
		GUIButton creditsButton = new GUIButton(x, y, 100, 32, "Credits") {
			public void onClick(MouseEvent e) {
				buttonSound.play();
				
				if(this.getText().equals("Credits")) {
					menuGroup.setVisible(false);
					credits.setVisible(true);
					setText("Back", false);
					setVisible(true);
				}
				else {
					menuGroup.setVisible(true);
					credits.setVisible(false);
					setText("Credits", false);
				}
			}
		};
		creditsButton.setBorderColor(lightBrown);
		creditsButton.setTextColor(lightBrown);
		
		GUIRect separator2 = new GUIRect(x, y+padding/2+16, 100, 3);
		separator2.setColor(lightBrown);
		
		y += padding;
		
		GUIButton exitButton = new GUIButton(x, y, 100, 32, "Exit") {
			public void onClick(MouseEvent e) {
				buttonSound.play();
				System.exit(0);
			}
		};
		exitButton.setBorderColor(lightBrown);
		exitButton.setTextColor(lightBrown);
		
		mainMenu.remove(loading);
		
		menuGroup.add(startButton);
		menuGroup.add(separator);
		menuGroup.add(creditsButton);
		menuGroup.add(separator2);
		menuGroup.add(exitButton);
		menuGroup.add(credits);
		
		menuGroup.addToScene(mainMenu);
		
		SceneObject titleGraphic = new SceneObject(titleScreen, 0, 0, Typemasks.Default);
		titleGraphic.getAnimationHandler().setFrameDuration(0);
		mainMenu.add(titleGraphic);
		
		titleGraphic.playAnimation("main", true, true);
	}
	
	public static void loadPlayerSelectGUI() {
		playerSelectGroup = new GUIGroup();
		
		GUIRect rect = new GUIRect(0, 0, WIN_WIDTH, WIN_HEIGHT);
		playerSelectGroup.add(rect);
		
		GUIText header = new GUIText(0, 30, "SELECT YOUR SHIP");
		header.setTextColor(lightBrown);
		header.centerAtX(WIN_WIDTH/2);
		playerSelectGroup.add(header);
		
		GUIButton backButton = new GUIButton(0, WIN_HEIGHT-78, 100, 32, "Back") {
			public void onClick(MouseEvent e) {
				buttonSound.play();
				
				playerSelectGroup.setVisible(false);
				menuGroup.setVisible(true);
				credits.setVisible(false);
			}
		};
		backButton.centerAtX(WIN_WIDTH/2);
		backButton.setTextColor(lightBrown);
		backButton.setBorderColor(lightBrown);
		playerSelectGroup.add(backButton);
		
		int padding = WIN_WIDTH/3;
		int y = (int)(WIN_HEIGHT*0.2);
		
		int x = WIN_WIDTH/2-padding;
		
		String[] shipNames = {"Coalition Mk I", "Marine Mk I", "Module Mk I"};
		double[] shipSpeeds = {0.5, 0.3, 0.9};
		double[] shipAttacks = {0.6, 0.9, 0.4};
		double[] shipHealths = {0.7, 1.0, 0.5};
		
		for(int i = 0; i < 3; i++) {
			GUIText shipName = new GUIText(0, y, shipNames[i]);
			shipName.centerAtX(x);
			shipName.setTextColor(lightBrown);
			
			GUIText shipSpeedLabel = new GUIText(0, y+40, "Speed:");
			shipSpeedLabel.alignRight(x-10);
			shipSpeedLabel.setTextColor(lightBrown);
			shipSpeedLabel.setFont(smallText);
			
			GUIProgressBar shipSpeedBar = new GUIProgressBar(x-5, y+50, 80, 10, green);
			shipSpeedBar.setColor(red);
			shipSpeedBar.setProgress(shipSpeeds[i]);
			
			GUIText shipAttackLabel = new GUIText(0, y+60, "Attack:");
			shipAttackLabel.alignRight(x-7);
			shipAttackLabel.setTextColor(lightBrown);
			shipAttackLabel.setFont(smallText);
			
			GUIProgressBar shipAttackBar = new GUIProgressBar(x-5, y+70, 80, 10, green);
			shipAttackBar.setColor(red);
			shipAttackBar.setProgress(shipAttacks[i]);
			
			GUIText shipHealthLabel = new GUIText(0, y+80, "Health:");
			shipHealthLabel.alignRight(x-7);
			shipHealthLabel.setTextColor(lightBrown);
			shipHealthLabel.setFont(smallText);
			
			GUIProgressBar shipHealthBar = new GUIProgressBar(x-5, y+90, 80, 10, green);
			shipHealthBar.setColor(red);
			shipHealthBar.setProgress(shipHealths[i]);
			
			GUIImage ship = new GUIImage(0, y+200, playerShips[i]);
			ship.centerAtX(x);
			
			GUIRect shipBox = new GUIRect(0, y+195, 70, 70);
			shipBox.centerAtX(x);
			shipBox.setColor(lightBrown);
			
			playerSelectGroup.add(shipName);
			playerSelectGroup.add(shipSpeedLabel);
			playerSelectGroup.add(shipSpeedBar);
			playerSelectGroup.add(shipAttackLabel);
			playerSelectGroup.add(shipAttackBar);
			playerSelectGroup.add(shipHealthLabel);
			playerSelectGroup.add(shipHealthBar);
			playerSelectGroup.add(shipBox);
			playerSelectGroup.add(ship);
			
			x += padding;
		}
		
		x -= padding*3;
		
		GUIButton selectShip0 = new GUIButton(0, y+280, 100, 32, "Select") {
			public void onClick(MouseEvent e) {
				buttonSound.play();
				setPlayerID(0);
			}
		};
		selectShip0.centerAtX(x);
		selectShip0.setBorderColor(lightBrown);
		selectShip0.setTextColor(lightBrown);
		
		x += padding;
		
		GUIButton selectShip1 = new GUIButton(0, y+280, 100, 32, "Select") {
			public void onClick(MouseEvent e) {
				buttonSound.play();
				setPlayerID(1);
			}
		};
		selectShip1.centerAtX(x);
		selectShip1.setBorderColor(lightBrown);
		selectShip1.setTextColor(lightBrown);
		
		x += padding;
		
		GUIButton selectShip2 = new GUIButton(0, y+280, 100, 32, "Select") {
			public void onClick(MouseEvent e) {
				buttonSound.play();
				setPlayerID(2);
			}
		};
		selectShip2.centerAtX(x);
		selectShip2.setBorderColor(lightBrown);
		selectShip2.setTextColor(lightBrown);
		
		playerSelectGroup.add(selectShip0);
		playerSelectGroup.add(selectShip1);
		playerSelectGroup.add(selectShip2);
		
		playerSelectGroup.setVisible(false);
		playerSelectGroup.addToScene(mainMenu);
	}
	
	public static void showMainMenu() {
		window.stop();
		window.setScene(mainMenu);
		menuGroup.setVisible(true);
		playerSelectGroup.setVisible(false);
		credits.setVisible(false);
		window.start();
	}
	
	public static void initKeyboard() {
		keyboard = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(player != null && player_allowInput) {
					int code = e.getKeyCode();
					if(code == KeyEvent.VK_LEFT && player.getX() > 0)
						player_dir |= 1;
					else if(code == KeyEvent.VK_RIGHT && player.getX()+player.getWidth() < WIN_WIDTH)
						player_dir |= 2;
					else if(code == KeyEvent.VK_UP && player.getY() > 0)
						player_dir |= 4;
					else if(code == KeyEvent.VK_DOWN && player.getY()+player.getHeight() < WIN_HEIGHT)
						player_dir |= 8;
					else if(code == KeyEvent.VK_SPACE)
						player_firing = true;
					
					updatePlayer();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(player != null && player_allowInput) {
					int code = e.getKeyCode();
					if(code == KeyEvent.VK_LEFT)
						player_dir &= ~1;
					else if(code == KeyEvent.VK_RIGHT)
						player_dir &= ~2;
					else if(code == KeyEvent.VK_UP)
						player_dir &= ~4;
					else if(code == KeyEvent.VK_DOWN)
						player_dir &= ~8;
					else if(code == KeyEvent.VK_SPACE)
						player_firing = false;
					
					updatePlayer();
				}
				
			}

			@Override
			public void keyTyped(KeyEvent e) {}
		};
	}
	
	public static void setPlayerID(int id) {
		if(id < 0 || id >= 3)
			return;
		
		player_allowInput = true;
		
		player_id = id;
		player_score = 0;
		player_dir = 0;
		player_firing = false;
		player_firePower = 0;
		
		switch(id) {
		case 0:
			player_speed = 250.0;
			player_maxHealth = 120.0;
			player_damage = 40;
			break;
		case 1:
			player_speed = 160.0;
			player_maxHealth = 200.0;
			player_damage = 70;
			break;
		case 2:
			player_speed = 375.0;
			player_maxHealth = 80.0;
			player_damage = 20;
			break;
		default:
			return;
		}
		
		startGame();
	}
	
	public static void updatePlayer() {
		if(player == null || player_openingSequence)
			return;
		
		double x = 0.0;
		double y = 0.0;
		
		if((player_dir & 1) > 0)
			x -= player_speed;
		if((player_dir & 2) > 0)
			x += player_speed;
		if((player_dir & 4) > 0)
			y -= player_speed;
		if((player_dir & 8) > 0)
			y += player_speed;
		
		player.setVel(x, y);
		
		if(x > 0)
			player.playAnimation("right", true, true);
		else if(x < 0)
			player.playAnimation("left", true, true);
		else
			player.stopAnimation();
	}
	
	public static void resetScene() {
		scene.clearBackground();
		scene.clearGUIObjects();
		scene.clear();
		scene.setPhysicsListener(null);
		scene.resetTicks();
	}
	
	public static void startGame() {
		resetScene();
		
		//Star background
		for(int i = 0; i < 200; i++) {
			int x = random.nextInt(WIN_WIDTH);
			int y = random.nextInt(WIN_HEIGHT+31 + 30) - 30;
			scene.addToBackground(new SceneObject(stars[random.nextInt(4)], x, y, Typemasks.Default) {
				public void onSpawn() {
					yvel = 40 * random.nextDouble() + 0.02;
				}
				
				public void tick(double dt) {
					move(dt);
					
					if(y > WIN_HEIGHT+30) {
						y = -30;
						x = random.nextInt(WIN_WIDTH);
					}
				}
			});
		}
		
		GUIText startText = new GUIText(0, 0, "START");
		startText.centerAt(WIN_WIDTH/2, WIN_HEIGHT/2-60);
		startText.setTextColor(Color.white);
		scene.add(startText);
		
		GUIText healthLabel = new GUIText(16, WIN_HEIGHT-80, "Health:");
		healthLabel.setTextColor(Color.white);
		healthLabel.setFont(smallText);
		
		GUIProgressBar healthBar = new GUIProgressBar(90, WIN_HEIGHT-68, 100, 8, green);
		healthBar.setColor(red);
		healthBar.setProgress(1);

		GUIText scoreLabel = new GUIText(16, WIN_HEIGHT-50, "Score:");
		scoreLabel.setTextColor(Color.white);
		scoreLabel.setFont(smallText);
		
		GUIText scoreText = new GUIText(90, WIN_HEIGHT-50, "0");
		scoreText.setTextColor(Color.white);
		scoreText.setFont(smallText);
		
		scene.add(healthLabel);
		scene.add(healthBar);
		scene.add(scoreLabel);
		scene.add(scoreText);
		
		Sprite playerSprite = playerShips[player_id];
		
		int x = WIN_WIDTH/2-playerSprite.width/2;
		
		player = new Ship(playerSprite, x, WIN_HEIGHT+10,
				Typemasks.Player) {
			public void tick(double dt) {
				move(dt);
				
				if(player_openingSequence)
					return;
				
				if(x < 0) {
					x = 0;
					player_dir &= ~1;
					updatePlayer();
				}
				else if(x > WIN_WIDTH-getWidth()) {
					x = WIN_WIDTH-getWidth();
					player_dir &= ~2;
					updatePlayer();
				}
				
				if(y < 0) {
					y = 0;
					player_dir &= ~4;
					updatePlayer();
				}
				else if(y > WIN_HEIGHT-getHeight()) {
					y = WIN_HEIGHT-getHeight();
					player_dir &= ~8;
					updatePlayer();
				}
				
				if(player_firing && currentTick % 5 == 0) {
					for(int i = -player_firePower; i <= player_firePower; i++) {
						double vel = i*90;
						
						scene.add(new Projectile(projectiles[0], 0, y, Typemasks.PlayerProjectile, 500, player_damage) {
							public void onSpawn() {
								centerAtX(player.getX()+player.getWidth()/2);
								yvel = -500;
								xvel = vel;
							}
							
							@Override
							public void onDeath() {
								scene.remove(this);
							}
						});
					}
				}
			}

			public void onCollision(SceneObject other) {
				if((other.getTypemask() & Typemasks.EnemyProjectile) > 0) {
					damage(((Projectile)other).getDamage());
					((Projectile)other).onDeath();
				}
				else if((other.getTypemask() & Typemasks.Pickup) > 0) {
					Sprite pickup = other.getSprite();
					if(pickup == pickups[0]) {
						setHealth(player_maxHealth);
						healthBar.setProgress(1);
					}
					else if(pickup == pickups[1]) {
						if(player_firePower == 0)
							player_firePower++;
					}
					else if(pickup == pickups[2])
						player_damage += 10;
					else if(pickup == pickups[3]) {
						ArrayList<SceneObject> objs = scene.getSceneObjects();
						for(int i = objs.size()-1; i >= 0; i--) {
							SceneObject obj = objs.get(i);
							if((obj.getTypemask() & Typemasks.Enemy) > 0)
								((AIShip)obj).die();
							else if((obj.getTypemask() & Typemasks.EnemyProjectile) > 0)
								((Projectile)obj).onDeath();
						}
					}
					
					scene.remove(other);
					player_score += 30;
				}
			}
			
			@Override
			public void onDeath() {
				handleDeath(player);
				player_allowInput = false;
				
				GUIRect overlay = new GUIRect(0, 0, WIN_WIDTH, WIN_HEIGHT);
				overlay.setColor(new Color(0, 0, 0, 128));
				
				GUIText gameoverText = new GUIText(0, 280, "GAME OVER");
				gameoverText.centerAtX(WIN_WIDTH/2);
				gameoverText.setTextColor(Color.white);
				
				GUIButton menuButton = new GUIButton(0, 350, 100, 32, "Menu") {
					public void onClick(MouseEvent e) {
						buttonSound.play();
						showMainMenu();
						resetScene();
					}
				};
				menuButton.centerAtX(WIN_WIDTH/2);
				menuButton.setTextColor(lightBrown);
				menuButton.setBorderColor(lightBrown);
				
				scene.add(overlay);
				scene.add(gameoverText);
				scene.add(menuButton);
			}

			@Override
			public void onDamage() {
				healthBar.setProgress(health/player_maxHealth);
			}
		};
		player.setHealth(player_maxHealth);
		scene.add(player);
		
		SceneObject wingmanLeft = new SceneObject(playerSprite, x-84, WIN_HEIGHT+94,
				Typemasks.Default);
		SceneObject wingmanRight = new SceneObject(playerSprite, x+84, WIN_HEIGHT+94,
				Typemasks.Default);
		
		scene.add(wingmanLeft);
		scene.add(wingmanRight);
		
		double[] alienSpawnChances = {0.8, 0.6, 0.6, 0.5, 0.2};
		alienSpawnRate = 150;
		
		scene.setPhysicsListener(new PhysicsListener() {
			@Override
			public void tick(double dt, int totalTicks) {
				scoreText.setText(Integer.toString(player_score), false);
				if(player_openingSequence && totalTicks >= 196) {
					player_openingSequence = false;
					startText.setVisible(false);
					
					scene.remove(wingmanLeft);
					scene.remove(wingmanRight);
					
					updatePlayer();
				}
				else if(player_openingSequence) {
					if(totalTicks % 14 == 0)
						startText.setVisible(!startText.isVisible());
					
					if(totalTicks == 120) {
						player.setVel(0, 0);
						wingmanLeft.playAnimation("left", true, true);
						wingmanRight.playAnimation("right", true, true);
						
						wingmanLeft.setVel(-250, 120);
						wingmanRight.setVel(250, 120);
					}
				}
				else {
					if(totalTicks % alienSpawnRate == 0) {
						int index = random.nextInt(alienSpawnChances.length);
						double rand = random.nextDouble();
						
						if(rand <= alienSpawnChances[index])
							spawnEnemyShip(index);
						
						if(alienSpawnRate > 10)
							alienSpawnRate -= 5;
					}
					
					if(totalTicks % 250 == 0) {
						for(int i = 0; i < alienSpawnChances.length; i++)
							alienSpawnChances[i] += getRandom(0, 0.1);
					}
					
					if(totalTicks % 350 == 0) {
						int index = random.nextInt(pickups.length);
						Sprite pickup = pickups[index];
						double x = getRandom(pickup.width, WIN_WIDTH-pickup.width*2);
						
						scene.add(new SceneObject(pickup, x, -50, Typemasks.Pickup) {
							public void tick(double dt) {
								move(dt);
								
								xvel = Math.cos(currentTick * 0.05) * 100;
								yvel = 80;
								
								if(y > WIN_HEIGHT)
									scene.remove(this);
							}
						});
					}
				}
			}
		});
		
		player_openingSequence = true;
		player.setVel(0, -80); 
		wingmanLeft.setVel(0, -80);
		wingmanRight.setVel(0, -80);
		
		scene.addKeyListener(keyboard);
		
		window.stop();
		window.setScene(scene);
		window.start();
	}
	
	public static void handleDeath(Ship ship) {
		scene.add(new SceneObject(explosion, 0, 0, Typemasks.Default) {
			public void onSpawn() {
				//explodeSound.play();
				centerAt(ship.getCenterX(), ship.getCenterY());
				getAnimationHandler().setFrameDuration(1);
				playAnimation("explode", true, true);
			}
			
			public void tick(double dt) {
				AnimationHandler handler = getAnimationHandler();
				
				if(handler.isPlayingForward() && handler.getCurrentFrameNumber() == 7)
					scene.remove(this); //playAnimation("explode", true, false);
//				else if(handler.isPlayingForward() == false && handler.getCurrentFrameNumber() == 0)
//					scene.remove(this);
			}
		});
		
		scene.remove(ship);
	}
	
	public static void handleShipMove(AIShip ship) {
		double x = ship.getX() + getRandom(-60, 60);
		double y = ship.getY() + getRandom(-60, 60);
		
		if(x < 0) x = 0;
		else if(x > WIN_WIDTH - ship.getWidth()) x = WIN_WIDTH-ship.getWidth();
		
		if(y < 0) y = 0;
		else if(y > WIN_HEIGHT/2) y = WIN_HEIGHT/2;
		
		ship.setTargetPos(x, y);
	}
	
	public static void spawnEnemyShip(int id) {
		final int[] shipHealth = {100, 130, 150, 200, 600};
		final int spawnDelay = 120;
		
		double x = random.nextInt(WIN_WIDTH-50);
		double y = -160;
		
		AIShip ship;
		
		switch(id) {
		case 0:
			ship = new AIShip(alienShips[0], x, y, Typemasks.Enemy) {
				@Override
				public void onDeath() {
					handleDeath(this);
					player_score += 10;
				}
				
				public void tick(double dt) {
					move(dt);
					
					if(currentTick > spawnDelay && currentTick % 38 == 0) {
						Projectile p = new Projectile(projectiles[1], 0, 0, Typemasks.EnemyProjectile, 500, 10) {
							@Override
							public void onDeath() {
								scene.remove(this);
							}
						};
						p.centerAt(getCenterX(), y+getHeight()+p.getHeight()/2);
						p.setVel(0, 150);
						
						scene.add(p);
					}
				}

				@Override
				public void onTargetReached() {
					handleShipMove(this);
				}

				@Override
				public void onDamage() {
					//hitSound.play();
				}
			};
			break;
			
		case 1:
			ship = new AIShip(alienShips[1], x, y, Typemasks.Enemy) {
				@Override
				public void onDeath() {
					handleDeath(this);
					player_score += 20;
				}
				
				public void tick(double dt) {
					move(dt);
					
					if(currentTick > spawnDelay && currentTick % 30 == 0) {
						Projectile p = new Projectile(projectiles[2], 0, 0, Typemasks.EnemyProjectile, 500, 10) {
							@Override
							public void onDeath() {
								scene.remove(this);
							}
							
							public void tick(double dt) {
								move(dt);
								xvel = Math.cos(currentTick*0.5) * 100;
							}
						};
						p.centerAt(getCenterX()-8, y+getHeight()+p.getHeight()/2);
						p.setVel(0, 200);
						
						scene.add(p);
						
						Projectile p2 = new Projectile(projectiles[2], 0, 0, Typemasks.EnemyProjectile, 500, 10) {
							@Override
							public void onDeath() {
								scene.remove(this);
							}
							
							public void tick(double dt) {
								move(dt);
								xvel = -Math.cos(currentTick*0.5) * 100;
							}
						};
						p2.centerAt(getCenterX()+8, y+getHeight()+p2.getHeight()/2);
						p2.setVel(0, 200);
						
						scene.add(p2);
					}
				}

				@Override
				public void onTargetReached() {
					handleShipMove(this);
				}

				@Override
				public void onDamage() {
					//hitSound.play();
				}
			};
			break;
			
		case 2:
			ship = new AIShip(alienShips[2], x, y, Typemasks.Enemy) {
				@Override
				public void onDeath() {
					handleDeath(this);
					player_score += 40;
				}
				
				public void tick(double dt) {
					move(dt);
					
					if(currentTick > spawnDelay && currentTick % 50 == 0) {
						for(int i = -50; i <= 50; i += 50) {
							Projectile p = new Projectile(projectiles[1], 0, 0, Typemasks.EnemyProjectile, 500, 10) {
								@Override
								public void onDeath() {
									scene.remove(this);
								}
							};
							
							p.centerAt(getCenterX(), y+getHeight()+p.getHeight()/2);
							p.setVel(i, 100);
							
							scene.add(p);
						}
					}
				}

				@Override
				public void onTargetReached() {
					handleShipMove(this);
				}

				@Override
				public void onDamage() {
					//hitSound.play();
				}
			};
			break;
			
		case 3:
			ship = new AIShip(alienShips[3], x, y, Typemasks.Enemy) {
				@Override
				public void onDeath() {
					handleDeath(this);
					player_score += 60;
				}
				
				public void tick(double dt) {
					move(dt);
					
					if(currentTick > spawnDelay && currentTick % 80 == 0) {
						for(int i = 0; i < 360; i += 30) {
							double x = Math.cos(i*Math.PI/180) * 20 + getCenterX();
							double y = Math.sin(i*Math.PI/180) * 20 + getCenterY();
							
							Projectile p = new Projectile(projectiles[3], 0, 0, Typemasks.EnemyProjectile, 500, 10) {
								@Override
								public void onDeath() {
									scene.remove(this);
								}
							};
							
							p.centerAt(x, y);
							p.setVel((getCenterX()-x)*((i+80)*Math.PI/180), (getCenterY()-y)*((i+80)*Math.PI/180));
							
							scene.add(p);
						}
					}
				}

				@Override
				public void onTargetReached() {
					handleShipMove(this);
				}

				@Override
				public void onDamage() {
					//hitSound.play();
				}
			};
			break;
			
		case 4:
			ship = new AIShip(alienShips[4], x, y, Typemasks.Enemy) {
				@Override
				public void onDeath() {
					handleDeath(this);
					player_score += 100;
				}
				
				public void tick(double dt) {
					move(dt);
					
					if(currentTick > spawnDelay && currentTick % 35 == 0) {
						for(int i = 0; i <= 5; i++) {
							Projectile p = new Projectile(projectiles[3], 0, 0, Typemasks.EnemyProjectile, 500, 10) {
								@Override
								public void onDeath() {
									scene.remove(this);
								}
								
								public void tick(double dt) {
									move(dt);
									yvel += 9;
									
									if(currentTick < 30)
										xvel = (getRandom(player.getX(), player.getX()+player.getWidth())-x);
								}
							};
							
							p.centerAt(getCenterX() + getRandom(-10, 10), y-p.getHeight()/2);
							p.setVel(getRandom(-800, 800), -getRandom(100, 200));
							
							scene.add(p);
						}
					}
				}

				@Override
				public void onTargetReached() {
					handleShipMove(this);
				}

				@Override
				public void onDamage() {
					//hitSound.play();
				}
			};
			break;
		default:
			return;
		}
		
		ship.setHealth(shipHealth[id]);
		ship.setTargetPos(random.nextInt(WIN_WIDTH-80), random.nextInt(WIN_HEIGHT/3));
		scene.add(ship);
	}
}
