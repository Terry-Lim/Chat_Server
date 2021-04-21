import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
 // ��� ���� �����ϴ� ������� �����ؾߵȴ�.
 // hashmap map<key(��ѹ�), value(�����)>


public class RoomManager {
	private HashMap<Integer, List <User>> room = new HashMap<>();
	private List <User> room_member = new ArrayList<>();
	
	public String getRoomMember (int roomnumber) {
		return room.get(roomnumber).get(0).getId();
	}

	public void addRoom_member(int key, String id, DataOutputStream dos) {
//		room.get(key).add(id);
		room_member.add(new User(id, dos));
		room.put(key, room_member);
		
	}
	public void removeRoom_member(int key, String id) {
		room_member.remove(id);
	}
	public int getRoomNum(int key) {
		return room.get(key).size();
	}

	public String getDos(int roomNumber, String id) {
		return getUser(id, room.get(roomNumber)).getId();
		
	}
	public User getUser(String id, List<User> room_m) {
		for (int i =0; i < room_m.size(); i++) {
			if (room_m.get(i).getId().equals(id)) {
				return room_m.get(i);
			}
		}
		return null;
	}
	public void getuserid(int roomnum, int num) {
		room.get(roomnum).get(num).getId();
	}
}


class User {
	private String id;
	private DataOutputStream dos;
	private boolean isLeader;
	private int warring;
	public User(String id, DataOutputStream dos) {
		super();
		this.id = id;
		this.dos = dos;
	}
	public String getId() {
		return id;
	}
	public DataOutputStream getDos() {
		return dos;
	}
	public void setId(String id) {
		this.id = id;
	}

}
