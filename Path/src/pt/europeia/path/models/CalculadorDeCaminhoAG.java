package pt.europeia.path.models;

import java.util.ArrayList;

import edu.princeton.cs.algs4.StdRandom;
import javafx.geometry.Point2D;


public class CalculadorDeCaminhoAG {
	
	//O genotipo e um array de ints
	private int[][] genotipos;

	private int[] melhorGenotipo;
	
	private double[] aptidoes;
	
	private int geracao = 1;
	
	private double melhorDistancia;

	private int populacao;

	private double taxaMutacao;

	private ArrayList<Point2D> cidades = new ArrayList<Point2D>();

	

	public CalculadorDeCaminhoAG(ArrayList<Point2D> cidades, int populacao, double taxaMutacao) {
		this.cidades = cidades;
		this.populacao = populacao;
		this.taxaMutacao = taxaMutacao;
		
		genotipos = new int[populacao][cidades.size()];
		aptidoes = new double[populacao];
		melhorGenotipo = new int[cidades.size()];
		
		for(int i = 0; i < cidades.size(); i++) {
			melhorGenotipo[i] = i;
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
	public double distancia(int[] genotipo) {

		double somatorio = 0;

		for(int i = 0; i < cidades.size() - 1; i++) {
			somatorio += cidades.get(genotipo[i]).distance(cidades.get(genotipo[i+1]));
		}

		return somatorio;

	}
	
	
	public void mutar(int[] genotipo) {
		double random = StdRandom.uniform();
		if(random <= taxaMutacao) {
			int i = (int) StdRandom.uniform(0,genotipo.length);
			int j = (int) StdRandom.uniform(0,genotipo.length);
			swap(genotipo, i, j);
		}
	}
	
	
	public void novaGeracao() {
		
		//Selecionar os 50% melhores da populacao
//		InsertionModified.sort(aptidao, genotipos);
		
		for(int[] each : genotipos) {
			mutar(each);
		}
		geracao++;
	}
	
		
	public int getGeracao() {
		return geracao;
	}


	private void gerarPopulacao() {
		
		genotipos[0] = melhorGenotipo;
		for(int i = 1; i < populacao; i++) {
			int[] temp = melhorGenotipo.clone();
			shuffle(temp);
			aptidoes[i] = (1 / distancia(temp) + 1);
			genotipos[i] = temp;
		}
		
	}


	//Knuth Shuffling Algorithm - Entry Point
	public static void shuffle(final int[] values) {
		for (int i = 0; i != values.length; i++) {
			final int r = StdRandom.uniform(i + 1);
			swap(values, i, r);
		}
	}
	

	private static void swap(final int[] values, final int firstPosition,
			final int secondPosition) {
		final int temporary = values[firstPosition];
		values[firstPosition] = values[secondPosition];
		values[secondPosition] = temporary;
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


	public int[][] getGenotipos() {
		return genotipos;
	}


	public int[] getMelhorGenotipo() {
		return melhorGenotipo;
	}
	
	public void setMelhorGenotipo(int[] genotipo) {
		melhorGenotipo = genotipo.clone();
	}

}
