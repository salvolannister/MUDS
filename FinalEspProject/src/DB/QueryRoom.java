package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class QueryRoom {
    private Connection conn;
    public QueryRoom(Connection conn) {
        this.conn=conn;
    }

    public boolean addRoom(String Name, float X, float Y) throws SQLException {
        PreparedStatement pstmt;

        try {
            conn.setAutoCommit(false);

            String s=new String("INSERT INTO Room"+ "(Name, X, Y)"+" VALUES (?, ?, ?)");
            try (PreparedStatement preparedStatement = pstmt = conn.prepareStatement(s)) {
                pstmt.setString(1, Name);
                pstmt.setFloat(2, X);
                pstmt.setFloat(3, Y);
                pstmt.executeUpdate();
                conn.commit();
                return true;
            }
            catch (Exception ex){
                ex.printStackTrace();
                return false;
            }

        }catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
            System.out.println("errore");
            return false;
        }

    }

    public boolean checkRoomExistence(String Name) throws SQLException{
        String sql = "SELECT COUNT(*) as total FROM Room WHERE Name = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, Name);

        ResultSet resultSet = preparedStatement.executeQuery();
        if(  resultSet.getInt("total")!= 0 )
            return true;
        else
            return false;
    }

    public List<String> getRoomName() throws SQLException {
        List<String> rooms = new ArrayList<>();
        String sql = "SELECT Name FROM Room ";


        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int i = 0;
        ResultSet resultSet = preparedStatement.executeQuery();


        /* is Before first check if the pointer is before first,
        if it is this means there is data in
        result set
         */

        if (!resultSet.isBeforeFirst() ) {
            System.out.println("No data in Room");
            return null;
        }
        while(resultSet.next()) {
          rooms.add(resultSet.getString(1));

      }
        return rooms;
    }

    

    public ArrayList<Float> getRoomDim(String Name) throws SQLException {

        String sql = "SELECT X,Y FROM Room WHERE Name = ? ";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, Name);
        ResultSet resultSet = preparedStatement.executeQuery();

        /* is Before first check if the pointer is before first,
        if it is this means there is data in
        result set
         */

        if (!resultSet.isBeforeFirst() ) {
            System.out.println("No Room with this name");
            return null;
        }

        Float X = resultSet.getFloat(1);
        Float Y = resultSet.getFloat( 2);
        ArrayList<Float> dim = new ArrayList<>();
        dim.add(X);
        dim.add(Y);
        return  dim;
    }

}
