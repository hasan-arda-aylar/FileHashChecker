import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FileHashChecker extends JFrame implements ActionListener {
    private ImageIcon icon = new ImageIcon("icon.png");
    private File f;
    private JComboBox<String> comboBox;
    private static String checksum = "";
    private static String algo = "";
    private static JPanel panel_omni, text_panel, Combo_panel, file_path_panel, pass_fail;
    private JTextField text_field, file_path;
    private String[] algos = {"MD5", "SHA1", "SHA256", "SHA384", "SHA512"};
    private String path = "";
    private static JLabel lbl_pass, lbl_fail;

    FileHashChecker(){
    setTitle("Hash Checker");
    setSize(500,500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout(5,5));
    setResizable(false);
    setIconImage(icon.getImage());


    panel_omni = new JPanel();
    panel_omni.setLayout(new GridLayout(3,1,5,5));



    text_panel = new JPanel();
    text_panel.setLayout(new BorderLayout(5,5));
    text_field = new JTextField("Enter Checksum Here");
    JLabel lbl_1 = new JLabel("Check sum: ");
    text_panel.add(lbl_1, BorderLayout.WEST);
    text_panel.add(text_field, BorderLayout.CENTER);


    Combo_panel = new JPanel();
    Combo_panel.setLayout(new BorderLayout(5,5));
    comboBox = new JComboBox(algos);
    JLabel lbl_2 = new JLabel("Algorithm: ");
    Combo_panel.add(lbl_2, BorderLayout.WEST);
    Combo_panel.add(comboBox,  BorderLayout.CENTER);

    file_path_panel = new JPanel();
    file_path_panel.setLayout(new BorderLayout(5,5));
    JLabel lbl_3 = new JLabel("File path: ");
    file_path_panel.add(lbl_3, BorderLayout.WEST);
    file_path = new JTextField("File path");
    file_path.setEditable(false);
    file_path_panel.add(file_path, BorderLayout.CENTER);
    JButton open_button = new JButton("open");
    open_button.setPreferredSize(new Dimension(15,15));
    open_button.addActionListener(this);
    open_button.setActionCommand("open");
    file_path_panel.add(open_button, BorderLayout.EAST);



    panel_omni.add(text_panel);
    panel_omni.add(Combo_panel);
    panel_omni.add(file_path_panel);

    add(panel_omni, BorderLayout.NORTH);

        pass_fail = new JPanel();
        pass_fail.setLayout(new OverlayLayout(pass_fail));
        pass_fail.setAlignmentX(Component.CENTER_ALIGNMENT);
        pass_fail.setAlignmentY(Component.CENTER_ALIGNMENT);


        lbl_pass = new JLabel("File Hash check successful", SwingConstants.CENTER);
        lbl_pass.setForeground(Color.WHITE);
        lbl_pass.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_pass.setAlignmentY(Component.CENTER_ALIGNMENT);
        lbl_pass.setVisible(false);


        lbl_fail = new JLabel("File Hash check failed", SwingConstants.CENTER);
        lbl_fail.setForeground(Color.WHITE);
        lbl_fail.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_fail.setAlignmentY(Component.CENTER_ALIGNMENT);
        lbl_fail.setVisible(false);


        pass_fail.add(lbl_fail);
        pass_fail.add(lbl_pass);


    add(pass_fail, BorderLayout.CENTER);

    JButton check_button = new JButton("Check");
    check_button.addActionListener(this);
    check_button.setActionCommand("check");
    add(check_button,BorderLayout.SOUTH);
    }


    public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("open")) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            path = file.getAbsolutePath();
            file_path.setText(path);
            System.out.println("Selected file: " + path);
        }
    } else if(e.getActionCommand().equals("check")){
        f = new File(file_path.getText());
        algo = (String) comboBox.getSelectedItem();
        checksum = text_field.getText().toUpperCase();
        System.out.println("Algorithm: " + algo);
        System.out.println("Checksum: " + checksum);


        try {
            if (checksum.equals(computeHash(f, algo))) {
                lbl_fail.setVisible(false);
                lbl_pass.setVisible(true);
                pass_fail.setBackground(Color.GREEN);
            } else{
                lbl_pass.setVisible(false);
                lbl_fail.setVisible(true);
                pass_fail.setBackground(Color.RED);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    }

    public static String computeHash(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        try (FileInputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, md)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {

            }
        }
        byte[] digest = md.digest();
        System.out.println("File Hash: " + bytesToHex(digest).toUpperCase());
        return bytesToHex(digest).toUpperCase();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args){
        FileHashChecker gui = new FileHashChecker();
        gui.setVisible(true);


    }
}
