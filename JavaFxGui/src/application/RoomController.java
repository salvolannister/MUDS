package application;

import DB.DBUtil;
import DB.QueryConfiguration;
import DB.QueryFake;
import DB.QueryRoom;
import DTO.Polo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDateTimeTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class RoomController implements Initializable {

    @FXML private Button SearchButton;
    @FXML private Pane graph_container;
    @FXML private LocalDateTimeTextField DataF;
    @FXML private LocalDateTimeTextField DataI;
    @FXML private Slider nav;
    @FXML private ComboBox<String> roomCB;
    @FXML private ComboBox<String> configCB;
    @FXML private Button start;
    @FXML private Button stop;
    private ScatterChart<Number, Number> grafico;

    private Map<String, Polo> risultato= new HashMap<>();
    private ObservableList<String> roomList = FXCollections.observableArrayList();
    private List<String> readList = new ArrayList<>();
    private ObservableList<String> configList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final NumberAxis xAxis = new NumberAxis(0, 10, 0.01);
        final NumberAxis yAxis = new NumberAxis(0, 10, 0.01);
        xAxis.setLabel("posX");
        yAxis.setLabel("posY");
        grafico=new ScatterChart<Number, Number>(xAxis, yAxis);
        grafico.setTitle("Posizione");
        graph_container.getChildren().add(grafico); //aggiungo un grafico vuoto

        /* setting content in Room and Configuration button*/
        try {
            DBUtil db = new DBUtil();
            if (!db.openConnection("database.db")) {
                System.err.println("Errore di Connessione al DB. Impossibile Continuare");
                System.exit(-1);
            }

            QueryRoom p = new QueryRoom(db.getConn());
            QueryConfiguration qC = new QueryConfiguration(db.getConn());

            try {
             readList = p.getRoomName();

                if (readList != null) {
                    for (String room : readList) {
                        roomList.add(room);
                        System.out.println(room);
                    }
                }
                List<String> readListConf = qC.getConfNames();
                if (readListConf != null) {
                    for (String config : readListConf) {
                        configList.add(config);
                       // System.out.println(config);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            db.closeConnection();
            configCB.setItems(configList);
            roomCB.setItems(roomList);
        }catch(NullPointerException n){
            return;
        }
    }

    public void search(MouseEvent mouseEvent) {



        try {




            Timestamp inizio = Timestamp.valueOf(DataI.getLocalDateTime());
            Timestamp fine = Timestamp.valueOf(DataF.getLocalDateTime());

            DBUtil db=new DBUtil();
            if(!db.openConnection("database.db")){
                System.err.println("Errore di Connessione al DB. Impossibile Continuare");
                System.exit(-1);
            }
            QueryFake p=new QueryFake(db.getConn());

            try {
                nav.getValue();
                risultato=p.showPosition(String.valueOf(inizio.getTime()), String.valueOf(fine.getTime()));
                if(risultato!=null) {
                    //System.out.println("tutto ok");




                    XYChart.Series series = new XYChart.Series();
                    for (String s : risultato.keySet()) {

                        series.getData().add(new XYChart.Data(risultato.get(s).getX(), risultato.get(s).getY()));

                    }
                    graph_container.getChildren().remove(grafico); //rimuovo il grafico vuoto
                    grafico.getData().add(series);
                    graph_container.getChildren().add(grafico);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            db.closeConnection();
            DataI.setText("");
            DataI.setLocalDateTime(null);
            DataF.setText("");
            DataF.setLocalDateTime(null);
            return;
        }catch (NullPointerException n){
            //AreaInfo.setText("inserire data e ora di inizio e fine");
            //DataF.text
            return;
        }


    }



    @FXML
    public void back(MouseEvent mouseEvent) {

        Parent configurationPage;
        try {
            configurationPage = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene configurationPageScene = new Scene(configurationPage);
            Stage appStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            appStage.hide();
            appStage.setScene(configurationPageScene);
            appStage.show();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }


    public void printValue(MouseEvent mouseEvent) {
        System.out.println(nav.getValue());

    }
}
