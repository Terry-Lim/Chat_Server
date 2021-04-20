import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
 // ��� ���� �����ϴ� ������� �����ؾߵȴ�.
 // hashmap map<key(��ѹ�), value(�����)>

public class RoomManager {
	private HashMap<String, List> room = new HashMap<>();
	private List <String> room_member = new ArrayList<>();
	
	public void updateRoom() {
		
	}
	
	public HashMap<String, List> getRoom() {
		return room;
	}

	public void setRoom(HashMap<String, List> room) {
		this.room = room;
	}

	public List getRoom_member() {
		return room_member;
	}

	public void setRoom_member(List room_member) {
		this.room_member = room_member;
	}

	
	public void addRoom_member(String roomname, String id) {
//		room.get(key).add(id);
		room_member.add(id);
		room.put(roomname, room_member);
	}
	public void removeRoom_member(String roomname, String id) {
		room_member.remove(roomname);
	}
	public int getRoomNum(String roomname) {
		return room.get(roomname).size();
	}
	
}






//public class RoomManager {
//    private static List roomList; // ���� ����Ʈ
//    private static AtomicInteger atomicInteger;
//    
//    static {
//        roomList = new ArrayList();
//        atomicInteger = new AtomicInteger();
//    }
//
//    public RoomManager() {
//
//    }
//    public static TalkRoom createRoom() { // ���� ���� ����(�� ��)
//        TalkRoom room = new TalkRoom(roomId);
//        roomList.add(room);
//        System.out.println("Room Created!");
//        return room;
//    }
//
//    /**
//     * ���� �����԰� ���ÿ� ������ �������
//     * @param owner ����
//     * @return GameRoom
//     */
//    public static TalkRoom createRoom(TalkRoom owner) { // ������ ���� ������ �� ���(������ �������� ��)
//
//        TalkRoom room = new TalkRoom();
//        room.enterUser(owner);
//        room.setOwner(owner);
//
//        roomList.add(room);
//        System.out.println("Room Created!");
//        return room;
//    }
//
//    /**
//     * ���޹��� ���� ����
//     * @param room ������ ��
//     */
//    public static void removeRoom(TalkRoom room) {
//        room.close();
//        roomList.remove(room); // ���޹��� ���� �����Ѵ�.
//        System.out.println("Room Deleted!");
//    }
//
//    /**
//     * ���� ���� ũ�⸦ ����
//     * @return ���� size
//     */
//    public static int roomCount() {
//        return roomList.size();
//    }
//}
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TalkRoom {
//
//    private List userList;
//    private User roomOwner; // ����
//    private String roomName; // �� �̸�
//
//
//    public TalkRoom(int roomSize, User user) { // �游���
//        userList = new ArrayList(roomSize);
//        user.enterRoom(this);
//        userList.add(user); // ������ �߰���Ų ��
//        this.roomOwner = user; // ������ ������ �����.
//        this.roomName = null;
//    }
//    
//    public TalkRoom(int roomSize, String roomTitle, User user) {
//    	userList = new ArrayList(roomSize);
//    	user.enterRoom(this);
//        userList.add(user); // ������ �߰���Ų ��
//        this.roomOwner = user; // ������ ������ �����.
//        this.roomName = roomTitle;
//    	
//    }
//
//    public void enterUser(User user) {
//        user.enterRoom(this);
//        userList.add(user);
//    }
//
//    // ����
//    public void exitUser(User user) {
//        user.exitRoom(this);
//        userList.remove(user); // �ش� ������ �濡�� ������
//    }
//    
//    // ���� ���� �絵
//    public void setOwner(User user) {
//        this.roomOwner = user; // Ư�� ����ڸ� �������� �����Ѵ�.
//    }
//    
//    public void close() {
//		if (userList.size() <= 1) { // ��� �ο��� �� ���� �����ٸ�
//			RoomManager.removeRoom(this); // �� ���� �����Ѵ�.
//	        return;
//	    }
//	}
//    // �ο�������
//    public void changePeople(int num, List list) {
//    	for (int i = 0; i >= list.size(); i++) {
//    		userList = new ArrayList<>(num);
//    		userList.add(list.get(i));
//    	}
//    	
//    }
//
//    public void setRoomName(String name) { // �� �̸��� ����
//        this.roomName = name;
//    }
//
//    public String getRoomName() { // �� �̸��� ������
//        return roomName;
//    }
//
//    public int getUserSize() { // ������ ���� ����
//        return userList.size();
//    }
//
//    public User getOwner() { // ������ ����
//        return roomOwner;
//    }
//
//    public List getUserList() {
//        return userList;
//    }
//
//    public void setUserList(List userList) {
//        this.userList = userList;
//    }
//
//    public User getRoomOwner() {
//        return roomOwner;
//    }
//
//    public void setRoomOwner(User roomOwner) {
//        this.roomOwner = roomOwner;
//    }
//
//    
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((roomName == null) ? 0 : roomName.hashCode());
//		result = prime * result + ((roomOwner == null) ? 0 : roomOwner.hashCode());
//		result = prime * result + ((userList == null) ? 0 : userList.hashCode());
//		return result;
//	}
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		TalkRoom other = (TalkRoom) obj;
//		if (roomName == null) {
//			if (other.roomName != null)
//				return false;
//		} else if (!roomName.equals(other.roomName))
//			return false;
//		if (roomOwner == null) {
//			if (other.roomOwner != null)
//				return false;
//		} else if (!roomOwner.equals(other.roomOwner))
//			return false;
//		if (userList == null) {
//			if (other.userList != null)
//				return false;
//		} else if (!userList.equals(other.userList))
//			return false;
//		return true;
//	}
//}