package sophomoreproject.game.systems;

public class GameServer {
    private GameWorld world;

    public void run(float dt) {
        world.serverOnly(dt);
        world.update(dt);
    }
}
