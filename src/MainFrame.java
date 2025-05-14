import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.io.File;
import javax.swing.ImageIcon;

public class MainFrame extends JFrame {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    public MainFrame() {
        setTitle("水果超市管理系统 v0.8");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        loadFruits(""); // 初始加载全部数据
    }

    private void initComponents() {
        JPanel topPanel = new JPanel();
        JLabel searchLabel = new JLabel("菜品名称:");
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("查询");
        JButton addBtn = new JButton("添加");
        JButton editBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(addBtn);
        topPanel.add(editBtn);
        topPanel.add(deleteBtn);

        // 表格标题
        String[] columns = {"序号", "水果名称", "水果价格", "计量单位", "封面"};
        model = new DefaultTableModel(null, columns);
        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false; // 禁止编辑表格单元格
            }
        };
        table.setRowHeight(80); // 增加行高以显示图片

        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮事件
        searchBtn.addActionListener(e -> loadFruits(searchField.getText().trim()));
        addBtn.addActionListener(e -> {
            Fruit newFruit = new Fruit("", 0.0, "", "");
            new EditFruitDialog(this, newFruit);
            reloadData(); // 添加后刷新
        });
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请选择要修改的水果！");
                return;
            }
            String name = (String) model.getValueAt(row, 1);
            try (Connection conn = DBUtil.getConnection()) {
                Fruit fruit = FruitDao.findFruitByName(conn, name);
                if (fruit != null) {
                    new EditFruitDialog(this, fruit);
                    reloadData(); // 刷新数据
                } else {
                    JOptionPane.showMessageDialog(this, "未找到水果：" + name);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "查询水果信息失败：" + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> deleteFruit());
        //
        table.setRowHeight(80); // 行高足够显示图片
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // 图片列设置合适宽度
        // 设置封面列的渲染器，使其显示图片而不是 ImageIcon 的 toString
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    return new JLabel((ImageIcon) value);
                } else {
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });
    }

    private void loadFruits(String keyword) {
        try (Connection conn = DBUtil.getConnection()) {
            model.setRowCount(0); // 清空原有数据
            String sql = "SELECT * FROM fruits WHERE name LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            int index = 1;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(index++);
                row.add(rs.getString("name"));
                row.add(rs.getDouble("price"));
                row.add(rs.getString("unit"));

                // 图片处理
                String imagePath = rs.getString("image_path");
                ImageIcon icon = null;
                if (imagePath != null && !imagePath.isEmpty()) {
                    File file = new File(imagePath);
                    if (file.exists()) {
                        ImageIcon rawIcon = new ImageIcon(imagePath);
                        Image scaled = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(scaled);
                    } else {
                        icon = new ImageIcon(); // 空图标避免null
                    }
                } else {
                    icon = new ImageIcon(); // 空图标
                }
                row.add(icon);

                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage());
        }
    }


/*
    private void loadFruits(String keyword) {
        try (Connection conn = DBUtil.getConnection()) {
            model.setRowCount(0); // 清空原有数据
            String sql = "SELECT * FROM fruits WHERE name LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            int index = 1;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(index++);
                row.add(rs.getString("name"));
                row.add(rs.getDouble("price"));
                row.add(rs.getString("unit"));
                String imagePath = rs.getString("image_path");
                row.add(new ImageIcon(imagePath)); // 使用 ImageIcon 显示图片
                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询失败：" + e.getMessage());
        }
    }

 */

    private void editFruit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请选择要修改的水果！");
            return;
        }

        String name = (String) model.getValueAt(row, 1); // 获取选中的水果名

        // 从数据库查询完整的水果对象
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM fruits WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // 构建 Fruit 对象
                Fruit fruit = new Fruit(
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getString("image_path")
                );

                new EditFruitDialog(this, fruit); // 传入完整的 Fruit 对象
            } else {
                JOptionPane.showMessageDialog(this, "未找到该水果的详细信息！");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询水果信息失败：" + e.getMessage());
        }
    }

    private void deleteFruit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "请选择要删除的水果！");
            return;
        }
        String name = (String) model.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "确定删除：" + name + "？", "确认", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBUtil.getConnection()) {
                String sql = "DELETE FROM fruits WHERE name = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "删除成功！");
                loadFruits(""); // 重新加载数据
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败：" + e.getMessage());
            }
        }
    }

    public void reloadData() {
        loadFruits(""); // 用于子窗口刷新主表格
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
*/
}
