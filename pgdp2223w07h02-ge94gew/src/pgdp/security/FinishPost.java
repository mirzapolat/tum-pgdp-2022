package pgdp.security;

public class FinishPost extends FlagPost{

    public FinishPost(int postNumber) {
        super(postNumber);
    }

    public boolean up(String type) {
        String switchTo = null;
        switch (getDepiction()) {
            case "" -> switchTo = switch (type) {
                case "green" -> "green";
                case "blue" -> "blue";
                case "yellow" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "chequered";
                default -> null;
            };
            case "green" -> switchTo = switch (type) {
                case "blue" -> "green/blue";
                case "yellow" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "chequered";
                default -> null;
            };
            case "blue" -> switchTo = switch (type) {
                case "green" -> "green/blue";
                case "yellow" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "chequered";
                default -> null;
            };
            case "green/blue" -> switchTo = switch (type) {
                case "yellow" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "chequered";
                default -> null;
            };
            case "yellow" -> switchTo = switch (type) {
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "chequered";
                default -> null;
            };
            case "doubleYellow" -> switchTo = switch (type) {
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "chequered";
                default -> null;
            };
            case "doubleYellow/[SC]" -> switchTo = switch (type) {
                case "red" -> "red";
                case "end" -> "chequered";
                default -> null;
            };
            case "red" -> switchTo = switch (type) {
                case "end" -> "chequered";
                default -> null;
            };
            default -> switchTo = null;
        }

        if (switchTo == null) return false;
        else {
            setDepiction(switchTo);
            setLevel(switch (switchTo) {
                case "green", "blue", "green/blue" -> 1;
                case "yellow" -> 2;
                case "doubleYellow", "doubleYellow/[SC]" -> 3;
                case "red" -> 4;
                case "chequered" -> 5;
                default -> 0;
            });
            return true;
        }
    }

    public String toString() {
        if (getLevel() == 0)
            return "Signal post " + getPostNumber() + " of type finish post is in level " + getLevel() + " and is  doing nothing";
        else
            return "Signal post " + getPostNumber() + " of type finish post is in level " + getLevel() + " and is  waving  " + getDepiction();
    }
}
