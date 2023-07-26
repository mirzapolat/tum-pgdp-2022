package pgdp.messenger;

public class UserArray {
	private User[] users;
	private int size;

	public UserArray(int initCapacity) {
		if (initCapacity >= 1) this.users = new User[initCapacity];
		else this.users = new User[1];
		this.size = 0;
	}

	/** Fügt den übergebenen Nutzer in das durch dieses Objekt dargestellte 'UserArray' ein
	 * @param user Eine beliebige User-Referenz (schließt 'null' mit ein)
	 */
	public void addUser(User user) {
		if (size == users.length) {
			User[] nArray = new User[users.length * 2];
			for (int i = 0; i < users.length; i++) nArray[i] = users[i];
			users = nArray;
		}

		searchPoint:
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null) {
				users[i] = user;
				break searchPoint;
			}
		}

		size++;
	}

	/** Entfernt den Nutzer mit der übergebenen ID aus dem Array und gibt diesen zurück.
	 *  Wenn kein solcher Nutzer existiert, wird 'null' zurückgegeben.
	 * @param id Ein beliebiger long
	 * @return Der eben aus dem UserArray entfernte Nutzer, wenn ein Nutzer mit der übergebenen id darin existiert, sonst 'null'
	 */
	public User deleteUser(long id) {
		for (int i = 0; i < users.length; i++) {
			if (users[i].getId() == id) {
				User ret = users[i];
				users[i] = null;
				size--;
				return ret;
			}
		}

		return null;
	}

	public int size() { return size; }

	public User[] getUsers() { return users; }
	public void setUsers(User[] users) { this.users = users; }
}
