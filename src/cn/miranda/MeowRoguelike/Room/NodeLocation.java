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

    /**
     * 获取相对偏移 X
     *
     * @return 相对偏移 X
     */
    public int getX() {
        return this.x;
    }

    /**
     * 获取相对偏移 Y
     *
     * @return 相对偏移 Y
     */
    public int getY() {
        return this.y;
    }

    /**
     * 获取相对偏移 Z
     *
     * @return 相对偏移 Z
     */
    public int getZ() {
        return this.z;
    }

    /**
     * 获取相对偏移的数组
     *
     * @return 相对偏移的数组
     */
    public int[] getInt() {
        return new int[]{this.x, this.y, this.z};

    }

    /**
     * 添加偏移值
     *
     * @param nodeLocation 偏移相对坐标
     * @return 偏移后的相对坐标
     */
    public NodeLocation add(NodeLocation nodeLocation) {
        return new NodeLocation(this.x + nodeLocation.getX(), this.y + nodeLocation.getY(), this.z + nodeLocation.getZ());
    }

    /**
     * 判断两个相对坐标是否相等
     *
     * @param node 另一个相对坐标
     * @return 如果相等则返回 true 否则返回 false
     */
    public boolean equals(NodeLocation node) {
        return Arrays.equals(node.getInt(), this.getInt());
    }
}
