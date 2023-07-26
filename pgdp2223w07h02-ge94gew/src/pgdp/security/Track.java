package pgdp.security;

public class Track {

    private SignalPost[] posts;

    public Track(int amountOfPosts) {
        if (amountOfPosts <= 0) amountOfPosts = 10;
        posts = new SignalPost[amountOfPosts];

        posts[0] = new LightPanel(0);
        if (amountOfPosts == 1) posts[0] = new FinishPost(0);
        for(int i = 1; i < amountOfPosts; i++) {
            if (i == amountOfPosts -1) posts[i] = new FinishPost(i);
            else if (i % 3 == 0) posts[i] = new LightPanel(i);
            else posts[i] = new FlagPost(i);
        }
    }

    public void setAll(String type, boolean up) {
        setRange(type, up, 0, posts.length-1);
    }

    public void setRange(String type, boolean up, int start, int end) {
        if (start >= posts.length || start < 0 || end >= posts.length || end < 0) return;
        if (start > end) {
            setRange(type, up, start, posts.length -1);
            setRange(type, up, 0, end);
            return;
        }

        for (int i = start; i <= end; i++) {
            if (up) posts[i].up(type);
            else posts[i].down(type);
        }
    }

    public void createHazardAt(int start, int end) {
        setRange("yellow", true, start, end-1);
        posts[end].up("green");
    }

    public void removeHazardAt(int start, int end) {
        setRange("danger", false, start, end);
    }

    public void createLappedCarAt(int post) {
        if (posts.length <= 4) setAll("blue", true);
        else setRange("blue", true, post, (post+3) >= posts.length ? (post+3)-posts.length : (post+3));
    }

    public void removeLappedCarAt(int post) {
        if (posts.length <= 4) setAll("blue", false);
        else setRange("blue", false, post, (post+3) >= posts.length ? (post+3)-posts.length : (post+3));
    }

    public void printStatus() {
        for (int i = 0; i < posts.length; i++) {
            System.out.print(posts[i].toString() + "\n");
        }
        System.out.print("\n");
    }

    // ------- Getter und Setter

    public void setPosts(SignalPost[] posts) { this.posts = posts; }

    public SignalPost[] getPosts() { return posts; }
}
