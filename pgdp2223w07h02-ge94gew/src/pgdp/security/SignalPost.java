package pgdp.security;

public abstract class SignalPost {

	/**
	 * Diese Klasse ist nur da, damit keine Buildfails entstehen. Allerdings ist sie
	 * bei Weitem noch nicht vollständig.
	 * 
	 */
	private int postNumber;
	private String depiction;
	private int level;

	public SignalPost(int postNumber) {
		this.postNumber = postNumber;
		depiction = "";
		level = 0;
	}

	public abstract boolean up(String type);

	public abstract boolean down(String type);

	public String toString() { return "Signal Post " + postNumber + ": " + level + " " + depiction; }

	// GETTER UND SETTER
	public int getPostNumber() { return postNumber; }
	public String getDepiction() { return depiction; }
	public int getLevel() { return level; }

	public void setPostNumber(int postNumber) { this.postNumber = postNumber; };
	public void setDepiction(String depiction) {this.depiction = depiction; }
	public void setLevel(int level) {this.level = level; }
}
