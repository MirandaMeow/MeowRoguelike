package cn.miranda.MeowRoguelike.Room;

import static cn.miranda.MeowRoguelike.Manager.ConfigManager.config;

public enum Direction {
    LEFT(new NodeLocation(-1, 0, 0), new NodeLocation(0, 1, (config.getInt("room.z") - 1) / 2), true),
    RIGHT(new NodeLocation(1, 0, 0), new NodeLocation(config.getInt("room.x") - 1, 1, (config.getInt("room.z") - 1) / 2), true),
    FRONT(new NodeLocation(0, 0, 1), new NodeLocation((config.getInt("room.x") - 1) / 2, 1, config.getInt("room.z") - 1), true),
    BACK(new NodeLocation(0, 0, -1), new NodeLocation((config.getInt("room.x") - 1) / 2, 1, 0), true),
    UP(new NodeLocation(0, 1, 0), new NodeLocation((config.getInt("room.x") - 1) / 2, config.getInt("room.y") - 1, (config.getInt("room.z") - 1) / 2), false),
    DOWN(new NodeLocation(0, -1, 0), new NodeLocation((config.getInt("room.x") - 1) / 2, 0, (config.getInt("room.z") - 1) / 2), false);

    private final NodeLocation offset;
    private final NodeLocation doorLocation;
    private final boolean isFlat;

    Direction(NodeLocation offset, NodeLocation doorLocation, boolean isFlat) {
        this.offset = offset;
        this.doorLocation = doorLocation;
        this.isFlat = isFlat;
    }

    /**
     * 获取方位的相对偏移
     *
     * @return 方位的相对偏移
     */
    public NodeLocation getOffset() {
        return this.offset;
    }

    /**
     * 获取门的相对偏移
     *
     * @return 门的相对偏移
     */
    public NodeLocation getDoorLocation() {
        return this.doorLocation;
    }

    /**
     * 判断是否为平面方位
     *
     * @return 是平面方位返回 true，否则返回 false
     */
    public boolean isFlat() {
        return this.isFlat;
    }

    /**
     * 返回方位的对向方位
     *
     * @return 该方位的对向方位
     */
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case BACK:
                return FRONT;
            case FRONT:
                return BACK;
            default:
                return null;
        }
    }
}
