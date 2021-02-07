package com.hardgforgif.dragonboatracing;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.UI.GameOverUI;
import com.hardgforgif.dragonboatracing.UI.GamePlayUI;
import com.hardgforgif.dragonboatracing.UI.MenuUI;
import com.hardgforgif.dragonboatracing.UI.ResultsUI;
import com.hardgforgif.dragonboatracing.core.AI;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Lane;
import com.hardgforgif.dragonboatracing.core.Map;
import com.hardgforgif.dragonboatracing.core.Obstacle;
import com.hardgforgif.dragonboatracing.core.Player;
import com.hardgforgif.dragonboatracing.core.Powerup;

/**
 * The main game class.
 * 
 * @since 1
 * @version 2
 * @author Team 10
 * @author Matt Tomlinson
 * @author Josh Stafford
 */
public class Game extends ApplicationAdapter implements InputProcessor {

	private Player player;
	private AI[] opponents = new AI[3];
	private Map map; // MODIFIED: array not necessary
	private Batch batch;
	private Batch UIbatch;
	private OrthographicCamera camera;
	private World world; // MODIFIED: array not necessary

	private Vector2 mousePosition = new Vector2();
	private Vector2 clickPosition = new Vector2();
	private boolean[] pressedKeys = new boolean[4]; // W, A, S, D buttons status

	private ArrayList<Body> toBeRemovedBodies = new ArrayList<>();
	private ArrayList<Body> toUpdateHealth = new ArrayList<>();

	/**
	 * @since 1
	 * @version 2
	 * @author Team 10
	 * @author Team 8
	 */
	@Override
	public void create() {

		// Initialize the sprite batches
		batch = new SpriteBatch();
		UIbatch = new SpriteBatch();

		// Get the values of the screen dimensions
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// Initialise the world and the map arrays
		// Initialize the physics game World
		// MODIFIED: For efficiency, a single world and map is used
		// Rather than iterating through an array of worlds
		world = new World(new Vector2(0f, 0f), true);

		// Initialize the map
		map = new Map("Map1/Map1.tmx", w);

		// MODIFIED: create map renderer
		map.createMapRenderer();

		// Calculate the ratio between pixels, meters and tiles
		GameData.TILES_TO_METERS = map.getTilesToMetersRatio();
		GameData.PIXELS_TO_TILES = 1 / (GameData.METERS_TO_PIXELS * GameData.TILES_TO_METERS);

		// Create the collision with the land
		map.createMapCollisions("CollisionLayerLeft", world);
		map.createMapCollisions("CollisionLayerRight", world);

		// MODIFIED: Now called when the leg loads
		// Create the lanes, and the obstacles in the physics game world
		// map[i].createLanes(world[i]);

		// Create the finish line
		// map[i].createFinishLine("finishLine.png");

		// Create a new collision handler for the world
		createContactListener(world);

		// Initialize the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);

		// Set the app's input processor
		Gdx.input.setInputProcessor(this);

	}

	/**
	 * This method creates new ContactListener who's methods are executed when
	 * objects collide.
	 * 
	 * @param world This is the physics world in which the collisions happen
	 * @since 1
	 * @version 2
	 * @author Team 10
	 * @author Josh Stafford
	 */
	private void createContactListener(World world) {

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

				// MODIFIED: Now detects if boat collides with powerup and applies power to boat
				if (fixtureA.getBody().getUserData() instanceof Powerup
						&& fixtureB.getBody().getUserData() instanceof Boat) {
					Powerup p = (Powerup) fixtureA.getBody().getUserData();
					Boat b = (Boat) fixtureB.getBody().getUserData();
					toBeRemovedBodies.add(fixtureA.getBody());
					b.applyPowerup(p.getType());
				} else if (fixtureB.getBody().getUserData() instanceof Powerup
						&& fixtureA.getBody().getUserData() instanceof Boat) {
					Powerup p = (Powerup) fixtureB.getBody().getUserData();
					Boat b = (Boat) fixtureA.getBody().getUserData();
					toBeRemovedBodies.add(fixtureB.getBody());
					b.applyPowerup(p.getType());
				} else {
					if (fixtureA.getBody().getUserData() instanceof Obstacle) {
						toBeRemovedBodies.add(fixtureA.getBody());
					} else if (fixtureB.getBody().getUserData() instanceof Obstacle) {
						toBeRemovedBodies.add(fixtureB.getBody());
					}

					if (fixtureA.getBody().getUserData() instanceof Boat) {
						toUpdateHealth.add(fixtureA.getBody());
					} else if (fixtureB.getBody().getUserData() instanceof Boat) {
						toUpdateHealth.add(fixtureB.getBody());
					}
				}
			}

			@Override
			public void endContact(Contact contact) {

			}

			@Override
			public void preSolve(Contact contact, Manifold manifold) {

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse contactImpulse) {

			}
		});

	}

	/**
	 * Sets the camera y position at the y position of a player's sprite.
	 * 
	 * @param player The target player
	 * 
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	private void updateCamera(Player player) {

		camera.position.set(camera.position.x, player.boatSprite.getY() + 600, 0);
		camera.update();

	}

	/**
	 * Updates the GameData.standings array by comparing boats positions.
	 * 
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	private void updateStandings() {

		// If the player hasn't finished the race...
		if (!player.hasFinished()) {
			// Reset their position
			GameData.standings[0] = 1;

			// For every AI that is ahead, increment by 1
			for (Boat boat : opponents)
				if (boat.boatSprite.getY() + boat.boatSprite.getHeight() / 2 > player.boatSprite.getY()
						+ player.boatSprite.getHeight() / 2) {
					GameData.standings[0]++;
				}

		}

		// Iterate through all the AIs to update their standings too
		for (int i = 0; i < 3; i++)
			// If the AI hasn't finished the race...
			if (!opponents[i].hasFinished()) {
				// Reset their position
				GameData.standings[i + 1] = 1;

				// If the player is ahead, increment the standing by 1
				if (player.boatSprite.getY() > opponents[i].boatSprite.getY())
					GameData.standings[i + 1]++;

				// For every other AI that is ahead, increment by 1
				for (int j = 0; j < 3; j++)
					if (opponents[j].boatSprite.getY() > opponents[i].boatSprite.getY())
						GameData.standings[i + 1]++;
			}

	}

	/**
	 * Check if the leg results need updating.
	 * <p>
	 * Updates the GameData.results list by adding a new result every time a boat
	 * finishes the game.
	 * 
	 * @since 1
	 * @version 2
	 * @author Team 10
	 * @author Matt Tomlinson
	 */
	private void checkForResults() {

		// MODIFIED: store the current leg time and penalties for easier access
		float time = GameData.currentTimer;
		float[] penalties = GameData.penalties;

		// If the player has finished and we haven't added their result already...
		if (player.hasFinished() && player.acceleration > 0 && GameData.results.size() < 4) {
			// Add the result to the list with key 0, the player's lane
			GameData.results.add(new Float[] { 0f, time });

			// MODIFIED: store the finishing time in 'bests' if it is the player's fastest
			// yet
			if (GameData.currentLeg != 0 && time + penalties[0] < GameData.bests[0]) {
				GameData.bests[0] = time + penalties[0];
			}

			// Transition to the results UI
			GameData.showResultsState = true;
			GameData.currentUI = new ResultsUI();

			// Change the player's acceleration so the boat stops moving
			player.acceleration = -200f;
		}

		// Iterate through the AI to see if any of them finished the race
		for (int i = 0; i < 3; i++) {
			// If the AI has finished and we haven't added their result already...
			if (opponents[i].hasFinished() && opponents[i].acceleration > 0 && GameData.results.size() < 4) {
				// Add the result to the list with the their lane numer as key
				GameData.results.add(new Float[] { Float.valueOf(i + 1), time });

				// MODIFIED: store the finishing time in 'bests' if it is the opponent's fastest
				// yet
				if (GameData.currentLeg != 0 && time + penalties[i + 1] < GameData.bests[i + 1]) {
					GameData.bests[i + 1] = time + penalties[i + 1];
				}

				// Change the AI's acceleration so the boat stops moving
				opponents[i].acceleration = -200f;
			}
		}

	}

	/**
	 * This method checks the position of all the boats to add penalties if
	 * necessary.
	 * 
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	private void updatePenalties() {

		// Update the penalties for the player, if they is outside their lane
		float boatCenter = player.boatSprite.getX() + player.boatSprite.getWidth() / 2;
		if (!player.hasFinished() && player.robustness > 0
				&& (boatCenter < player.leftLimit || boatCenter > player.rightLimit)) {
			GameData.penalties[0] += Gdx.graphics.getDeltaTime();
		}

		// Update the penalties for the opponents, if they are outside the lane
		for (int i = 0; i < 3; i++) {
			boatCenter = opponents[i].boatSprite.getX() + opponents[i].boatSprite.getWidth() / 2;
			if (!opponents[i].hasFinished() && opponents[i].robustness > 0
					&& (boatCenter < opponents[i].leftLimit || boatCenter > opponents[i].rightLimit)) {
				GameData.penalties[i + 1] += Gdx.graphics.getDeltaTime();
			}
		}

	}

	// MODIFIED: method redundant
	/**
	 * This method marks all the boats that haven't finished the race as dnfs
	 */
	// private void dnfRemainingBoats() {
	// // If the player hasn't finished
	// if (!player.hasFinished() && player.robustness > 0 && GameData.results.size()
	// < 4) {
	// // Add a dnf result
	// GameData.results.add(new Float[] { 0f, Float.MAX_VALUE });

	// // Transition to the showResult screen
	// GameData.showResultsState = true;
	// GameData.currentUI = new ResultsUI();
	// }

	// // Iterate through the AI and add a dnf result for any who haven't finished
	// for (int i = 0; i < 3; i++) {
	// if (!opponents[i].hasFinished() && opponents[i].robustness > 0 &&
	// GameData.results.size() < 4)
	// GameData.results.add(new Float[] { Float.valueOf(i + 1), Float.MAX_VALUE });
	// }
	// }

	/**
	 * @since 1
	 * @version 2
	 * @author Team 10
	 * @author Matt Tomlinson
	 */
	@Override
	public void render() {

		// Reset the screen
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// If the game is in one of the static state
		if (GameData.mainMenuState || GameData.choosingBoatState || GameData.GameOverState) {
			// Draw the UI and wait for the input
			GameData.currentUI.drawUI(UIbatch, mousePosition, Gdx.graphics.getWidth(), Gdx.graphics.getDeltaTime());
			GameData.currentUI.getInput(Gdx.graphics.getWidth(), clickPosition);

		}

		// MODIFIED: Obstacles now generated after difficulty and boat selection
		// To facilitate scaled number of obstacles
		else if (!GameData.obstaclesGenerated) {
			map.createLanes(world, GameData.difficulty[GameData.currentLeg]);
			map.createFinishLine("finishLine.png");
			GameData.obstaclesGenerated = true;
		}

		// Otherwise, if we are in the game play state
		else if (GameData.gamePlayState) {
			// If it's the first iteration in this state, the boats need to be created at
			// their starting positions
			if (player == null) {
				// Create the player boat
				int playerBoatType = GameData.boatTypes[0];
				player = new Player(GameData.boatsStats[playerBoatType][0], GameData.boatsStats[playerBoatType][1],
						GameData.boatsStats[playerBoatType][2], GameData.boatsStats[playerBoatType][3], playerBoatType,
						map.lanes[0]);
				player.createBoatBody(world, GameData.startingPoints[0][0], GameData.startingPoints[0][1],
						"Boat1.json");
				//Check for any saved data that needs to be loaded
				if (GameData.loadedState)
				{
					player.robustness=GameData.currenHPs[0];
					player.stamina=GameData.currentStamina[0];
					player.current_speed=GameData.currentSpeeds[0];
				}
				// Create the AI boats
				for (int i = 1; i <= 3; i++) {
					int AIBoatType = GameData.boatTypes[i];
					opponents[i - 1] = new AI(GameData.boatsStats[AIBoatType][0], GameData.boatsStats[AIBoatType][1],
							GameData.boatsStats[AIBoatType][2], GameData.boatsStats[AIBoatType][3], AIBoatType,
							map.lanes[i]);
					opponents[i - 1].createBoatBody(world, GameData.startingPoints[i][0], GameData.startingPoints[i][1],
							"Boat1.json");
					if (GameData.loadedState)
					{
						opponents[i-1].robustness=GameData.currenHPs[i];
						opponents[i-1].stamina=GameData.currentStamina[i];
						opponents[i-1].current_speed=GameData.currentSpeeds[i];
					}
				}

				if (GameData.loadedState)
				{
					GameData.loadedState = false;
					GameData.startingPoints = new float[][] { { 2.3f, 4f }, { 4f, 4f }, { 7f, 4f }, { 10f, 4f } };
				}

				// MODIFIED: if it is the final leg, determine who did not qualify
				if (GameData.currentLeg == 3) {
					int maxIndex = 0;

					// MODIFIED: find the boat number with the highest best time
					for (int i = 1; i < GameData.bests.length; i++) {
						if (GameData.bests[i] >= GameData.bests[maxIndex]) {
							maxIndex = i;
						}
					}

					// MODIFIED: if the boat with the highest time is the player
					if (maxIndex == 0) {
						// MODIFIED: set the game to be over with dnq = true
						GameData.GameOverState = true;
						GameData.gamePlayState = false;
						GameData.dnq = true;
						GameData.currentLeg = 0;
						GameData.currentUI = new GameOverUI();
					}

					// MODIFIED: otherwise, destroy the opponent that did not qualify and set time
					// to DNF
					else {
						world.destroyBody(opponents[maxIndex - 1].boatBody);
						GameData.results.add(new Float[] { Float.valueOf(maxIndex), Float.MAX_VALUE });
					}
				}
			}

			// Iterate through the bodies that need to be removed from the world after a
			// collision
			for (Body body : toBeRemovedBodies) {
				// Find the obstacle that has this body and mark it as null
				// so it's sprite doesn't get rendered in future frames
				for (Lane lane : map.lanes) {
					for (Obstacle obstacle : lane.obstacles)
						if (obstacle.obstacleBody == body) {
							obstacle.obstacleBody = null;
						}

					// MODIFIED: for each powerup to be removed, set the body to null
					for (Powerup powerup : lane.powerups)
						if (powerup.obstacleBody == body) {
							powerup.obstacleBody = null;
						}
				}

				// Remove the body from the world to avoid other collisions with it
				world.destroyBody(body);
			}

			// Iterate through the bodies marked to be damaged after a collision
			for (Body body : toUpdateHealth) {
				// if it's the player body
				if (player.boatBody == body && !player.hasFinished()) {
					// Reduce the health and the speed
					player.robustness -= 10f;
					player.current_speed -= 30f;

					// If all the health is lost
					if (player.robustness <= 0 && GameData.results.size() < 4) {
						// Remove the body from the world, but keep it's sprite in place
						world.destroyBody(player.boatBody);

						// Add a DNF result
						GameData.results.add(new Float[] { 0f, Float.MAX_VALUE });

						// Transition to the show result screen
						GameData.showResultsState = true;
						GameData.currentUI = new ResultsUI();
					}
				}

				// Otherwise, one of the AI has to be updated similarly
				else {
					for (int i = 0; i < 3; i++) {
						if (opponents[i].boatBody == body && !opponents[i].hasFinished()) {

							opponents[i].robustness -= 10f;
							opponents[i].current_speed -= 30f;

							if (opponents[i].robustness < 0 && GameData.results.size() < 4) {
								world.destroyBody(opponents[i].boatBody);
								GameData.results.add(new Float[] { Float.valueOf(i + 1), Float.MAX_VALUE });
							}
						}

					}
				}

			}

			toBeRemovedBodies.clear();
			toUpdateHealth.clear();

			// Advance the game world physics
			world.step(1f / 60f, 6, 2);
			// Update the timer
			GameData.currentTimer += Gdx.graphics.getDeltaTime();

			// Update the player's and the AI's movement
			player.updatePlayer(pressedKeys, Gdx.graphics.getDeltaTime());
			for (AI opponent : opponents)
				opponent.updateAI(Gdx.graphics.getDeltaTime());

			// Set the camera as the batches projection matrix
			batch.setProjectionMatrix(camera.combined);

			// Render the map
			map.renderMap(camera, batch);

			// Render the player and the AIs
			player.drawBoat(batch);
			for (AI opponent : opponents)
				opponent.drawBoat(batch);

			// Render the objects that weren't destroyed yet
			for (Lane lane : map.lanes) {
				for (Obstacle obstacle : lane.obstacles) {
					if (obstacle.obstacleBody != null)
						obstacle.drawObstacle(batch);
				}

				// MODIFIED: for each powerup with a body, draw them
				for (Powerup powerup : lane.powerups) {
					if (powerup.obstacleBody != null)
						powerup.drawObstacle(batch);
				}
			}

			// Update the camera at the player's position
			updateCamera(player);

			updatePenalties();

			// Update the standings of each boat
			updateStandings();

			// MODIFIED: player can now finish even if all other boats have finished

			// If it's been 15 seconds since the winner completed the race, dnf all boats
			// who haven't finished yet
			// Then transition to the result state
			// if (GameData.results.size() > 0 && GameData.results.size() < 4
			// && GameData.currentTimer > GameData.results.get(0)[1] + 15f) {
			// //dnfRemainingBoats();
			// GameData.showResultsState = true;
			// GameData.currentUI = new ResultsUI();
			// }
			// Otherwise keep checking for new results

			checkForResults();

			// Choose which UI to display based on the current state
			if (!GameData.showResultsState)
				GameData.currentUI.drawPlayerUI(UIbatch, player);
			else {
				GameData.currentUI.drawUI(UIbatch, mousePosition, Gdx.graphics.getWidth(), Gdx.graphics.getDeltaTime());
				GameData.currentUI.getInput(Gdx.graphics.getWidth(), clickPosition);
			}

			// MODIFIED: if the player is in gameplay, not results and presses escape, save
			// the game and reset
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !GameData.showResultsState) {
				GameData.currentPositions[0][0]=player.boatBody.getPosition().x;
				GameData.currentPositions[0][1]=player.boatBody.getPosition().y;
				GameData.currentStamina[0] = player.stamina;
				GameData.currentSpeeds[0] = player.current_speed;
				GameData.currenHPs[0] = player.robustness;

				for (int i =0; i<3; i++)
				{
					GameData.currentPositions[i+1][0]=opponents[i].boatBody.getPosition().x;
					GameData.currentPositions[i+1][1]=opponents[i].boatBody.getPosition().y;
					GameData.currentStamina[i+1] = opponents[i].stamina;
					GameData.currentSpeeds[i+1] = opponents[i].current_speed;
					GameData.currenHPs[i+1] = opponents[i].robustness;
				}

				/*for (int i =0; i<3; i++)
				{
					for (int j=0; j< map.lanes[i].obstacles.length; j++)
					{
						Obstacle obstacle = map.lanes[i].obstacles[j];
						if ((obstacle.obstacleSprite.getY()-player.boatBody.getPosition().y)<=1200
								&& (obstacle.obstacleSprite.getY()-player.boatBody.getPosition().y)>0)
						{

						}
					}
				}*/

				SaveLoad.save();
				GameData.gamePlayState = false;
				GameData.resetGameState = true;
			}

		}

		// Otherwise we need need to reset elements of the game to prepare for the next
		// race
		else if (GameData.resetGameState) {
			// MODIFIED: large portion of code turned into reset function
			reset();
		}

		// If we haven't clicked anywhere in the last frame, reset the click position
		if (clickPosition.x != 0f && clickPosition.y != 0f)
			clickPosition.set(0f, 0f);

	}

	/**
	 * A function to reset the game state.
	 * <p>
	 * Will partially reset for a new leg if showResultsState is still true,
	 * otherwise fully reset for a new game.
	 * 
	 * 
	 * @since 2
	 * @version 2
	 * @author Team 8
	 * @author Matt Tomlinson
	 */
	public void reset() {
		player = null;
		for (int i = 0; i < 3; i++)
			opponents[i] = null;
		GameData.results.clear();
		GameData.currentTimer = 0f;
		GameData.penalties = new float[4];

		// If we're coming from the result screen, then we need to advance to the next
		// leg

		camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		camera.update();

		// Reset everything for the next game
		// Initialize the physics game World
		world = new World(new Vector2(0f, 0f), true);

		// MODIFIED: new map not needed
		// Initialize the map
		// map = new Map("Map1/Map1.tmx", Gdx.graphics.getWidth());

		// MODIFIED: no longer needs recalculating
		// Calculate the ratio between pixels, meters and tiles
		// GameData.TILES_TO_METERS = map.getTilesToMetersRatio();
		// GameData.PIXELS_TO_TILES = 1 / (GameData.METERS_TO_PIXELS *
		// GameData.TILES_TO_METERS);

		// Create the collision with the land
		map.createMapCollisions("CollisionLayerLeft", world);
		map.createMapCollisions("CollisionLayerRight", world);

		// Create the lanes, and the obstacles in the physics game world
		map.createLanes(world, GameData.difficulty[GameData.currentLeg]);

		// Create the finish line
		map.createFinishLine("finishLine.png");

		// Create a new collision handler for the world
		createContactListener(world);

		// MODIFIED: if player is coming from the results screen...
		if (GameData.showResultsState) {
			GameData.currentLeg += 1;
			GameData.showResultsState = false;
			GameData.gamePlayState = true;
			GameData.currentUI = new GamePlayUI();
		} else {

			// MODIFIED: clear the body update ArrayLists to avoid null pointer exceptions
			toBeRemovedBodies.clear();
			toUpdateHealth.clear();

			GameData.currentLeg = 0;
			GameData.mainMenuState = true;
			GameData.dnq = false;

			// MODIFIED: reset the best times array
			GameData.bests = new float[] { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE };

			// MODIFIED: change back to main menu music before loading the main menu
			GameData.music.stop();
			GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Vibing.ogg"));

			GameData.currentUI = new MenuUI();
		}
		GameData.resetGameState = false;

	}

	/**
	 * @since 1
	 * @version 2
	 * @author Team 10
	 */
	@Override
	public void dispose() {

		// MODIFIED: dispose of singular world, not array
		world.dispose();

	}

	/**
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Input.Keys.W)
			pressedKeys[0] = true;
		if (keycode == Input.Keys.A)
			pressedKeys[1] = true;
		if (keycode == Input.Keys.S)
			pressedKeys[2] = true;
		if (keycode == Input.Keys.D)
			pressedKeys[3] = true;
		return true;

	}

	/**
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Input.Keys.W)
			pressedKeys[0] = false;
		if (keycode == Input.Keys.A)
			pressedKeys[1] = false;
		if (keycode == Input.Keys.S)
			pressedKeys[2] = false;
		if (keycode == Input.Keys.D)
			pressedKeys[3] = false;
		return true;

	}

	/**
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	@Override
	public boolean keyTyped(char character) {

		return false;

	}

	/**
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
		clickPosition.set(position.x, position.y);
		return true;

	}

	/**
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;

	}

	/**
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;

	}

	/**
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
		mousePosition.set(position.x, position.y);
		return true;

	}

	/**
	 * @since 1
	 * @version 1
	 * @author Team 10
	 */
	@Override
	public boolean scrolled(int amount) {

		return false;

	}

}
