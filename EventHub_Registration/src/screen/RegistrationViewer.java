package screen;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class RegistrationViewer extends JFrame {

    private JTable registrationTable;

    public RegistrationViewer() {
        setTitle("EventHub - View Registrations");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 700, 500, 30, 30));
        setLayout(new BorderLayout());

        // Main Panel with Gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(63, 81, 181);
                Color color2 = new Color(101, 31, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            new MainFrame();
            dispose();
        });

        JLabel titleLabel = new JLabel("View Registrations", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create table model
        RegistrationTableModel tableModel = new RegistrationTableModel();
        registrationTable = new JTable(tableModel);
        registrationTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registrationTable.setRowHeight(30);
        registrationTable.setBackground(Color.WHITE);
        registrationTable.setForeground(Color.BLACK);
        registrationTable.setGridColor(new Color(200, 200, 200));

        // Style table header
        registrationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        registrationTable.getTableHeader().setBackground(new Color(63, 81, 181));
        registrationTable.getTableHeader().setForeground(Color.WHITE);

        // Set up cancel button column
        registrationTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        registrationTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), tableModel));

        JScrollPane scrollPane = new JScrollPane(registrationTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Table model for registrations
    class RegistrationTableModel extends AbstractTableModel {
        private String[] columnNames = {"Event Name", "Category", "Semester", "Action"};
        private java.util.ArrayList<Registration> data = StudentRegistration.registrations;

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            Registration reg = data.get(row);
            switch (col) {
                case 0: return reg.getEventName();
                case 1: return reg.getCategory();
                case 2: return reg.getSemester();
                case 3: return "Cancel";
                default: return null;
            }
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 3; // Only the Cancel button column is editable
        }

        public void removeRow(int row) {
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    // Renderer for button column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.BLACK);
            setBackground(new Color(255, 193, 7));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor for button column
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private RegistrationTableModel model;
        private int row;

        public ButtonEditor(JCheckBox checkBox, RegistrationTableModel model) {
            super(checkBox);
            this.model = model;
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setForeground(Color.BLACK);
            button.setBackground(new Color(255, 193, 7));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int confirm = JOptionPane.showConfirmDialog(RegistrationViewer.this,
                    "Are you sure you want to cancel this registration?", "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(RegistrationViewer.this, "Registration cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    private JButton createActionButton(String text, Color bgColor, Runnable action) {
        JButton button = new JButton(text) {
            private Color normalColor = bgColor;
            private Color hoverColor = new Color(
                Math.min(bgColor.getRed() + 30, 255),
                Math.min(bgColor.getGreen() + 30, 255),
                Math.min(bgColor.getBlue() + 30, 255)
            );

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hoverColor : normalColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(0, 45));
        button.addActionListener(e -> action.run());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });

        return button;
    }
}