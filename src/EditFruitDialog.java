import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class EditFruitDialog extends JDialog {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField unitField;
    private JTextField imagePathField;
    private JButton chooseImageBtn, saveBtn, cancelBtn;
    private JLabel imagePreviewLabel; // 图片预览
    private boolean isEditMode;
    private Fruit fruit;

    public EditFruitDialog(JFrame parent, Fruit fruit) {
        super(parent, "编辑水果", true);
        this.fruit = fruit;
        this.isEditMode = fruit.getName() != null && !fruit.getName().isEmpty();
        initUI();
        loadFruitData();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(450, 400);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        formPanel.add(new JLabel("水果名称:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("水果单价:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("计量单位:"));
        unitField = new JTextField();
        formPanel.add(unitField);

        formPanel.add(new JLabel("水果图片:"));
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePathField = new JTextField();
        imagePathField.setEditable(false);
        chooseImageBtn = new JButton("选择文件");
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(chooseImageBtn, BorderLayout.EAST);
        formPanel.add(imagePanel);

        add(formPanel, BorderLayout.NORTH);

        // 图片预览区
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 200));
        add(imagePreviewLabel, BorderLayout.CENTER);

        // 按钮区
        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton("保存");
        cancelBtn = new JButton("取消");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // 文件选择按钮事件
        chooseImageBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("图片文件", "jpg", "jpeg", "png"));
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String path = file.getAbsolutePath();
                imagePathField.setText(path);
                showImagePreview(path);
            }
        });

        // 保存按钮事件
        saveBtn.addActionListener(e -> {
            fruit.setName(nameField.getText());
            fruit.setPrice(Double.parseDouble(priceField.getText()));
            fruit.setUnit(unitField.getText());
            fruit.setImagePath(imagePathField.getText());

            if (isEditMode) {
                FruitDao.updateFruit(fruit);
                JOptionPane.showMessageDialog(this, "修改成功！");
            } else {
                FruitDao.insertFruit(fruit);
                JOptionPane.showMessageDialog(this, "添加成功！");
            }

            dispose();
        });

        // 取消按钮事件
        cancelBtn.addActionListener(e -> dispose());
    }

    private void loadFruitData() {
        if (fruit != null) {
            nameField.setText(fruit.getName());
            priceField.setText(String.valueOf(fruit.getPrice()));
            unitField.setText(fruit.getUnit());
            imagePathField.setText(fruit.getImagePath());
            showImagePreview(fruit.getImagePath());
        }
    }

    private void showImagePreview(String path) {
        if (path != null && !path.isEmpty()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(img));
        } else {
            imagePreviewLabel.setIcon(null);
        }
    }
}
