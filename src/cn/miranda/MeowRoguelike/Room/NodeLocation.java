package cn.miranda.MeowRoguelike.Room;

import java.util.Arrays;

public class NodeLocation {
    private final int x;
    private final int y;
    private final int z;

    public NodeLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int[] getInt() {
        return new int[]{this.x, this.y, this.z};

    }

    public NodeLocation add(NodeLocation node) {
        return new NodeLocation(this.x + node.getX(), this.y + node.getY(), this.z + node.getZ());
    }

    public boolean equals(NodeLocation node) {
        return Arrays.equals(node.getInt(), this.getInt());
    }
}
