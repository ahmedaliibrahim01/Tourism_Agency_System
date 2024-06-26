package view;

import business.UserManager;
import core.Helper;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * UserManagementGUI provides an interface for managing users.
 * This class presents an interface for viewing, adding, updating, and deleting user information.
 */
public class UserManagementGUI extends Layout {
    private JPanel container;
    private JTabbedPane tabbedPane_table_user;
    private JButton btn_update;
    private JButton btn_delete;
    private JButton btn_add;
    private JLabel lbl_admin_panel;
    private JComboBox<String> cmbx_user_filter;
    private JLabel lbl_welcome;
    private JTable tbl_users;
    private JButton btn_logout;
    private JPanel pnl_admin;
    private JPanel pnl_welcome;
    private JPanel pnl_table_user;
    private JScrollPane scrl_table;
    private JLabel lbl_filter;
    private JTextField txtf_selected_id;
    private JPanel pnl_selected;
    private JPopupMenu user_menu;
    private Object[] col_model;
    private User user;
    private DefaultTableModel tmdl_users = new DefaultTableModel();
    private UserManager userManager;

    /**
     * Constructs a UserManagementGUI object.
     * Initializes necessary objects and creates the user management interface.
     * @param user The user who is currently logged in
     */
    public UserManagementGUI(User user) {
        this.userManager = new UserManager();
        this.add(container);
        this.guiInitialize(1000, 500);
        container.setPreferredSize(new Dimension(1000, 500));
        pnl_selected.setPreferredSize(new Dimension(100, pnl_selected.getPreferredSize().height));
        this.user = user;
        if (this.user == null) {
            dispose();
        }
        this.lbl_welcome.setText("Welcome :  " + Helper.firstWordUpper(this.user.getFullName()));

        // Load user table and components
        loadUsersTable();
        loadUserComponent();
        this.tbl_users.setComponentPopupMenu(user_menu);
        logout();
    }

    /**
     * Loads the user table with user data.
     */
    private void loadUsersTable() {
        Object[] col_user_list = {"ID", "Name Surname", "User", "Password", "Role"};
        ArrayList<Object[]> userList = this.userManager.getForTable(col_user_list.length);
        this.createTable(this.tmdl_users, this.tbl_users, col_user_list, userList);
    }

    /**
     * Loads components related to user management and sets up their functionality.
     */
    private void loadUserComponent() {
        btn_update.addActionListener(e -> {
            int selectedUserId = this.getTableSelectedRow(tbl_users,0);
            if (selectedUserId != -1) {
                UserGUI userView = new UserGUI(this.userManager.getById(selectedUserId));
                userView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadUsersTable();
                    }
                });
            } else {
                JOptionPane.showMessageDialog(UserManagementGUI.this, "Please select a user.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        btn_delete.addActionListener(e -> {
            int selectedUserId = this.getTableSelectedRow(tbl_users,0);
            if (selectedUserId != -1){
                if (Helper.confirm("sure","Delete")){
                    if (this.userManager.delete(selectedUserId)){
                        Helper.showMsg("done");
                        loadUsersTable();
                    }else {
                        Helper.showMsg("error");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(UserManagementGUI.this, "Please select a user.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        btn_add.addActionListener(e -> {
            UserGUI userView = new UserGUI(null);
            userView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadUsersTable();
                }
            });
        });

        this.tbl_users.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_users.rowAtPoint(e.getPoint());
                tbl_users.setRowSelectionInterval(selectedRow, selectedRow);
                int selectedUserId = (int) tbl_users.getValueAt(selectedRow, 0);
                if (selectedUserId != -1){
                    txtf_selected_id.setText(String.valueOf(selectedUserId));
                }
            }
        });

        this.user_menu = new JPopupMenu();
        this.user_menu.add("Add").addActionListener(e -> {
            UserGUI userView = new UserGUI(null);
            userView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadUsersTable();
                }
            });
        });

        this.user_menu.add("Update").addActionListener(e -> {
            int selectedUserId = getTableSelectedRow(tbl_users, 0);
            if (selectedUserId != -1) {
                UserGUI userView = new UserGUI(userManager.getById(selectedUserId));
                userView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadUsersTable();
                    }
                });
            } else {
                JOptionPane.showMessageDialog(UserManagementGUI.this, "Please select a row.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        this.user_menu.add("Delete").addActionListener(e -> {
            int selectedUserId = this.getTableSelectedRow(tbl_users,0);
            if (selectedUserId != -1){
                if (Helper.confirm("sure","Delete")){
                    if (this.userManager.delete(selectedUserId)){
                        Helper.showMsg("done");
                        loadUsersTable();
                    }else {
                        Helper.showMsg("error");
                    }
                }
            }else {
                JOptionPane.showMessageDialog(UserManagementGUI.this, "Please select a row.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        this.cmbx_user_filter.addActionListener(e -> {
            String selectedRole = (String) cmbx_user_filter.getSelectedItem();
            if(selectedRole != null ){
                if (selectedRole.equals("ADMIN")) {
                    ArrayList<User> userList = this.userManager.findByAdmin();
                    loadUsersByRole(userList);
                }else if (selectedRole.equals("EMPLOYEE")){
                    ArrayList<User> userList = this.userManager.findByEmployee();
                    loadUsersByRole(userList);
                }else {
                    ArrayList<User> userList = this.userManager.findAll();
                    loadUsersByRole(userList);
                }
            }
        });
    }

    /**
     * Loads users based on their role and updates the user table.
     * @param userList List of users to be loaded
     */
    private void loadUsersByRole(ArrayList<User> userList) {
        tmdl_users.setRowCount(0);
        for (User user : userList) {
            Object[] rowData = {user.getId(), user.getFullName(), user.getUserName(), user.getPassword(), user.getRole()};
            tmdl_users.addRow(rowData);
        }
        tbl_users.setModel(tmdl_users);
        txtf_selected_id.setText("No selected ID");
    }

    /**
     * Logs out the current user.
     */
    public void logout(){
        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI loginView = new LoginGUI();
        });
    }
}
