package sophomoreproject.game.systems;

public class GameClient {
    private GameWorld world;

    public void run(float dt) {
        world.update(dt);
    }
}
