package pt.europeia.path.models;

import java.util.ArrayList;

import edu.princeton.cs.algs4.StdRandom;
import javafx.geometry.Point2D;


public class CalculadorDeCaminhoAG {
	
	//O genotipo e um array de ints
	private ArrayList<ArrayList<Integer>> genotipos = new ArrayList<ArrayList<Integer>>();

	private ArrayList<Integer> melhorGenotipo = new ArrayList<Integer>();
	
	private ArrayList<Double> aptidao = new ArrayList<Double>();
	
	private int geracao = 1;
	
	private double melhorDistancia;

	private int populacao;

	private double taxaMutacao;

	private ArrayList<Point2D> cidades = new ArrayList<Point2D>();

	

	public CalculadorDeCaminhoAG(ArrayList<Point2D> cidades, int populacao, double taxaMutacao) {
		this.cidades = cidades;
		this.populacao = populacao;
		this.taxaMutacao = taxaMutacao;
		
		for(int i = 0; i < cidades.size(); i++) {
			melhorGenotipo.add(i);
		}
		
		melhorDistancia = distancia(melhorGenotipo);
		
		gerarPopulacao();
		
	}


	/**
	 * Calcula o caminho mais curto a percorrer se passarmos por todas a cidades
	 * @return
	 */
	public ArrayList<Integer> aptidoes() {
		return null;
	}
	

	/**
	 * Calcula a distancia necessaria para percorrer uma ordem especifica de cidades
	 * @param genotipo A ordem a percorrer
	 * @return
	 */
	public double distancia(ArrayList<Integer> genotipo) {

		double somatorio = 0;

		for(int i = 0; i < cidades.size() - 1; i++) {
			somatorio += cidades.get(genotipo.get(i)).distance(cidades.get(genotipo.get(i+1)));
		}

		return somatorio;

	}
	
	
	public void mutar(ArrayList<Integer> genotipo) {
		double random = StdRandom.uniform();
		if(random <= taxaMutacao) {
			int i = (int) StdRandom.uniform(0,genotipo.size());
			int j = (int) StdRandom.uniform(0,genotipo.size());
			swap(genotipo, i, j);
		}
	}
	
	
	public void novaGeracao() {
		
		//Selecionar os 50% melhores da populacao
//		InsertionModified.sort(aptidao, genotipos);
		
		for(ArrayList<Integer> each : genotipos) {
			mutar(each);
		}
		geracao++;
	}
	
		
	public int getGeracao() {
		return geracao;
	}


	private void gerarPopulacao() {
		
		genotipos.add(melhorGenotipo);
		for(int i = 0; i < populacao - 1; i++) {
			ArrayList<Integer> temp = new ArrayList(melhorGenotipo);
			shuffle(temp);
			aptidao.add(1 / distancia(temp) + 1);
			genotipos.add(temp);
		}
		
	}


	//Knuth Shuffling Algorithm - Entry Point
	public static void shuffle(final ArrayList values) {
		for (int i = 0; i != values.size(); i++) {
			final int r = StdRandom.uniform(i + 1);
			swap(values, i, r);
		}
	}
	

	private static void swap(final ArrayList values, final int firstPosition,
			final int secondPosition) {
		final Object temporary = values.get(firstPosition);
		values.set(firstPosition, values.get(secondPosition));
		values.set(secondPosition, temporary);
	}
	//Knuth Shuffling Algorithm - End Point

	public double getMelhorDistancia() {
		return melhorDistancia;
	}

	public void setMelhorDistancia(double melhorDistancia) {
		this.melhorDistancia = melhorDistancia;
	}


	public int getPopulacao() {
		return populacao;
	}


	public void setPopulacao(int populacao) {
		this.populacao = populacao;
	}


	public double getTaxaMutacao() {
		return taxaMutacao;
	}


	public void setTaxaMutacao(double taxaMutacao) {
		this.taxaMutacao = taxaMutacao;
	}


	public ArrayList<Point2D> getCidades() {
		return cidades;
	}


	public void setCidades(ArrayList<Point2D> cidades) {
		this.cidades = cidades;
	}


	public ArrayList<ArrayList<Integer>> getGenotipos() {
		return genotipos;
	}


	public ArrayList<Integer> getMelhorGenotipo() {
		return melhorGenotipo;
	}
	
	
//	public static void main(String[] args) {
//		ArrayList<Point2D> cidades = new ArrayList<Point2D>();
//		int i = new Random().nextInt(8);
//		while(i < 10) {
//			double x = StdRandom.uniform() * (100);
//			double y = StdRandom.uniform() * (100);
//			Coordenadas random = new Coordenadas(x,y);
//			cidades.add(random);
//			i++;
//		}
//		CalculadorDeCaminho n = new CalculadorDeCaminho(cidades,100,1);
//	}

}
