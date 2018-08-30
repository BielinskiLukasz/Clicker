package sample.menu.game.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import sample.menu.game.model.RoomModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Room implements Initializable {

    @FXML
    private Label roomIncome;
    @FXML
    private HBox employeesViewList;
    @FXML
    private Button buyButton;

    private World world;
    private City city;
    private RoomModel roomModel;
    private List<Employee> employeeList;

    Room(World world, City city, int floor, int cityLifeCostLvl) {
        this.world = world;
        this.city = city;
        roomModel = new RoomModel(floor, cityLifeCostLvl);
        employeeList = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buyButton.setVisible(false);
        buyButton.setText("Cost of purchase: " + roomModel.getRoomBuyCost() + " $");

        roomIncome.setVisible(false);
        roomIncome.setText("" + roomModel.getRoomIncomePerSec() + " $/s");
    }

    @FXML
    public void buyRoom() {
        if (world.getFounds() >= roomModel.getRoomBuyCost()) {
            world.charge(roomModel.getRoomBuyCost());

            buyButton.setVisible(false);
            roomIncome.setVisible(true);

            for (int i = 0; i < 6; i++) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("employee.fxml"));
                    Employee employee = new Employee(world, this, i, city.getCityLifeCostLvl());
                    loader.setController(employee);
                    Pane pane = loader.load();

                    employeesViewList.getChildren().add(pane);

                    employeeList.add(employee);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            city.setNextRoomAvailable();
        } else {
            System.out.println("ROOM - YOU NEED MORE MONEY"); //TODO UPGRADE
        }
    }

    void actualizeIncome(double employeeIncomePerSec) {
        roomModel.actualizeIncome(employeeIncomePerSec);
        roomIncome.setText("" + roomModel.getRoomIncomePerSec() + " $/s");
        city.actualizeIncome(employeeIncomePerSec);
    }

    int getFloor() {
        return roomModel.getFloor();
    }

    void setAvailable() {
        buyButton.setVisible(true);
    }
}