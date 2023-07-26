package pgdp.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FlagPost extends SignalPost {

    public FlagPost(int postNumber) {
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
                case "end" -> "green/yellow/red/blue";
                default -> null;
            };
            case "green" -> switchTo = switch (type) {
                case "blue" -> "green/blue";
                case "yellow" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "green/yellow/red/blue";
                default -> null;
            };
            case "blue" -> switchTo = switch (type) {
                case "green" -> "green/blue";
                case "yellow" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "green/yellow/red/blue";
                default -> null;
            };
            case "green/blue" -> switchTo = switch (type) {
                case "yellow" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "green/yellow/red/blue";
                default -> null;
            };
            case "yellow" -> switchTo = switch (type) {
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "green/yellow/red/blue";
                default -> null;
            };
            case "doubleYellow" -> switchTo = switch (type) {
                case "[SC]" -> "doubleYellow/[SC]";
                case "red" -> "red";
                case "end" -> "green/yellow/red/blue";
                default -> null;
            };
            case "doubleYellow/[SC]" -> switchTo = switch (type) {
                case "red" -> "red";
                case "end" -> "green/yellow/red/blue";
                default -> null;
            };
            case "red" -> switchTo = switch (type) {
                case "end" -> "green/yellow/red/blue";
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
                case "green/yellow/red/blue" -> 5;
                default -> 0;
            });
            return true;
        }
    }

    public boolean down(String type) {
        boolean dangerStatus = (Objects.equals(getDepiction(), "yellow")
                || Objects.equals(getDepiction(), "doubleYellow")
                || Objects.equals(getDepiction(), "doubleYellow/[SC]")
                || Objects.equals(getDepiction(), "red"));
        if (Objects.equals(type, "danger") && dangerStatus) {
            setDepiction("green");
            setLevel(1);
            return true;
        }
        else if (Objects.equals(type, "blue")) {
            if (Objects.equals(getDepiction(), "blue")) {
                setDepiction("");
                setLevel(0);
                return true;
            }
            else if (Objects.equals(getDepiction(), "green/blue")) {
                setDepiction("green");
                setLevel(1);
                return true;
            }
        }
        else if (Objects.equals(type, "green")) {
            if (Objects.equals(getDepiction(), "green")) {
                setDepiction("");
                setLevel(0);
                return true;
            }
            else if (Objects.equals(getDepiction(), "green/blue")) {
                setDepiction("blue");
                setLevel(1);
                return true;
            }
        }
        else if (Objects.equals(type, "clear")) {
            if (getLevel() == 0) return false;
            setDepiction("");
            setLevel(0);
            return true;
        }

        return false;
    }

    public String toString() {
        if (getLevel() == 0)
            return "Signal post " + getPostNumber() + " of type  flag post  is in level " + getLevel() + " and is  doing nothing";
        else
            return "Signal post " + getPostNumber() + " of type  flag post  is in level " + getLevel() + " and is  waving  " + getDepiction();
    }
}
