package team.catgirl.plastic.world;

import team.catgirl.collar.api.location.Location;

/**
 * TODO: replace with {@link team.catgirl.collar.api.entities.Entity}
 */
@Deprecated
public interface Entity {
    /**
     * @return network id of entity
     */
    int networkId();

    /**
     * @return current position
     */
    Location location();
}
