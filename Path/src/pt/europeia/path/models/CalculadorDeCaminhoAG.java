package pt.europeia.path.models;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdRandom;
import javafx.geometry.Point2D;


public class CalculadorDeCaminhoAG {

	//O genotipo e um array de ints
	private int[][] genotipos;

	private int[] melhorGenotipo;

	private double[] aptidoes;

	private int geracaoAtual = 1;

	private final int geracoes;

	private double melhorDistancia;

	private int populacao;

	private double taxaMutacao;

	private ArrayList<Point2D> cidades = new ArrayList<Point2D>();



	public CalculadorDeCaminhoAG(ArrayList<Point2D> cidades, int populacao, double taxaMutacao, int geracoes) {
		this.cidades = cidades;
		this.populacao = populacao;
		this.taxaMutacao = taxaMutacao;
		this.geracoes = geracoes;

		genotipos = new int[populacao][cidades.size()];
		aptidoes = new double[populacao];
		melhorGenotipo = new int[cidades.size()];

		for(int i = 0; i < cidades.size(); i++) {
			melhorGenotipo[i] = i;
		}

		melhorDistancia = distancia(melhorGenotipo);

		gerarPopulacao();

	}

	public enum crossOver {
		PMX, OX;
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

	public static void changeEntry(int entry, int end,int[] gen, int[] g1, ArrayList<Integer> temp) {
		for(int i = entry; i < end; i++) {
			for(int j = 0; j < entry; j++) {
				if(gen[i] == gen[j]) {
					gen[j] = g1[i];
					if(temp.contains(g1[i])) {
						changeEntry(entry,end,gen,g1,temp);
					}
				}
			}
		}
	}

	public static void changeEnd(int entry, int end,int[] gen, int[] g1, ArrayList<Integer> temp) {
		for(int i = entry; i < end; i++) {
			for(int j = end; j < gen.length; j++) {
				if(gen[i] == gen[j]) {
					gen[j] = g1[i];
					if(temp.contains(g1[i])) {
						changeEnd(entry,end,gen,g1,temp);
					}
				}
			}
		}
	}

	public int[] crossOver(int[] g1, int[] g2, crossOver type) {

		int[] newGen = g1.clone();

		switch(type) {
		case PMX:

			int entry = StdRandom.uniform(g1.length);
			int end = StdRandom.uniform(entry, g1.length);

			ArrayList<Integer> temp = new ArrayList<Integer>();

			for(int i = entry; i < end; i++) {
				temp.add(g2[i]);
				newGen[i] = g2[i];
			}

			changeEntry(entry,end,newGen,g1,temp);

			changeEnd(entry,end,newGen,g1,temp);

			break;

		case OX:



			break;
		}


		return newGen;
	}


	public void novaGeracao() {

		if(geracaoAtual <= geracoes) {//Ordenar os genotipos dos melhores aos piores
			InsertionModified.sort(aptidoes, genotipos);
			int melhorMetade = genotipos.length/2;

			for(int i = melhorMetade; i < genotipos.length; i++) {

				//Escolher dois genotipos ranodm dos 50% dos melhores
				int[] g1 = genotipos[StdRandom.uniform(melhorMetade)];
				int[] g2 = genotipos[StdRandom.uniform(melhorMetade)];
				genotipos[i] = crossOver(g1, g2, crossOver.PMX);
			}

			for(int i = 0; i < populacao; i++) {
				mutar(genotipos[i]);
				aptidoes[i] = distancia(genotipos[i]);
			}
			geracaoAtual++;
		}
	}


	public int getGeracoes() {
		return geracoes;
	}

	public int getGeracaoAtual() {
		return geracaoAtual;
	}


	private void gerarPopulacao() {

		genotipos[0] = melhorGenotipo;
		aptidoes[0] = distancia(melhorGenotipo);
		for(int i = 1; i < populacao; i++) {
			int[] temp = melhorGenotipo.clone();
			shuffle(temp);
			aptidoes[i] = distancia(temp);
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
