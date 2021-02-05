package cn.miranda.MeowRoguelike.Room;

public enum RoomType {
    MAIN("normal"),
    SUB("normal"),
    BOSS("boss"),
    ORIGIN("origin");

    private final String prefix;

    RoomType(String string) {
        this.prefix = string;
    }

    /**
     * 获得前缀
     *
     * @return 前缀
     */
    public String getPrefix() {
        return this.prefix;
    }
}
