package pgdp.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class LightPanel extends SignalPost {

    public LightPanel(int postNumber) {
        super(postNumber);
    }

    public boolean up(String type) {
        String switchTo = null;
        switch (getDepiction()) {
            case "" -> switchTo = switch (type) {
                case "green" -> "green";
                case "blue" -> "blue";
                case "yellow", "end" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "[SC]";
                case "red" -> "red";
                default -> null;
            };
            case "green" -> switchTo = switch (type) {
                case "blue" -> "blue";
                case "yellow", "end" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "[SC]";
                case "red" -> "red";
                default -> null;
            };
            case "blue" -> switchTo = switch (type) {
                case "yellow", "end" -> "yellow";
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "[SC]";
                case "red" -> "red";
                default -> null;
            };
            case "yellow" -> switchTo = switch (type) {
                case "doubleYellow" -> "doubleYellow";
                case "[SC]" -> "[SC]";
                case "red" -> "red";
                case "end" -> "yellow";
                default -> null;
            };
            case "doubleYellow" -> switchTo = switch (type) {
                case "[SC]" -> "[SC]";
                case "red" -> "red";
                case "end" -> "yellow";
                default -> null;
            };
            case "[SC]" -> switchTo = switch (type) {
                case "red" -> "red";
                case "end" -> "yellow";
                default -> null;
            };
            case "red" -> switchTo = switch (type) {
                case "end" -> "yellow";
                default -> null;
            };
            default -> switchTo = null;
        }

        if (switchTo == null) return false;
        else {
            setDepiction(switchTo);
            setLevel(switch (type) {
                case "green", "blue" -> 1;
                case "yellow" -> 2;
                case "doubleYellow", "[SC]" -> 3;
                case "red" -> 4;
                case "end" -> 5;
                default -> 0;
            });
            return true;
        }
    }

    public boolean down(String type) {
        boolean dangerStatus = (Objects.equals(getDepiction(), "yellow")
                || Objects.equals(getDepiction(), "doubleYellow")
                || Objects.equals(getDepiction(), "[SC]")
                || Objects.equals(getDepiction(), "red")) && getLevel() != 5;
        if (Objects.equals(type, "danger") && dangerStatus) {
            setDepiction("green");
            setLevel(1);
            return true;
        }
        else if (Objects.equals(type, "blue") && Objects.equals(getDepiction(), "blue")) {
            setDepiction("");
            setLevel(0);
            return true;
        }
        else if (Objects.equals(type, "green") && Objects.equals(getDepiction(), "green")) {
            setDepiction("");
            setLevel(0);
            return true;
        }
        else if (Objects.equals(type, "clear")) {
            if (getLevel() == 0) return false;
            setDepiction("");
            setLevel(0);
            return true;
        }

        return false;
    };

    public String toString() {
        if (getLevel() == 0)
            return "Signal post " + getPostNumber() + " of type light panel is in level " + getLevel() + " and is switched off";
        else
            return "Signal post " + getPostNumber() + " of type light panel is in level " + getLevel() + " and is blinking " + getDepiction();
    }
}
