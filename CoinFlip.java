import java.io.*;
import java.util.*;

public class CoinFlip {
	public static void main(String[] args) throws IOException{
		int numthreads=0;
		int numflips=0;
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("How many threads do you want to use?");
		numthreads=Integer.parseInt(br.readLine());
		System.out.println("How many coin flips do you want to happen?");
		numflips=Integer.parseInt(br.readLine());
		int numTossesPerThread=numflips/numthreads;
		int numTossesLeftover=numflips-numTossesPerThread*numthreads;
		int workload=0;
		Thread[] threads=new Thread[numthreads];	
		CoinData data =new CoinData();		
		long start=System.currentTimeMillis();
		for(int i=0; i<numthreads; i++) {
			if(i==numthreads-1) { workload=numTossesPerThread+numTossesLeftover;}
			else {workload=numTossesPerThread;}
			threads[i]=new Thread(new Flip(data, workload));
			threads[i].start();
		}
		for(int i=0; i<numthreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long end=System.currentTimeMillis();
		long timeElapsed=end-start;
		System.out.println("Elapsed Time: "+timeElapsed+" ms");
		System.out.println("Number of Heads: "+data.numHeads());
	}
}

class Flip implements Runnable {
	int iterationsToRun=0;
	CoinData data = null;

	public Flip(CoinData d, int n) {
		data=d;
		iterationsToRun=n;
	}

	public void run() {
		int numHeads=0;
		int numTails=0;
		Random random=new Random();
		int randomInt=0;
		for(int i=0; i<iterationsToRun; i++) {
			randomInt=random.nextInt(2);
			if(randomInt==0) {numHeads++;}
			else {numTails++;}
		}
		data.addHeads(numHeads);
		data.addTails(numTails);
	}
}

class CoinData {
	int numheads=0;
	int numtails=0;
	public synchronized void addHeads(int n) {
		numheads=numheads+n;
	}
	public synchronized void addTails(int n) {
		numtails=numtails+n;
	}
	public int numHeads() {return numheads;}
	public int numTails() {return numtails;}
}
