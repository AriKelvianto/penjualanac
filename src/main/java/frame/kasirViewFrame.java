package frame;

import helpers.koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class kasirViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;
    private JButton cariButton;
    private JTextField textField1;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public kasirViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
           }
        });
        cariButton.addActionListener(e ->{
            Connection c = koneksi.getConnection();
            String keyword = "%" + textField1.getText() + "%";
            String searchSQL = "SELECT * FROM kasir WHERE id like ?";
            try{
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(  1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[2];
                while (rs.next()){
                    row[0] = rs.getInt(  "id");
                    row[1] = rs.getString(   "nama");
                    dtm.addRow(row);
                }
            }catch (SQLException ex){
                throw  new RuntimeException(ex);
            }
        });
        isiTable();
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Data Kasir");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable(){
        Connection c = koneksi.getConnection();
        String selectSQL = "SELECT * FROM kasir";
        try{
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            String header[] = {"id","nama_kasir","kode_merk","kode_pk"};
            DefaultTableModel dtm = new DefaultTableModel(header,  0);
            viewTable.setModel(dtm);
            Object[] row = new Object[4];
            while (rs.next()){
                row[0] = rs.getInt(  "id");
                row[1] = rs.getString(  "nama_kasir");
                row[2] = rs.getInt(  "kode_merk");
                row[3] = rs.getInt(  "kode_pk");
                dtm.addRow(row);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
