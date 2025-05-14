import java.sql.*;

public class FruitDao {
    public static void insertFruit(Fruit fruit) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO fruits (name, price, unit, image_path) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fruit.getName());
            ps.setDouble(2, fruit.getPrice());
            ps.setString(3, fruit.getUnit());
            ps.setString(4, fruit.getImagePath());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Fruit findFruitByName(Connection conn, String name) throws SQLException {
        String sql = "SELECT * FROM fruits WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Fruit(
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getString("image_path")
                );
            }
        }
        return null;
    }

    public static void updateFruit(Fruit fruit) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "UPDATE fruits SET price=?, unit=?, image_path=? WHERE name=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, fruit.getPrice());
            ps.setString(2, fruit.getUnit());
            ps.setString(3, fruit.getImagePath());
            ps.setString(4, fruit.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
