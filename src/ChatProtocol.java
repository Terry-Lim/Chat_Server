import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.attribute.standard.PresentationDirection;

public class ChatProtocol {
	DataOutputStream dos;
	DataInputStream dis; 
	ObjectOutputStream oos;
	ObjectInputStream ois;
	DAO_Client dao_client ;
	DAO_Room dao_room;
	RoomManager rm = new RoomManager();
	PrintWriter pw;
	BufferedReader br;
	ChatProtocol(DataOutputStream dos, DataInputStream dis, 
			PrintWriter pw, BufferedReader br,
			RoomManager rm) 
	{
		this.dos = dos;
		this.dis = dis;
		this.pw = pw;
		this.br = br;
		this.rm = rm;
		dao_client = new DAO_Client();
		dao_room = new DAO_Room();
	
	}
	
	
	
	
	
	public void process(int fromClient) {
		if (fromClient == Command.LOGIN) {
			login();
		} else if (fromClient == Command.SIGNUP_IDCHECK) {
			idFind_idcheck();		
		} else if (fromClient == Command.SIGNUP_OK) {
			signup_ok();
		} else if (fromClient == Command.CLIENT_DELETE) {
			client_delete();
		} else if (fromClient == Command.CLIENT_UPDATE) {
			client_update();
		} else if (fromClient ==Command.PROFILE_UPDATE) {
			profile_update();
		} else if  (fromClient == Command.PROFILE_lOAD) {
			profile_load();
		} else if (fromClient == Command.IDFIND_NAMECHECK) {
			idfind_namecheck();
		} else if (fromClient == Command.IDFIND_TELLCHECK) {
			idfind_tellcheck();
		} else if (fromClient == Command.IDFIND_IDFIND) {
			idfind_idfind();
		} else if (fromClient == Command.PWFIND_IDCHECK) {
			pwfind_idcheck();
		} else if (fromClient == Command.PWFIND_NAMECHECK) {
			pwfind_namecheck();
		} else if (fromClient == Command.PWFIND_TELLCHECK) {
			pwfind_tellcheck();
		} else if (fromClient == Command.PWFIND_PWFIND) {
			pwfind_pwfind();
		} else if (fromClient == Command.ROOMUPDATE) {
			chatroom_update(); // 다른 사람 입장시 room update를한다.
		} else if (fromClient == Command.ENTERROOM) {
			enterroom(); // 입장시 본인이 들어온 만큼 데이터베이스를 바꾸고 그방에 있는 모든 사람들에게 room_update를 시킨다. 
		} else if (fromClient == Command.EXITROOM) {
			exitroom(); // 퇴장시 본인이 들어온 만큼 데이터베이스를 바꾸고 그방에 있는 모든 사람들에게 room_update를 시킨다.
		} else if (fromClient == Command.RESETROOM) {
			resetroom();
		} else if (fromClient == Command.GETSELETEDROOM) {
			getselectedroom();
		} else if  (fromClient == Command.PROFILEVIEW) {
			getprofile();
		} else if (fromClient == Command.FINDROOM) {
			findroom();
		} else if (fromClient == Command.MAKEROOM) {
			makeroom();
		}
		
	}
	
	private void makeroom() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String id = dis.readUTF();
			String roomname = dis.readUTF();
			int num = dis.readInt();
			rm.makeRoom(roomname, id, pw);
			conn = dao_room.getConn();
			stmt = conn.createStatement();
			String sql1 = "SELECT COUNT(*) FROM room where roomname = '" + roomname + "';";
			rs = stmt.executeQuery(sql1);
			int isOk = 0;
			while (rs.next()) {
				isOk = rs.getInt(1);
			}
			if (isOk == 0) {
				dos.writeInt(0);
				String sql2 = "INSERT INTO room (roomname, maximum, currentnum) value ('"+ roomname+"', "+ num +", " + 1 +");";
				stmt.executeUpdate(sql2);
			} else {
				dos.writeInt(1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void findroom() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dao_room.getConn();
			String name = dis.readUTF();
			String sql1 = "SELECT COUNT(*) from room where roomname = '" + name +"';";
			pstmt = conn.prepareStatement(sql1);
			rs = pstmt.executeQuery();
			int count = 0;
			
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count > 0 ) {
				 dos.writeInt(1);
				 String sql2 = "SELECT roomname, maximum, currentnum from room where roomname = '" + name +"';";
					pstmt = conn.prepareStatement(sql2);
					rs = pstmt.executeQuery();
					String roomname =null;
					int max = 0;
					int num = 0;
					
					while (rs.next()) {
						roomname = rs.getString("roomname");
						max = rs.getInt("maximum");
						num = rs.getInt("currentnum");
					}
					dos.writeUTF(roomname);
					dos.writeInt(max);
					dos.writeInt(num);
			} else {
				dos.writeInt(0);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getprofile() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			String getid = dis.readUTF();
			conn = dao_client.getConn();
			stmt = conn.createStatement();
			String sql = "SELECT id, name, tell, birthdate, status FROM client WHERE id =" + getid;
			rs = stmt.executeQuery(sql);
			String id = null;
			String name = null;
			String tell = null;
			String birthdate = null;
			String status = null;
			
			
			id = rs.getString("id");
			name = rs.getString("name");
			tell = rs.getString("tell");
			birthdate = rs.getString("birthdate");
			status = rs.getString("status");
			
			dos.writeUTF(id);
			dos.writeUTF(name);
			dos.writeUTF(tell);
			dos.writeUTF(birthdate);
			dos.writeUTF(status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void getselectedroom() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			int roomnumber = dis.readInt();
			conn = dao_room.getConn();
			stmt = conn.createStatement();
			String sql = "SELECT roomname, maximum, currentnum from room where roomnumber = " + roomnumber;
			rs = stmt.executeQuery(sql);
			String roomname = null;
			int max = 0;
			int num = 0;
			
			while (rs.next()) {
				roomname = rs.getString("roomname");
				max = rs.getInt("maximum");
				num = rs.getInt("currentnum");
			}
			dos.writeUTF(roomname);
			dos.writeInt(max);
			dos.writeInt(num);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void resetroom() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dao_client.getConn();
			stmt = conn.createStatement();
			String sql = "SELECT COUNT(*) FROM ROOM;";
			rs = stmt.executeQuery(sql);
			int roomnum = 0;
			while (rs.next()) {
				roomnum = rs.getInt(1);
			}
			dos.writeInt(roomnum);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void chatroom_update() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String roomname = dis.readUTF();
			conn = dao_room.getConn();
			String sql = "SELECT  maximum, currentnum FROM room WHERE roomname = ?;";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, roomname);
			rs = pstmt.executeQuery();
			
			int num = 0;
			int maxnum = 0;
			while (rs.next()) { // 방이름 방장이름  참여자 인원수 최대인원수
			
				num = rs.getInt("currentnum");
				maxnum = rs.getInt("maximum");
			}
			dos.writeUTF(rm.getLeader(roomname));
			dos.writeInt(num);
			dos.writeInt(maxnum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void enterroom() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			String roomname = dis.readUTF();
			String id = dis.readUTF();
			rm.addRoom_member(roomname, id, pw);
			
			int currentNum = rm.getRoomNum(roomname);
			conn = dao_room.getConn();
			String sql = "UPDATE room SET currentnum = ? WHERE roomname = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentNum);
			pstmt.setString(2,roomname);
			pstmt.executeUpdate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		finally {
//			try {
//				rs.close();
//				pstmt.close();
//				conn.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	private void exitroom() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			String roomname = dis.readUTF();
			String id = dis.readUTF();
			rm.removeRoom_member(roomname, id);
			
			int currentNum = rm.getRoomNum(roomname);
			conn = dao_room.getConn();
			String sql = "UPDATE room SET currentnum = ? WHERE roomname = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentNum);
			pstmt.setString(2,roomname);
			pstmt.executeUpdate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	private void pwfind_pwfind() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String id = dis.readUTF();
			String name = dis.readUTF();
			String tell = dis.readUTF();
			String pw = null;
			conn = dao_client.getConn();
			String sql = "SELECT pw FROM CLIENT WHERE ID = ? AND NAME = ? AND tell = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, tell);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				pw = rs.getString(1);
			}
			dos.writeUTF(pw);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void pwfind_tellcheck() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String id = dis.readUTF();
			String name = dis.readUTF();
			String tell = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT COUNT(*) FROM CLIENT WHERE id = ? AND name = ? AND TELL = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, tell);
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);
			}
			dos.writeInt(x);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void pwfind_namecheck() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String id = dis.readUTF();
			String name = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT COUNT(*) FROM CLIENT WHERE id = ? AND name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);
			}
			dos.writeInt(x);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void pwfind_idcheck() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String id = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT COUNT(*) FROM CLIENT WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);
			}
			dos.writeInt(x);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void idfind_idfind() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String id = null;
		try {
			String name = dis.readUTF();
			String tell = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT id FROM CLIENT WHERE NAME = ? AND tell = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, tell);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getString(1);
			}
			dos.writeUTF(id);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private void idfind_tellcheck() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String name = dis.readUTF();
			String tell = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT COUNT(*) FROM CLIENT WHERE NAME = ? AND tell = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, tell);
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);
			}
			dos.writeInt(x);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private void idfind_namecheck() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String name = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT COUNT(*) FROM CLIENT WHERE name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);
			}
			dos.writeInt(x);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void profile_update() {
		String id = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			id = dis.readUTF();
			String pw = dis.readUTF();
			String name = dis.readUTF();
			String birth = dis.readUTF();
			String tell = dis.readUTF();
			String status = dis.readUTF();
			String icon = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "UPDATE client set pw = ?, name = ?,tell = ?,"
					+ " birthdate = ?, status = ?, icon = ? WHERE id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pw);
			pstmt.setString(2, name);
			pstmt.setString(3, tell);
			pstmt.setString(4, birth);
			pstmt.setString(5, status);
			pstmt.setString(6, icon);
			pstmt.setString(7, id);
			int x = pstmt.executeUpdate();
			dos.writeInt(x);
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private void profile_load() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM Client WHERE id = ?";
		String id = null;
		try {
			conn = dao_client.getConn();
			id = dis.readUTF();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			String name ="";
			String birth ="";
			String tell ="";
			String icon = "";
			String status = "";
			while (rs.next()) {
				name = rs.getString("name");
				birth = rs.getString("birthdate");
				tell = rs.getString("tell");
				status = rs.getString("status");
				icon = rs.getString("icon"); //아이콘
			}
			dos.writeUTF(name);
			dos.writeUTF(birth);
			dos.writeUTF(tell);
			if (status != null) {
				if (icon != null) {
					dos.writeInt(1);
					dos.writeUTF(status);
					dos.writeUTF(icon);
				} else {
					dos.writeInt(2);
					dos.writeUTF(status);
				}
			} else {
				if (icon != null) {
					dos.writeInt(3);
					dos.writeUTF(icon);
				} else {
					dos.writeInt(4);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void client_update() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			String id = dis.readUTF();
			String pw = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT COUNT(*) FROM CLIENT WHERE id = ? AND pw = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);		
			} 
			if (x == 1) {
				dos.writeInt(1);
			} else {
				dos.writeInt(2);
			}
		} catch (SQLException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	private void client_delete() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			String id = dis.readUTF();
			String pw = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT COUNT(*) FROM CLIENT WHERE id = ? AND pw = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);		
			} 
			if (x == 1) {
				dos.writeInt(1); //1
				int y = dis.readInt();
				if (y == 1) { //2
					try (Statement stmt = conn.createStatement();) {
						String sql1 = "DELETE FROM Client WHERE id = '"+ id + "';";
						stmt.executeUpdate(sql1);
					}
				} 
			} else {
				dos.writeInt(2);
			}
		} catch (SQLException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	private void signup_ok() {
		try {
			String id = dis.readUTF();
			String pw = dis.readUTF();
			String name  = dis.readUTF();
			String birth = dis.readUTF();
			String tell = dis.readUTF();
			tell += dis.readUTF();
			tell += dis.readUTF();

			try (Connection conn = dao_client.getConn();) {
				String sql = "INSERT INTO CLIENT (id, pw, name, birthdate, tell) VALUE (?, ? ,?, ? ,?)";
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setString(1, id);
				pstm.setString(2, pw);
				pstm.setString(3, name);
				pstm.setString(4, birth);
				pstm.setString(5, tell);
				int x = pstm.executeUpdate();
				System.out.println(x);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void idFind_idcheck() {
		String id = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		try {
			id = dis.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		conn = dao_client.getConn();
		String sql = "SELECT COUNT(*) FROM CLIENT WHERE id = ?;";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);
			}
			if (x == 1) { 
				dos.writeInt(2);
			} else if(x == 0) {
				dos.writeInt(1);
			}
		} catch (SQLException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}
	 public void login() {
		String id = null;
		ResultSet rs2 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			id = dis.readUTF();
			String pw = dis.readUTF();
			conn = dao_client.getConn();
			String sql = "SELECT COUNT(*) FROM CLIENT WHERE id = ? AND pw = ?;";
			pstmt = conn.prepareStatement(sql); //statement
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			 
			rs = pstmt.executeQuery();
			int x = 0;
			while (rs.next()) {
				x = rs.getInt(1);		
			} 
			if (x == 1) {
				dos.writeInt(1);
			} else {
				String sql2 = "SELECT COUNT(*) FROM CLIENT WHERE id = ?;";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, id);
				rs2 = pstmt2.executeQuery();
				int y = 0;
				while (rs2.next()) {
					y = rs2.getInt(1);
				}
				if (y == 1) {
					dos.writeInt(2);
				} else {
					dos.writeInt(3);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
//				rs2.close();
//				pstmt2.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	 }
}
