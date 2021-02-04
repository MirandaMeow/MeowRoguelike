package cn.miranda.MeowRoguelike.Room;

public enum Direction {
    LEFT(new NodeLocation(-1, 0, 0), new NodeLocation(0, 12, 1), true),
    RIGHT(new NodeLocation(1, 0, 0), new NodeLocation(24, 12, 1), true),
    FRONT(new NodeLocation(0, 0, 1), new NodeLocation(12, 24, 1), true),
    BACK(new NodeLocation(0, 0, -1), new NodeLocation(12, 0, 1), true),
    UP(new NodeLocation(0, 1, 0), new NodeLocation(12, 12, 14), false),
    DOWN(new NodeLocation(0, -1, 0), new NodeLocation(12, 12, 0), false);


    private final NodeLocation offset;
    private final NodeLocation doorLocation;
    private final boolean isFlat;

    Direction(NodeLocation offset, NodeLocation doorLocation, boolean isFlat) {
        this.offset = offset;
        this.doorLocation = doorLocation;
        this.isFlat = isFlat;
    }

    public NodeLocation getOffset() {
        return this.offset;
    }

    public NodeLocation getDoorLocation() {
        return this.doorLocation;
    }

    public boolean isFlat() {
        return this.isFlat;
    }
}
