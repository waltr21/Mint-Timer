import javafx.application.Application;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.control.Toggle;
import javafx.scene.paint.Color;
import javafx.geometry.HPos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.awt.event.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.control.ToggleButton;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.CheckBox;
import javafx.scene.chart.XYChart;
import javafx.animation.AnimationTimer;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import java.util.*;

public class MintGUI extends Application{
  RubiksTimer timer = new RubiksTimer();
  Rotations scramble = new Rotations();
  TextArea timeField;
  NumberFormat formatter;
  boolean timing, dnfBool, showSeconds;
  ArrayList<Double> averages;
  double averageDouble, width, height;
  long startTime, endTime;
  int typeIndex;
  String moves, fileType, moveType, tempType;
  GridPane dataGrid = new GridPane();
  Stage firstStage;
  Scene arrowScene;
  Text counter, averageText, bestText;
  ToggleButton clear, removeLast;



  public MintGUI(){
    width = 1200.0;
    height = 850.0;
    timing = false;
    averages = new ArrayList<Double>();
    formatter = new DecimalFormat("#00.000");
    dnfBool = false;
    firstStage = new Stage();
    typeIndex = 1;
    fileType = "3x3";
    tempType = fileType;
    moveType = "3x3";
    showSeconds = true;
  }

  @Override
    public void start(Stage primaryStage) {
      primaryStage.setTitle("Mint Timer");
      moves = "";
      moves = scramble.getMoves(moveType);

      //Grid for the active time display
      GridPane timeGrid = new GridPane();
      timeGrid.setHgap(10);
      timeGrid.setVgap(10);
      timeGrid.setPadding(new Insets(20, 20, 20, 20));
      timeGrid.setAlignment(Pos.CENTER);

      //Grid for the scramble algorithms
      GridPane scrambleGrid = new GridPane();
      scrambleGrid.setHgap(10);
      scrambleGrid.setVgap(10);
      scrambleGrid.setPadding(new Insets(20, 20, 20, 20));
      scrambleGrid.setAlignment(Pos.CENTER);

      //Grid for the solve time history
      GridPane historyGrid = new GridPane();
      historyGrid.setHgap(10);
      historyGrid.setVgap(10);
      historyGrid.setPadding(new Insets(20, 20, 20, 20));

      //Grid for the settings and arrow
      GridPane settingsGrid = new GridPane();
      settingsGrid.setHgap(10);
      settingsGrid.setVgap(10);
      settingsGrid.setPadding(new Insets(20, 20, 20, 20));
      settingsGrid.setAlignment(Pos.TOP_RIGHT);

      //Holds all previous times
      timeField = new TextArea();
      timeField.setPrefRowCount(14);
      timeField.setMaxWidth(85);
      timeField.setEditable(false);
      timeField.setStyle("-fx-control-inner-background: #eafff1; -fx-background-color: #eafff1;");
      historyGrid.add(timeField, 0, 1);

      //Text for the typw of cube
      Text timeHistoryText = new Text(fileType);
      timeHistoryText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
      timeHistoryText.setFill(Color.web("#eafff1"));
      historyGrid.add(timeHistoryText, 0, 0);

      //Text for the average
      averageText = new Text("Average: --");
      averageText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
      averageText.setFill(Color.web("#eafff1"));
      historyGrid.add(averageText, 0, 3);

      //Text for the best
      bestText = new Text("Best: --");
      bestText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
      bestText.setFill(Color.web("#eafff1"));
      historyGrid.add(bestText, 0, 2);

      //Running time
      counter = new Text("00.00");
      counter.setFont(Font.font("Tahoma", FontWeight.NORMAL, 120));
      counter.setFill(Color.web("#eafff1"));
      timeGrid.add(counter, 0, 0);

      //Scramble algorithm
      Text scrambleText = new Text();
      scrambleText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
      scrambleText.setText(moves);
      scrambleText.setFill(Color.web("#eafff1"));
      scrambleGrid.add(scrambleText, 0, 0);

      //Settings image
      Image settingsImage = new Image("settings.png");
      ImageView settingsView = new ImageView();
      settingsView.setImage(settingsImage);
      settingsView.setFitWidth(40);
      settingsView.setPreserveRatio(true);
      settingsView.setSmooth(true);
      settingsView.setCache(true);
      settingsGrid.add(settingsView, 1, 0);
      //Make image clickable
      settingsView.setPickOnBounds(true);

      //Text to help even out the grid (bad practice)
      Text centerHelp = new Text("          ");
      centerHelp.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
      settingsGrid.add(centerHelp, 0, 1);

      //Arrow image
      DropShadow shadow = new DropShadow();
      Image arrowImage = new Image("arrow.png");
      ImageView arrowView = new ImageView();
      arrowView.setImage(arrowImage);
      arrowView.setFitWidth(40);
      arrowView.setPreserveRatio(true);
      arrowView.setSmooth(true);
      arrowView.setCache(true);
      arrowView.setRotate(90);
      settingsGrid.add(arrowView, 1, 30);
      //Make image clickable
      arrowView.setPickOnBounds(true);

      //Button if the user does not finish
      ToggleButton dnf = new ToggleButton("DNF");
      timeGrid.add(dnf, 0,1);
      dnf.setStyle("-fx-background-color: #d5ffe4; -fx-font-size: 20pt;");
      dnf.setDisable(true);
      dnf.setVisible(false);

      //Button to remove the last solved time
      removeLast = new ToggleButton("Remove last");
      historyGrid.add(removeLast, 0, 4);
      removeLast.setStyle("-fx-background-color: #d5ffe4; ");
      removeLast.setDisable(true);

      //Button to clear all of the times
      clear = new ToggleButton("Clear");
      historyGrid.add(clear, 0, 5);
      clear.setStyle("-fx-background-color: #d5ffe4; ");
      clear.setDisable(true);

      //Border to line up all of the grids
      BorderPane border = new BorderPane();
      border.setBottom(scrambleGrid);
      border.setLeft(historyGrid);
      border.setCenter(timeGrid);
      border.setRight(settingsGrid);
      border.setStyle("-fx-background-color: #96d7cc;");

      //Create the scene
      Scene scene1 = new Scene(border, width, height);
      primaryStage.setMinWidth(900);
      primaryStage.setMinHeight(850);
      primaryStage.setScene(scene1);
      firstStage = primaryStage;
      firstStage.show();

      //Handles the spacebar detection
      scene1.setOnKeyReleased(new EventHandler<KeyEvent>() {
        @Override
          public void handle(KeyEvent event) {
            switch (event.getCode()) {
              case SPACE:
                //Start the timer
                if (!timing){
                  startTime = timer.startWatch();
                  timing = true;
                  if (showSeconds)
                    displayTimer.start();
                  else
                    counter.setText("Solve");

                  //Disable/enable appropriate buttons
                  dnf.setDisable(false);
                  clear.setDisable(true);
                  dnfBool = false;
                  dnf.setVisible(true);
                  removeLast.setDisable(true);
                }
                else{
                  if (!dnfBool){

                    endTime = timer.endWatch();
                    timing = false;

                    //Display final solve time
                    if (timer.getTime(endTime, startTime) > 60){
                      timeField.appendText(timer.convertMinute(timer.getTime(endTime, startTime)) + "\n");
                      counter.setText(timer.convertMinute(timer.getTime(endTime, startTime)) + "");
                    }
                    else{
                      timeField.appendText(formatter.format(timer.getTime(endTime, startTime)) + "\n");
                      counter.setText(formatter.format(timer.getTime(endTime, startTime)) + "");
                    }

                    if (showSeconds)
                      displayTimer.stop();

                    //Get scramble
                    moves = scramble.getMoves(moveType);
                    scrambleText.setText(moves);

                    //Get average and best with new time included
                    averages.add(timer.getTime(endTime, startTime));
                    averageDouble = timer.getAverage(averages);
                    if (averageDouble < 60)
                      averageText.setText("Average: " + formatter.format(averageDouble));
                    else
                      averageText.setText("Average: " + timer.convertMinute(averageDouble));

                    double bestTime = timer.getBest(averages);
                    if (bestTime < 60)
                      bestText.setText("Best: " + formatter.format(bestTime));
                    else
                      bestText.setText("Best: " + timer.convertMinute(bestTime));

                    //Enable/disable appropriate buttons
                    dnf.setDisable(true);
                    dnf.setVisible(false);
                    removeLast.setDisable(false);
                    clear.setDisable(false);

                  }
                }
              }
            }
      });

      //Clear the times
      clear.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
          clearTimes();
        }
      });

      //If user did not finish
      dnf.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
          dnf.setDisable(true);
          dnfBool = true;
          counter.setText("DNF");
          timing = false;
          moves = scramble.getMoves(moveType);
          scrambleText.setText(moves);
          timeField.requestFocus();
          dnf.setVisible(false);
          if (averages.size() > 0){
            removeLast.setDisable(false);
            clear.setDisable(false);
          }
          if (showSeconds)
            displayTimer.stop();
        }
      });

      //Remove current last time
      removeLast.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
          averages.remove(averages.size() - 1);
          timeField.setText("");
          for (int i = 0; i < averages.size(); i++){
            if (averages.get(i) < 60)
              timeField.appendText(averages.get(i) + "\n");
            else
              timeField.appendText(timer.convertMinute(averages.get(i)) + "\n");
          }
          if (averages.size() == 0){
            removeLast.setDisable(true);
            bestText.setText("Best: --");
            averageText.setText("Average: --");
            clear.setDisable(true);
            return;
          }
          timeField.requestFocus();
          double bestTime = timer.getAverage(averages);
          if(bestTime < 60)
            bestText.setText("Best: " + formatter.format(bestTime));
          else
            bestText.setText("Best: " + timer.convertMinute(bestTime));
          averageDouble = timer.getAverage(averages);
          if (averageDouble < 60)
            averageText.setText("Average: " + formatter.format(averageDouble));
          else
            averageText.setText("Average: " + timer.convertMinute(averageDouble));
        }
      });

      //Click listen for settins menu
      settingsView.setOnMouseClicked(event -> {
        //menu();
        settingsView.setVisible(false);
        arrowView.setVisible(false);

        Text cubeType = new Text("Cube Type: ");
        cubeType.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        cubeType.setFill(Color.web("#eafff1"));
        settingsGrid.add(cubeType, 0, 0);

        Text displaySec = new Text("Show Seconds: ");
        displaySec.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        displaySec.setFill(Color.web("#eafff1"));
        settingsGrid.add(displaySec, 0, 1);

        CheckBox secondCheck = new CheckBox("");
        secondCheck.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        //displaySec.setFill(Color.web("#eafff1"));
        secondCheck.setSelected(showSeconds);
        settingsGrid.add(secondCheck, 1, 1);

        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
        "2x2", "3x3", "4x4", "5x5", "6x6", "7x7"));
        cb.getSelectionModel().select(typeIndex);
        cb.setStyle("-fx-control-inner-background: #eafff1; -fx-background-color: #d5ffe4;");
        settingsGrid.add(cb, 1, 0);

        ToggleButton save = new ToggleButton("Save");
        save.setStyle("-fx-background-color: #d5ffe4; ");
        settingsGrid.add(save, 1, 2);

        save.setOnAction(new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent e) {
            timeField.requestFocus();
            settingsView.setVisible(true);
            arrowView.setVisible(true);
            cubeType.setVisible(false);
            displaySec.setVisible(false);
            secondCheck.setVisible(false);
            cb.setVisible(false);
            save.setVisible(false);
            timeField.requestFocus();
            timeHistoryText.setText((String) cb.getSelectionModel().getSelectedItem());
            showSeconds = secondCheck.isSelected();
            moveType = (String) cb.getSelectionModel().getSelectedItem();
            moves = scramble.getMoves(moveType);
            scrambleText.setText(moves);
            typeIndex = cb.getSelectionModel().getSelectedIndex();

            if (!tempType.equals(cb.getSelectionModel().getSelectedItem())){
              tempType = moveType;
              clearTimes();
            }


            /*So creating new variables in the settings menu would thow off the grid
              by the length of the longest string. I couldnt find a solution for how
              to fix it, so I threw in this blank text to even it back out. I am not
              proud of this, but ya gotta do what ya gotta do.*/
            Text imSorry = new Text("                        ");
            displaySec.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            historyGrid.add(imSorry, 1, 6);
          }
        });

      });

      arrowView.setOnMouseClicked(event -> {
        //reviewBorder.setRoot(null);
        if (averages.size() > 0){
          height = firstStage.getHeight();
          width = firstStage.getWidth();
          height = height - 22;
          fileType = timeHistoryText.getText();
          timer.writeTimes(averages, fileType);
          arrow();
        }
      });

    }

    public void clearTimes(){
      timeField.setText("");
      averageDouble = 0.0;
      averageText.setText("Average: --");
      averages.clear();
      bestText.setText("Best: --");
      clear.setDisable(true);
      removeLast.setDisable(true);
    }

    public void arrow(){
      BorderPane reviewBorder = new BorderPane();
      arrowScene =  new Scene(reviewBorder, width, height);
      firstStage.setScene(arrowScene);

      dataGrid.setHgap(10);
      dataGrid.setVgap(10);
      dataGrid.setPadding(new Insets(20, 20, 20, 20));
      dataGrid.setStyle("-fx-background-color: #96d7cc;");

      GridPane chartGrid = new GridPane();
      chartGrid.setHgap(10);
      chartGrid.setVgap(10);
      chartGrid.setPadding(new Insets(20, 20, 20, 20));
      chartGrid.setAlignment(Pos.CENTER);

      GridPane arrowGrid = new GridPane();
      arrowGrid.setHgap(10);
      arrowGrid.setVgap(10);
      arrowGrid.setPadding(new Insets(20, 20, 20, 20));
      arrowGrid.setAlignment(Pos.CENTER);

      double prevDouble = timer.getAverage(averages);

      ArrayList<Double> allTimes = timer.readTimes(fileType);

      Text totalSolves = new Text();
      totalSolves.setText("Solves: " + allTimes.size());
      totalSolves.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
      totalSolves.setFill(Color.web("#eafff1"));
      dataGrid.add(totalSolves, 0, 0);

      Text bestTime = new Text();
      bestTime.setText("Total Best: " + timer.getBest(allTimes));
      bestTime.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
      bestTime.setFill(Color.web("#eafff1"));
      dataGrid.add(bestTime, 0, 1);

      double totalAverage = timer.getAverage(allTimes);
      Text averageTime = new Text();
      averageTime.setText("Total Avg: " + formatter.format(totalAverage));
      averageTime.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
      averageTime.setFill(Color.web("#eafff1"));
      dataGrid.add(averageTime, 0, 2);

      Text prevAverageText = new Text();
      prevAverageText.setText("Prev Avg: " + formatter.format(averageDouble));
      prevAverageText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
      prevAverageText.setFill(Color.web("#eafff1"));
      dataGrid.add(prevAverageText, 0, 3);

      Image arrowImage = new Image("arrow.png");
      ImageView arrowView2 = new ImageView();
      arrowView2.setImage(arrowImage);
      arrowView2.setFitWidth(40);
      arrowView2.setPreserveRatio(true);
      arrowView2.setSmooth(true);
      arrowView2.setCache(true);
      arrowView2.setRotate(270);
      arrowView2.setPickOnBounds(true);
      arrowGrid.add(arrowView2, 0, 0);

      arrowView2.setOnMouseClicked(event -> {
        averages.clear();
        averageTime.setText("");
        bestTime.setText("");
        totalSolves.setText("");
        prevAverageText.setText("");
        width = firstStage.getWidth();
        height = firstStage.getHeight();
        height = height - 22;
        start(firstStage);
      });

      final CategoryAxis xAxis = new CategoryAxis();
      final NumberAxis yAxis = new NumberAxis();
      xAxis.setLabel("Number Solve");
      //creating the chart
      final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
      lineChart.setTitle("Times");
      XYChart.Series prevTimes = new XYChart.Series();
      prevTimes.setName("Previous Times");
      XYChart.Series overAverage = new XYChart.Series();
      overAverage.setName("Overall Average");
      XYChart.Series prevAverage = new XYChart.Series();
      prevAverage.setName("Previous Average");

      for (int i = 0; i < averages.size(); i++){
        prevTimes.getData().add(new XYChart.Data(i + 1 + "", averages.get(i)));
        overAverage.getData().add(new XYChart.Data(i + 1 + "", totalAverage));
        prevAverage.getData().add(new XYChart.Data(i + 1 + "", prevDouble));
      }

      lineChart.setCreateSymbols(false);
      lineChart.getData().add(prevTimes);
      lineChart.getData().add(overAverage);
      lineChart.getData().add(prevAverage);
      lineChart.setPrefWidth(width * .8);
      lineChart.setPrefHeight(height * .55);
      chartGrid.add(lineChart, 0, 0);

      reviewBorder.setBottom(chartGrid);
      reviewBorder.setTop(dataGrid);
      reviewBorder.setLeft(arrowGrid);
      reviewBorder.setStyle("-fx-background-color: #96d7cc;");

      firstStage.show();
    }

    //Animation timer taken from a template online.
    AnimationTimer displayTimer = new AnimationTimer() {
    private long timestamp;
    private long time = 0;
    private long fraction = 0;
    private int animatedSeconds = 0;
    private int animatedMinutes = 0;

    @Override
    public void start() {
        counter.setText("0.0");
        timestamp = startTime - fraction;
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        fraction = System.currentTimeMillis() - timestamp;
        time = 0;
        animatedSeconds = 0;
        animatedMinutes = 0;
    }

    @Override
    public void handle(long now) {
        long newTime = System.currentTimeMillis();

        if (timestamp + 100 <= newTime) {
            long deltaT = (newTime - timestamp) / 100;
            time += deltaT;
            timestamp += 100 * deltaT;
            if (time > 9){
              time = 0;
              animatedSeconds++;
            }
            if (animatedSeconds > 59){
              animatedSeconds = 0;
              animatedMinutes++;
            }

            if (animatedMinutes > 0){
              if (animatedSeconds < 10)
                counter.setText(animatedMinutes + ":" + "0" +
                animatedSeconds + "." + Long.toString(time));
              else
                counter.setText(animatedMinutes + ":" +
                animatedSeconds + "." + Long.toString(time));
            }
            else
              counter.setText(animatedSeconds + "." + Long.toString(time));
        }
    }
  };

  public static void main(String[] args) {
    launch(args);
  }
}
