import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class pantalla {
    private JPanel panel1;
    private JTextField placaTField;
    private JTextField marcaTField;
    private JTextField modeloTField;
    private JTextField colorTField;
    private JComboBox usoBOX;
    private JComboBox gasolinaBOX;
    private JComboBox paisBOX;
    private JTable datosTable;
    private JButton registrarButton;
    private JButton actualizarButton;
    private JButton buscarButton;
    private JButton borrarButton;
    private JButton mostrarButton;
    Connection con;

    public pantalla() {
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectar();
                try {
                    PreparedStatement ps;
                    ps = con.prepareStatement("insert into carros (placa, marca, modelo, color, uso, gasolina, pais) values (?,?,?,?,?,?,?)");
                    ps.setInt(1, Integer.parseInt(placaTField.getText()));
                    ps.setString(2, marcaTField.getText());
                    ps.setString(3, modeloTField.getText());
                    ps.setString(4, colorTField.getText());
                    ps.setString(5, usoBOX.getSelectedItem().toString());
                    ps.setString(6, gasolinaBOX.getSelectedItem().toString());
                    ps.setString(7, paisBOX.getSelectedItem().toString());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Los datos han sido guardados correctamente");
                    mostrarDatos();
                    limpiarEntradas();
                } catch (SQLException ex) {
                    throw new RuntimeException(" Error al registrar al cliente "+ ex);
                }
            }
        });
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreparedStatement ps;
                try{
                    ps = con.prepareStatement("Update carros set marca = ?, modelo = ?, color = ? uso = ?, gasolina = ?, pais = ? Where placa =" + placaTField.getText());
                    ps.setInt(1, Integer.parseInt(placaTField.getText()));
                    ps.setString(2, marcaTField.getText());
                    ps.setString(3, modeloTField.getText());
                    ps.setString(4, colorTField.getText());
                    ps.setString(5, usoBOX.getSelectedItem().toString());
                    ps.setString(6, gasolinaBOX.getSelectedItem().toString());
                    ps.setString(7, paisBOX.getSelectedItem().toString());
                    int indice = ps.executeUpdate();
                    if (indice > 0){
                        JOptionPane.showMessageDialog(null, "Datos actualizados correctamente");
                        mostrarDatos();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "No selecciono una fila");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectar();
                try {
                    Statement s = con.createStatement();
                    s = con.createStatement();
                    placaTField.setText(placaTField.getText());
                    ResultSet rs = s.executeQuery("SELECT * FROM carros WHERE id =" + placaTField.getText());

                    while(rs.next()){
                        if(placaTField.getText().equals(rs.getInt(1))){
                            marcaTField.setText(rs.getString(2));
                            modeloTField.setText(rs.getString(3));
                            colorTField.setText(rs.getString(4));
                            usoBOX.setSelectedItem(rs.getObject(5));
                            gasolinaBOX.setSelectedItem(rs.getObject(6));
                            paisBOX.setSelectedItem(rs.getObject(7));
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"DATOS NO ENCONTRADOS");
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        mostrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectar();
                mostrarDatos();
            }
        });
    }

    //Limpiar entradas
    public void limpiarEntradas(){
        placaTField.setText("");
        marcaTField.setText("");
        modeloTField.setText("");
        colorTField.setText("");
        usoBOX.setSelectedIndex(0);
        gasolinaBOX.setSelectedIndex(0);
        paisBOX.setSelectedIndex(0);
    }
    //Mostrar datos

    public void mostrarDatos(){
        DefaultTableModel mod = new DefaultTableModel();
        mod.addColumn("Placa");
        mod.addColumn("Marca");
        mod.addColumn("Modelo");
        mod.addColumn("Color");
        mod.addColumn("Estado");
        mod.addColumn("Tip.Gasolina");
        mod.addColumn("País");
        datosTable.setModel(mod);
        String consutlasql = "Select*from carros";

        String data[] = new String[7];

        Statement st;
        try{
            st = con.createStatement();
            ResultSet r = st.executeQuery(consutlasql);
            while (r.next()){
                data[0] = r.getString(1);
                data[1] = r.getString(2);
                data[2] = r.getString(3);
                data[3] = r.getString(4);
                data[4] = r.getString(5);
                data[5] = r.getString(6);
                data[6] = r.getString(7);
                mod.addRow(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException( "Error al mostrar datos"+ e);
        }
    }
    //Metodo conectar
    public void conectar(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehiculos",
                    "root", "mateo");
            System.out.println("La conección fue un exito");
        }catch (SQLException e){
            throw new RuntimeException("Error al conectar"+ e);
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Importacion de carros");
        frame.setContentPane(new pantalla().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
