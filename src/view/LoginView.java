package view;

import business.UserManager;
import core.Helper;
import entity.User;

import javax.swing.*;

public class LoginView extends Layout {

    private JPanel container;
    private JPanel user_login;
    private JPanel pnl_top;
    private JLabel lbl_rent_a_car;
    private JLabel lbl_management;
    private JPanel pnl_bottom;
    private JTextField textField_username;
    private JPasswordField passwordField;
    private JButton btn_login;
    private JLabel lbl_pass;
    private JLabel lbl_user;
    private UserManager userManager;

    public LoginView(){
        this.userManager = new UserManager();
        add(container);
        this.guiInitilaze(400, 400);

        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(textField_username)){
                JOptionPane.showMessageDialog(null,"Enter Username", "Error", JOptionPane.INFORMATION_MESSAGE);
            }else if (Helper.isFieldEmpty(passwordField)){
                JOptionPane.showMessageDialog(null,"Enter Password", "Error", JOptionPane.INFORMATION_MESSAGE);
            } else {
                User loginUser = this.userManager.findUser(this.textField_username.getText(),this.passwordField.getText());
                if (loginUser == null){
                    JOptionPane.showMessageDialog(null,"User not found");
                }else {
                    if (loginUser.getRole() == User.Role.ADMIN) {
                        System.out.println(loginUser.getName() + " Admin entered");
                            AdminView adminView = new AdminView(loginUser);
                            adminView.setVisible(true);
                    } else if (loginUser.getRole() == User.Role.EMPLOYEE) {
                        System.out.println(loginUser.getName() + " Employee entered");
                            UserManagementView adminView = new UserManagementView(loginUser);
                            adminView.setVisible(true);
                    }
                }
            }
        });
    }
    }