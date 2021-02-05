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

    public String getPrefix() {
        return this.prefix;
    }
}
