package pgdp.messenger;

public class PinguTalk {
    private static long topicID;
    private static long userID;
    private UserArray members;
    private Topic[] topics;

    public PinguTalk(int startLength, int topicLength) {
        members = new UserArray(startLength);
        if (topicLength >= 1) topics = new Topic[topicLength];
        else topics = new Topic[1];
    }

    public User addMember(String name, User supervisor) {
        User u = new User(userID++, name, supervisor);
        members.addUser(u);
        return u;
    }

    public User deleteMember(long id) {
        return members.deleteUser(id);
    }

    public Topic createNewTopic(String name) {
        Topic t = new Topic(topicID, name);

        for (int i = 0; i < topics.length; i++) {
            if (topics[i] == null) {
                topics[i] = t;
                topicID++;
                return t;
            }
        }

        return null;
    }

    public Topic deleteTopic(long id) {
        for (int i = 0; i < topics.length; i++) {
            if (topics[i] != null) {
                if (topics[i].getId() == id) {
                    Topic t = topics[i];
                    topics[i] = null;
                    return t;
                }
            }
        }
        return null;
    }

    public UserArray getMembers() { return members; }
    public void setMembers(UserArray members) { this.members = members; }

    public Topic[] getTopics() { return topics; }
    public void setTopics(Topic[] topics) {this.topics = topics; }
}
