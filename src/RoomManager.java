import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
 // ��� ���� �����ϴ� ������� �����ؾߵȴ�.
 // hashmap map<key(��ѹ�), value(�����)>

import com.sun.corba.se.pept.transport.Connection;

public class RoomManager {
	private HashMap<String, List<User>> room = new HashMap<>();
	private DAO_Room dao;
	
	public RoomManager() {
		ResultSet rs = null;
		Statement stmt = null;
		java.sql.Connection conn ;
		dao = new DAO_Room();
		String sql = "SELECT roomname FROM room ORDER BY roomnumber;";
		try {
			conn = dao.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				room.put(rs.getString(1), new ArrayList<User>());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public List<User> getList(String roomname) {
		return room.get(roomname);
	}
	
	public void addRoom_member(String roomname, String id, PrintWriter pw) {
		room.get(roomname).add(new User(id, pw));
	}
	
	public void makeRoom(String rn, String id, PrintWriter pw) {
		List<User> list = new ArrayList<User>();
		list.add(new User(id, pw));
		room.put(rn, list);
		getUser(rn, id).setLeader();
	}
	public void removeRoom_member(String roomname, String id) {
		for (int i = 0; i < room.get(roomname).size(); i++) {
			if (room.get(roomname).get(i).getId().equals(id)) {
				room.get(roomname).remove(id);
			}
		}
	}
	public int getRoomNum(String roomname) {
		return room.get(roomname).size();
	}
	public void say(String roomname, String contents) {
		for (int i = 0; i < room.get(roomname).size(); i++) {
			room.get(roomname).get(i).getPw().println(contents);
		}
	}
	public String getLeader(String roomname) {
		for (int i = 0; i < room.get(roomname).size(); i++) {
			if (room.get(roomname).get(i).getIsLeader()) {
				return room.get(roomname).get(i).getId();
			}
		}
		return "방장없음";
	}
	public User getUser(String roomname, String id) {
		for (int i = 0; i < room.get(roomname).size(); i++) {
			if (room.get(roomname).get(i).getId().equals(id)) {
				return room.get(roomname).get(i);
			}
		}
		return null;
	}
	
	public User getUserByIndex (String roomname, int index) {
		return room.get(roomname).get(index);
	}
}

class User {
	private String id;
	private PrintWriter pw;
	private boolean isLeader;
	
	User (String id, PrintWriter pw) {
		this.id = id;
		this.pw = pw;
	}
	public String getId() {
		return id;
	}
	public PrintWriter getPw() {
		return pw;
	}
	public void setLeader() {
		isLeader = true;
	}
	public boolean getIsLeader() {
		return isLeader;
	}
}



