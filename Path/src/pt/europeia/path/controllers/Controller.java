package pt.europeia.path.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import edu.princeton.cs.algs4.StdRandom;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt.europeia.path.models.CalculadorDeCaminhoAG;

public class Controller {

	private Timeline time = new Timeline();

	private int currentGen = 0;

	private double tamanhoCirc = 15;

	private CalculadorDeCaminhoAG calc;

	ArrayList<Point2D> cidades = new ArrayList<Point2D>();

	GraphicsContext gc;

	@FXML
	TextField populacao;

	@FXML
	Slider mutacao;

	@FXML
	TextField numCidades;
	
	@FXML
	TextField geracoes;
	
	@FXML
	Label mutv;

	@FXML
	Canvas canvas = new Canvas();

	@FXML
	public void initialize() {

		tamanhoCirc = 15;
		
		mutacao.setMax(1.0);
		mutacao.setMin(0.0);
		mutacao.setValue(.5);
		mutacao.setShowTickMarks(true);
		mutacao.setMajorTickUnit(.25);

		gc = canvas.getGraphicsContext2D();

		numCidades.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					numCidades.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		populacao.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					numCidades.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		
		geracoes.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					geracoes.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		canvas.setOnMouseClicked(mouseClick -> {
			time.stop();
			time.getKeyFrames().clear();
			cidades.add(new Point2D(mouseClick.getX(), mouseClick.getY()));
			update();
		});

	}


	@FXML
	public void random() {

		time.stop();

		clear();

		cidades = new ArrayList<Point2D>();

		//Caso nao for especificado o numero de cidade adiciona aleatoriamente entre 3 a 10 cidades
		if(numCidades.getText().isEmpty()) {
			int i = new Random().nextInt(8);
			while(i < 10) {
				double x = StdRandom.uniform() * (canvas.getWidth() - tamanhoCirc - 15);
				double y = StdRandom.uniform() * (canvas.getHeight() - tamanhoCirc - 15);
				Point2D random = new Point2D(x,y);
				cidades.add(random);
				i++;
			}

		} else {

			for(int i = 0; i < Integer.parseInt(numCidades.getText()); i++) {
				double x = StdRandom.uniform() * (canvas.getWidth() - tamanhoCirc - 15);
				double y = StdRandom.uniform() * (canvas.getHeight() - tamanhoCirc - 15);
				Point2D random = new Point2D(x,y);
				cidades.add(random);
			}
		}

		update();
	}

	@FXML
	public void playStop() {
		if(time.getStatus() == Animation.Status.RUNNING) {
			time.pause();
		} else if(time.getStatus() == Animation.Status.PAUSED) {
			time.play();
		}
	}

	@FXML
	public void run() {

		int pop = populacao.getText().isEmpty() ? 100 : Integer.parseInt(populacao.getText());
		int ger = geracoes.getText().isEmpty() ? 5 : Integer.parseInt(geracoes.getText());

		calc = new CalculadorDeCaminhoAG(cidades,pop,mutacao.getValue(), ger);

		time.stop();
		time.getKeyFrames().clear();

		currentGen = 0;

		time.getKeyFrames().add(new KeyFrame(Duration.millis(50), event -> {

			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

			int[] genAtual = calc.getGenotipos()[currentGen];

			double dist = calc.distancia(genAtual);
			if(dist < calc.getMelhorDistancia()) {
				calc.setMelhorDistancia(dist);
				calc.setMelhorGenotipo(genAtual);
			}

			//Melhor caminho ate ao momento
			gc.setStroke(Color.FORESTGREEN);
			gc.setLineWidth(5);
			for(int j = 0; j < cidades.size() - 1; j++) {
				gc.strokeLine(cidades.get(calc.getMelhorGenotipo()[j]).getX() + tamanhoCirc/2,cidades.get(calc.getMelhorGenotipo()[j]).getY() + tamanhoCirc/2,
						cidades.get(calc.getMelhorGenotipo()[j+1]).getX() + tamanhoCirc/2,cidades.get(calc.getMelhorGenotipo()[j+1]).getY() + tamanhoCirc/2);
			}

			//Caminho atual
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);
			for(int j = 0; j < cidades.size() - 1; j++) {
				gc.strokeLine(cidades.get(genAtual[j]).getX() + tamanhoCirc/2,cidades.get(genAtual[j]).getY() + tamanhoCirc/2,
						cidades.get(genAtual[j+1]).getX() + tamanhoCirc/2,cidades.get(genAtual[j+1]).getY() + tamanhoCirc/2);
			}

			DecimalFormat df = new DecimalFormat("0.00");
			
			currentGen++;
			gc.strokeText("Melhor distancia "+ df.format(calc.getMelhorDistancia()), 5, 20);
			gc.strokeText("População " + calc.getPopulacao() + "\t\tTaxa de Mutação " + df.format(calc.getTaxaMutacao()) + 
					"\t\t" + calc.getGeracaoAtual() + " de " + calc.getGeracoes() + " Gerações", 20, canvas.getHeight() - 5);

			update();

			if(currentGen == calc.getGenotipos().length) {
				currentGen = 0;
				calc.novaGeracao();
			}

			if(calc.getGeracaoAtual() > calc.getGeracoes()) {
				time.stop();
			}

		}));

		time.setCycleCount(time.INDEFINITE);
		time.play();
		update();

	}

	@FXML
	public void clear() {
		time.stop();
		time.getKeyFrames().clear();
		currentGen = 0;
		cidades = new ArrayList<Point2D>();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

	}

	/**
	 * Faz update do canvas
	 */
	public void update() {

		gc.setFill(Color.BLACK);
		for(int i = 0; i < cidades.size(); i++) {
			gc.fillOval(cidades.get(i).getX(), cidades.get(i).getY(), tamanhoCirc, tamanhoCirc);
		}

	}





}
