
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.lang.StringBuilder;

import java.util.StringTokenizer;

public class ProcessText { 

	public static void main(String[] args) throws IOException {
        
        String input = read("Tenet");

		unigram(input, "unigram");

		bigram(input, "bigram");

		bigramLaplace(input, "bigramLaplace");

		trigram(input, "trigram");

		sentences(input, "sentences");

        String fake = read("script");

        unigram(fake, "fake_unigram");

        posterior(input, fake, "posterior");

        evaluate(input, fake, "output/sentences", "evaluation");
	}

    public static String read(String file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("scripts/" + file + ".txt"));

		StringBuilder sb = new StringBuilder();

		String input, temp;
		StringTokenizer st;
		while( (input = br.readLine()) != null ) {

			st = new StringTokenizer(clean(input.toLowerCase()));

			while(st.hasMoreTokens()) {
				temp = st.nextToken();
				if(!temp.equals("")) {
					sb.append(temp).append(' ');
				}
			}

		}
        br.close();

		return sb.toString();
    }

	public static double[] calcUnigram(String input) {

		double[] count = new double[27];
		for(int i = 0; i < input.length(); i++) {
			count[charToIndex(input.charAt(i))]++;
		}

		for(int i = 0; i < count.length; i++) {
			count[i] /= (double) input.length();
		}

		return count;
	}

	public static void unigram(String input, String output) throws IOException {

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + output + ".txt")));

		double[] count = calcUnigram(input);

		for(double i : count) {
			out.print(i);
			out.print(',');
		}

		out.close();

	}

	public static double[][] calcBigram(String input) {

		double[][] count = new double[27][27];
		char prev = input.charAt(input.length() - 1);
		char next;
		for(int i = 0; i < input.length(); i++) {
			next = input.charAt(i);
			
			count[charToIndex(prev)][charToIndex(next)]++;

			prev = next;
		}

		int total;
		for(int r = 0; r < count.length; r++) {

			total = 0;
			for(double c : count[r]) {
				total += c;
			}

			for(int c = 0; c < count[r].length; c++) {
				count[r][c] /= total;
			}
		}

		return count;
	}

	public static void bigram(String input, String output) throws IOException {

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + output + ".txt")));

		double[][] count = calcBigram(input);

		for(double[] r : count) {
			for(double c : r) {
				out.print(c);
				out.print(',');
			}
			out.println();
		}

		out.close();

	}

	public static double[][] calcBigramLaplace(String input) {

		double[][] count = new double[27][27];
		for(int r = 0; r < count.length; r++) {
			for(int c = 0; c < count[r].length; c++) {
				count[r][c] = 1;
			}
		}

		char prev = input.charAt(input.length() - 1);
		char next;
		for(int i = 0; i < input.length(); i++) {
			next = input.charAt(i);
			
			count[charToIndex(prev)][charToIndex(next)]++;

			prev = next;
		}

		int total;
		for(int r = 0; r < count.length; r++) {

			total = 0;
			for(double c : count[r]) {
				total += c;
			}

			for(int c = 0; c < count[r].length; c++) {
				count[r][c] /= total;
			}
		}

		return count;
	}

	public static void bigramLaplace(String input, String output) throws IOException {

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + output + ".txt")));

		double[][] count = calcBigramLaplace(input);

		for(double[] r : count) {
			for(double c : r) {
				out.print(c);
				out.print(',');
			}
			out.println();
		}

		out.close();

	}

	public static double[][][] calcTrigram(String input) {

		double[][][] count = new double[27][27][27];

		char fir = input.charAt(input.length() - 2);
		char sec = input.charAt(input.length() - 1);
		char next;
		for(int i = 0; i < input.length(); i++) {
			next = input.charAt(i);
			
			count[charToIndex(fir)][charToIndex(sec)][charToIndex(next)]++;

			fir = sec;
			sec = next;
		}

		int total;
		for(int f = 0; f < count.length; f++) {
			for(int s = 0; s < count[f].length; s++) {

				total = 0;
				for(double c : count[f][s]) {
					total += c;
				}

				total = Math.max(total, 1);
				for(int c = 0; c < count[f][s].length; c++) {
					count[f][s][c] /= total;
				}
			}
		}

		return count;
	}

	public static void trigram(String input, String output) throws IOException {

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + output + ".txt")));

		double[][][] count = calcTrigram(input);

		for(double[][] f : count) {
			for(double[] s : f) {
				for(double c : s) {
					out.print(c);
					out.print(',');
				}
				out.println();
			}
			out.println();
		}

		out.close();

	}

	public static String generate(char start, double[][] bigram, double[][][] trigram) {
		StringBuilder sb = new StringBuilder();

		char fir = start;
		char sec = pick(bigram[charToIndex(fir)]);
		sb.append(fir).append(sec);

		char temp;
		for(int i = 0; i < 1000 - 2; i++) {
			try {
				temp = pick(trigram[charToIndex(fir)][charToIndex(sec)]);
			} catch(RuntimeException e) {
				temp = pick(bigram[charToIndex(sec)]);
			}

			sb.append(temp);

			fir = sec;
			sec = temp;
		}

		return sb.toString();
	}

	public static void sentences(String input, String output) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + output + ".txt")));

		double[][] bigram = calcBigramLaplace(input);
		double[][][] trigram = calcTrigram(input);
		for(char c = 'a'; c <= 'z'; c++) {
			out.println(generate(c, bigram, trigram));
		}

		out.close();
	}

    public static double[] calcPosterior(String input, String alt) throws IOException {
		double[] real = calcUnigram(input);
		double[] fake = calcUnigram(alt);

        double[] dist = new double[ Math.min( real.length, fake.length ) ];
        for(int i = 0; i < dist.length; i++) {
            dist[i] = fake[i] * 0.27 / (fake[i] * 0.27 + real[i] * 0.73);
        }
        return dist;
    }

	public static void posterior(String input, String alt, String output) throws IOException {

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + output + ".txt")));

        double[] count = calcPosterior(input, alt);

		for(double i : count) {
			out.print(i);
			out.print(',');
		}

		out.close();

	}

    public static void evaluate(String input, String alt, String test, String output) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(test + ".txt"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + output + ".txt")));

        double[] dist = calcPosterior(input, alt);

        String sentence;
        double fake, real;
        while( (sentence = br.readLine()) != null ) {
            fake = 0;
            real = 0;

            for(int i = 0; i < sentence.length(); i++) {
                fake += Math.log(dist[charToIndex(sentence.charAt(i))]);
                fake += Math.log(1 - dist[charToIndex(sentence.charAt(i))]);
            }

            out.print(fake - real > 0 ? '1' : '0');
            out.print(',');
        }

        br.close();
        out.close();
    }

	public static char pick(double[] dist) {
		double rand = Math.random();
		for(int i = 0; i < dist.length; i++) {
			rand -= dist[i];
			if(rand < 0) {

				if(i == 0) {
					return ' ';
				}
				return (char) ('a' + (i - 1));

			}
		}

		throw new RuntimeException("Invalid distribution");

	}

	public static char pickBackup(double[] dist) {
		double val = -1;
		int index = 0;
		for(int i = 0; i < dist.length; i++) {
			if(dist[i] > val) {
				val = dist[i];
				index = i;
			}
		}

		if(val == 0) {
			throw new RuntimeException("Invalid distribution");
		}
		
		if(index == 0) {
			return ' ';
		}

		if(1 <= index && index <= 26) {
			return (char) ('a' + (index - 1));
		}

		throw new RuntimeException("Invalid index");
	}

	public static int charToIndex(char c) {
		int i = -1;

		if(c == ' ') {
			i = 0;
		}

		if('a' <= c && c <= 'z') {
			i = (int) (c - 'a') + 1;
		}

		return i;
	}

	public static String clean(String text) {

		StringBuilder sb = new StringBuilder();

		char c;
		for(int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if('a' <= c && c <= 'z') {
				sb.append(c);
			} else {
				sb.append(' ');
			}
		}

		return sb.toString();

	}

}

