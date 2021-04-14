import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
 // 방과 방을 포함하는 사람들을 저장해야된다.
 // hashmap map<key(방넘버), value(사람들)>

public class RoomManager {
	private HashMap<Integer, List> room = new HashMap<>();
	private List <String> room_member = new ArrayList<>();
	
	public void updateRoom() {
		
	}
	
	public HashMap<Integer, List> getRoom() {
		return room;
	}

	public void setRoom(HashMap<Integer, List> room) {
		this.room = room;
	}

	public List getRoom_member() {
		return room_member;
	}

	public void setRoom_member(List room_member) {
		this.room_member = room_member;
	}

	
	public void addRoom_member(int key, String id) {
//		room.get(key).add(id);
		room_member.add(id);
		room.put(key, room_member);
	}
	public void removeRoom_member(int key, String id) {
		room_member.remove(id);
	}
	public int getRoomNum(int key) {
		return room.get(key).size();
	}
	
}






//public class RoomManager {
//    private static List roomList; // 방의 리스트
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
//    public static TalkRoom createRoom() { // 룸을 새로 생성(빈 방)
//        TalkRoom room = new TalkRoom(roomId);
//        roomList.add(room);
//        System.out.println("Room Created!");
//        return room;
//    }
//
//    /**
//     * 방을 생성함과 동시에 방장을 만들어줌
//     * @param owner 방장
//     * @return GameRoom
//     */
//    public static TalkRoom createRoom(TalkRoom owner) { // 유저가 방을 생성할 때 사용(유저가 방장으로 들어감)
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
//     * 전달받은 룸을 제거
//     * @param room 제거할 룸
//     */
//    public static void removeRoom(TalkRoom room) {
//        room.close();
//        roomList.remove(room); // 전달받은 룸을 제거한다.
//        System.out.println("Room Deleted!");
//    }
//
//    /**
//     * 방의 현재 크기를 리턴
//     * @return 현재 size
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
//    private User roomOwner; // 방장
//    private String roomName; // 방 이름
//
//
//    public TalkRoom(int roomSize, User user) { // 방만들기
//        userList = new ArrayList(roomSize);
//        user.enterRoom(this);
//        userList.add(user); // 유저를 추가시킨 후
//        this.roomOwner = user; // 방장을 유저로 만든다.
//        this.roomName = null;
//    }
//    
//    public TalkRoom(int roomSize, String roomTitle, User user) {
//    	userList = new ArrayList(roomSize);
//    	user.enterRoom(this);
//        userList.add(user); // 유저를 추가시킨 후
//        this.roomOwner = user; // 방장을 유저로 만든다.
//        this.roomName = roomTitle;
//    	
//    }
//
//    public void enterUser(User user) {
//        user.enterRoom(this);
//        userList.add(user);
//    }
//
//    // 강퇴
//    public void exitUser(User user) {
//        user.exitRoom(this);
//        userList.remove(user); // 해당 유저를 방에서 내보냄
//    }
//    
//    // 방장 권한 양도
//    public void setOwner(User user) {
//        this.roomOwner = user; // 특정 사용자를 방장으로 변경한다.
//    }
//    
//    public void close() {
//		if (userList.size() <= 1) { // 모든 인원이 다 방을 나갔다면
//			RoomManager.removeRoom(this); // 이 방을 제거한다.
//	        return;
//	    }
//	}
//    // 인원수변경
//    public void changePeople(int num, List list) {
//    	for (int i = 0; i >= list.size(); i++) {
//    		userList = new ArrayList<>(num);
//    		userList.add(list.get(i));
//    	}
//    	
//    }
//
//    public void setRoomName(String name) { // 방 이름을 설정
//        this.roomName = name;
//    }
//
//    public String getRoomName() { // 방 이름을 가져옴
//        return roomName;
//    }
//
//    public int getUserSize() { // 유저의 수를 리턴
//        return userList.size();
//    }
//
//    public User getOwner() { // 방장을 리턴
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