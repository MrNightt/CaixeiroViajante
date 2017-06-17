package pt.europeia.path.controllers;

import java.util.ArrayList;
import java.util.Random;

import edu.princeton.cs.algs4.StdRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt.europeia.path.models.CalculadorDeCaminhoAG;

public class Controller {
	
	private Timeline time = new Timeline();

	private int currentGen = 0;

	private int melhorGen = 0;

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
	Canvas canvas = new Canvas();

	@FXML
	public void initialize() {

		tamanhoCirc = 15;

		gc = canvas.getGraphicsContext2D();
		
		mutacao = new Slider(0, 1, .5);

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

//		mutacao.textProperty().addListener(new ChangeListener<String>() {
//			@Override
//			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				if (!newValue.matches("\\d*")) {
//					numCidades.setText(newValue.replaceAll("[^\\d]", ""));
//				}
//			}
//		});

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
				double x = StdRandom.uniform() * (canvas.getWidth() - tamanhoCirc);
				double y = StdRandom.uniform() * (canvas.getHeight() - tamanhoCirc);
				Point2D random = new Point2D(x,y);
				cidades.add(random);
				i++;
			}

		} else {

			for(int i = 0; i < Integer.parseInt(numCidades.getText()); i++) {
				double x = StdRandom.uniform() * (canvas.getWidth() - tamanhoCirc);
				double y = StdRandom.uniform() * (canvas.getHeight() - tamanhoCirc);
				Point2D random = new Point2D(x,y);
				cidades.add(random);
			}
		}

		update();
	}

	@FXML
	public void run() {

		int pop = populacao.getText().isEmpty() ? 100 : Integer.parseInt(populacao.getText());
//		double mut = mutacao.getText().isEmpty() ? 0.1 : Integer.parseInt(mutacao.getText());
		double mut = mutacao.getValue();
		
		calc = new CalculadorDeCaminhoAG(cidades,pop,mut);

		time.stop();
		time.getKeyFrames().clear();

		currentGen = 0;

		time.getKeyFrames().add(new KeyFrame(Duration.millis(50), event -> {

			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

			ArrayList<Integer> genAtual = calc.getGenotipos().get(currentGen);

			double dist = calc.distancia(genAtual);
			if(dist < calc.getMelhorDistancia()) {
				calc.setMelhorDistancia(dist);
				melhorGen = currentGen;
			}
			
			//Melhor caminho ate ao momento
			gc.setStroke(Color.FORESTGREEN);
			gc.setLineWidth(5);
			for(int j = 0; j < cidades.size() - 1; j++) {
				gc.strokeLine(cidades.get(calc.getGenotipos().get(melhorGen).get(j)).getX() + tamanhoCirc/2,cidades.get(calc.getGenotipos().get(melhorGen).get(j)).getY() + tamanhoCirc/2,
						cidades.get(calc.getGenotipos().get(melhorGen).get(j+1)).getX() + tamanhoCirc/2,cidades.get(calc.getGenotipos().get(melhorGen).get(j+1)).getY() + tamanhoCirc/2);
			}

			//Caminho atual
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);
			for(int j = 0; j < cidades.size() - 1; j++) {
				gc.strokeLine(cidades.get(genAtual.get(j)).getX() + tamanhoCirc/2,cidades.get(genAtual.get(j)).getY() + tamanhoCirc/2,
						cidades.get(genAtual.get(j+1)).getX() + tamanhoCirc/2,cidades.get(genAtual.get(j+1)).getY() + tamanhoCirc/2);
			}

			currentGen++;
			gc.strokeText(""+calc.getMelhorDistancia(), 0, 20);
			gc.strokeText(calc.getGeracao()+" Geração", canvas.getWidth() - 70, canvas.getHeight() - 20);

			update();
			
			if(currentGen == calc.getGenotipos().size()) {
				currentGen = 0;
				calc.novaGeracao();
			}
			
			if(calc.getGeracao() > 5) {
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
