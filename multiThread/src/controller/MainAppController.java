package controller;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clients.Client;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.MainApp;
import tasks.OrderTask;
import utils.KeyGenerator;

public class MainAppController {

	private MainApp mainApp;

	@FXML
	private TextField client;
	@FXML
	private TextField task;
	@FXML
	private TextField work;
	@FXML
	private Label ctime;
	@FXML
	private Label sum;
	@FXML
	private ListView<ProgressBar> listView;
	@FXML
	private Button calBtn;
	@FXML
	public void calculateHandler() {
		int cNum = Integer.parseInt(client.getText());
		int tNum = Integer.parseInt(task.getText());
		int wNum = Integer.parseInt(work.getText());

		ExecutorService pool = null;
		ListView list = new ListView();
		addListViewToPane(list);
		calBtn.setDisable(true);
		try {
			pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

			for (int i = 0; i < cNum; i++) {
				Client c = new Client(wNum, tNum, timer(), KeyGenerator.genUniqueKey());
				HBox hbox = new HBox();
				ProgressBar bar = new ProgressBar(0);
				Text txtState = new Text();
				showProgressBar(list, bar, txtState,hbox);
				progressBarListener(bar,txtState,list,hbox,c);
				bar.progressProperty().unbind();
				bar.progressProperty().bind(c.progressProperty());
				pool.execute(c);
			}
		} finally {
			pool.shutdown();
		}

	}

	private void progressBarListener(ProgressBar bar,Text txtState,ListView list,HBox hbox,Client c) {
		bar.indeterminateProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldBool, Boolean newBool) {
				if(newBool) {
					txtState.setText("Waiting..");
					txtState.setFill(Color.RED);
				}else {
					txtState.setText("Loading..");
					txtState.setFill(Color.YELLOW);
				}
				
			}
		});

		bar.progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldNum, Number newNum) {
				if (newNum.doubleValue() == 1) {
					txtState.setText("Work Done");
					txtState.setFill(Color.GREEN);
					if(!bar.isIndeterminate()) {
						if(list.getItems().remove(hbox)) {
							list.getItems().add(c);
						}
						
					}
					
				}
			}
		 
		});
	}

	private void showProgressBar(ListView list, ProgressBar bar, Text txtState,HBox hbox) {
		HBox.setHgrow(bar, Priority.ALWAYS);
		HBox.setHgrow(txtState, Priority.ALWAYS);
		setProgressBarLayout(bar);
		hbox.getChildren().addAll(bar, txtState);
		list.getItems().add(hbox);
	}

	private void setProgressBarLayout(ProgressBar bar) {
		bar.setMaxWidth(Double.MAX_VALUE);
		bar.setPadding(new Insets(0, 10, 0, 0));
	}

	private void addListViewToPane(ListView list) {
		BorderPane pane = mainApp.getView();
		pane.setCenter(list);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		Platform.runLater(() -> mainApp.getView().requestFocus());
	}

	public int timer() {
		int num = new Random().nextInt(9);
		return num < 9 ? num + 5 : num;
	}

}
