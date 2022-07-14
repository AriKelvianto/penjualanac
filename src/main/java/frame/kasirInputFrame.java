package frame;

import helpers.koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class kasirInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private int id;
    private JPanel buttonPanel;
    private JLabel kode_pk;
    private JLabel kode_merk;
    private JTextField kodemerkTextField;
    private JTextField kodepkTextField;

    public kasirInputFrame(){
        batalButton.addActionListener(e -> {
            dispose();
        });
        simpanButton.addActionListener(e -> {
            String nama_kasir = namaTextField.getText();
            String kode_merk = kodemerkTextField.getText();
            String kode_pk = kodepkTextField.getText();
            Connection c = koneksi.getConnection();
            PreparedStatement ps;
            if (nama_kasir.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data nama",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }
            try {
                String cekSQL;
                if (this.id == 0) { //jika TAMBAH

                    cekSQL = "SELECT * FROM kasir WHERE nama_kasir=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_kasir);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "nama yang sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO kasir (id,nama_kasir,kode_merk,kode_pk) VALUES (NULL, ?, ?, ?)";
                    insertSQL = "INSERT INTO `kasir` (`id`, `nama_kasir`, `kode_merk`, `kode_pk`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `kasir` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO kasir (nama_kasir,kode_merk,kode_pk) VALUES (?)";
                    insertSQL = "INSERT INTO kasir SET nama_kasir=?, kode_merk=?, kode_pk=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama_kasir);
                    ps.setString(2, kode_merk);
                    ps.setString(3, kode_pk);
                    ps.executeUpdate();
                    dispose();
                } else {
                    cekSQL = "SELECT * FROM kasir WHERE nama_kasir=? AND kode_merk=? AND kode_pk=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_kasir);
                    ps.setString(2, kode_merk);
                    ps.setString(3, kode_pk);
                    ps.setInt(4,id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Data sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String updateSQL = "UPDATE kasir SET nama_kasir=?,kode_merk=?, kode_pk=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama_kasir);
                    ps.setString(2, kode_merk);
                    ps.setString(3, kode_pk);
                    ps.setInt(4, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("input kasir");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        Connection c = koneksi.getConnection();
        String findSQL = "SELECT * FROM kasir WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setId(int id) {
    }
}