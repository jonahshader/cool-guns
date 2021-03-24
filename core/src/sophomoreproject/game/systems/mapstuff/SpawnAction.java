package sophomoreproject.game.systems.mapstuff;

import sophomoreproject.game.systems.GameServer;

public interface SpawnAction {
    void spawn(GameServer server, float x, float y);
}
