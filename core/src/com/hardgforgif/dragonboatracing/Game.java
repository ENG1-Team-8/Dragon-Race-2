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

		// Calculate the ratio between pixels, meters and tiles
		GameData.TILES_TO_METERS = map.getTilesToMetersRatio();
		GameData.PIXELS_TO_TILES = 1 / (GameData.METERS_TO_PIXELS * GameData.TILES_TO_METERS);

		// Create the collision with the land
		map.createMapCollisions("CollisionLayerLeft", world);
		map.createMapCollisions("CollisionLayerRight", world);

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
	 * objects collide
	 * 
	 * @param world This is the physics world in which the collisions happen
	 */
	private void createContactListener(World world) {
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
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
	 * Sets the camera y position at the y position of a player's sprite
	 * 
	 * @param player The target player
	 */
	private void updateCamera(Player player) {
		camera.position.set(camera.position.x, player.boatSprite.getY() + 600, 0);
		camera.update();
	}

	/**
	 * Updates the GameData.standings array by comparing boats positions
	 */
	private void updateStandings() {
		// If the player hasn't finished the race...
		if (!player.hasFinished()) {
			// Reset his position
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
				// Reset his position
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
	 * Updates the GameData.results list by adding a new result every time a boat
	 * finishes the game
	 */
	private void checkForResults() {

		float time = GameData.currentTimer;
		float[] penalties = GameData.penalties;

		// If the player has finished and we haven't added his result already...
		if (player.hasFinished() && player.acceleration > 0 && GameData.results.size() < 4) {
			// Add the result to the list with key 0, the player's lane
			GameData.results.add(new Float[] { 0f, time });
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
			// If the AI has finished and we haven't added his result already...
			if (opponents[i].hasFinished() && opponents[i].acceleration > 0 && GameData.results.size() < 4) {
				// Add the result to the list with the his lane numer as key
				GameData.results.add(new Float[] { Float.valueOf(i + 1), time });
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
	 * necessary
	 */
	private void updatePenalties() {
		// Update the penalties for the player, if he is outside his lane
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

	/**
	 * This method marks all the boats that haven't finished the race as dnfs
	 */
	private void dnfRemainingBoats() {
		// If the player hasn't finished
		if (!player.hasFinished() && player.robustness > 0 && GameData.results.size() < 4) {
			// Add a dnf result
			GameData.results.add(new Float[] { 0f, Float.MAX_VALUE });

			// Transition to the showResult screen
			GameData.showResultsState = true;
			GameData.currentUI = new ResultsUI();
		}

		// Iterate through the AI and add a dnf result for any who haven't finished
		for (int i = 0; i < 3; i++) {
			if (!opponents[i].hasFinished() && opponents[i].robustness > 0 && GameData.results.size() < 4)
				GameData.results.add(new Float[] { Float.valueOf(i + 1), Float.MAX_VALUE });
		}
	}

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

		// Otherwise we need need to reset elements of the game to prepare for the next
		// race
		else if (GameData.resetGameState) {
			// MODIFIED: large portion of code turned into reset function
			reset();
		}

		// MODIFIED: Obstacles now generated after difficulty and boat selection
		// To facilitate scaled number of obstacles
		else if (!GameData.obstaclesGenerated) {
			map.createLanes(world);
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
				// Create the AI boats
				for (int i = 1; i <= 3; i++) {
					int AIBoatType = GameData.boatTypes[i];
					opponents[i - 1] = new AI(GameData.boatsStats[AIBoatType][0], GameData.boatsStats[AIBoatType][1],
							GameData.boatsStats[AIBoatType][2], GameData.boatsStats[AIBoatType][3], AIBoatType,
							map.lanes[i]);
					opponents[i - 1].createBoatBody(world, GameData.startingPoints[i][0], GameData.startingPoints[i][1],
							"Boat1.json");
				}

				if (GameData.currentLeg == 3) {
					int maxIndex = 0;
					for (int i = 1; i < GameData.bests.length; i++) {
						if (GameData.bests[i] >= GameData.bests[maxIndex]) {
							maxIndex = i;
						}
					}
					if(maxIndex == 0){
						GameData.GameOverState = true;
						GameData.dnq = true;
						GameData.currentUI = new GameOverUI();
					} else {
						opponents[maxIndex - 1].robustness = 0;
					}
				}
			}

			// Iterate through the bodies that need to be removed from the world after a
			// collision
			for (Body body : toBeRemovedBodies) {
				// Find the obstacle that has this body and mark it as null
				// so it's sprite doesn't get rendered in future frames
				for (Lane lane : map.lanes)
					for (Obstacle obstacle : lane.obstacles)
						if (obstacle.obstacleBody == body) {
							obstacle.obstacleBody = null;
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
			for (Lane lane : map.lanes)
				for (Obstacle obstacle : lane.obstacles) {
					if (obstacle.obstacleBody != null)
						obstacle.drawObstacle(batch);
				}

			// Update the camera at the player's position
			updateCamera(player);

			updatePenalties();

			// Update the standings of each boat
			updateStandings();

			// If it's been 15 seconds since the winner completed the race, dnf all boats
			// who haven't finished yet
			// Then transition to the result state
			if (GameData.results.size() > 0 && GameData.results.size() < 4
					&& GameData.currentTimer > GameData.results.get(0)[1] + 15f) {
				dnfRemainingBoats();
				GameData.showResultsState = true;
				GameData.currentUI = new ResultsUI();
			}
			// Otherwise keep checking for new results
			else {
				checkForResults();
			}

			// Choose which UI to display based on the current state
			if (!GameData.showResultsState)
				GameData.currentUI.drawPlayerUI(UIbatch, player);
			else {
				GameData.currentUI.drawUI(UIbatch, mousePosition, Gdx.graphics.getWidth(), Gdx.graphics.getDeltaTime());
				GameData.currentUI.getInput(Gdx.graphics.getWidth(), clickPosition);
			}

		}

		// If we haven't clicked anywhere in the last frame, reset the click position
		if (clickPosition.x != 0f && clickPosition.y != 0f)
			clickPosition.set(0f, 0f);
	}

	/**
	 * A function to reset the game state
	 * 
	 * @since 2
	 * @version 2
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

		// Initialize the map
		// map = new Map("Map1/Map1.tmx", Gdx.graphics.getWidth());

		// Calculate the ratio between pixels, meters and tiles
		// GameData.TILES_TO_METERS = map.getTilesToMetersRatio();
		// GameData.PIXELS_TO_TILES = 1 / (GameData.METERS_TO_PIXELS *
		// GameData.TILES_TO_METERS);

		// Create the collision with the land
		map.createMapCollisions("CollisionLayerLeft", world);
		map.createMapCollisions("CollisionLayerRight", world);

		// Create the lanes, and the obstacles in the physics game world
		map.createLanes(world);

		// Create the finish line
		map.createFinishLine("finishLine.png");

		// Create a new collision handler for the world
		createContactListener(world);

		if (GameData.showResultsState) {
			GameData.currentLeg += 1;
			GameData.showResultsState = false;
			GameData.gamePlayState = true;
			GameData.currentUI = new GamePlayUI();
		} else {
			GameData.currentLeg = 0;
			GameData.mainMenuState = true;
			GameData.bests = new float[] { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE };
			GameData.currentUI = new MenuUI();
		}
		GameData.resetGameState = false;
	}

	public void dispose() {
		world.dispose();
	}

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

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
		clickPosition.set(position.x, position.y);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
		mousePosition.set(position.x, position.y);
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
