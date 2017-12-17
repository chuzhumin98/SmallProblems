package kmeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


public class Kmeans {
	private ArrayList<ClusterEntry> samples = new ArrayList<ClusterEntry>();
												//记录所有需要聚类的样本点信息
	private ClusterEntry[] clusters; //记录各个聚类类别的信息
	private int clusterNum; //聚类的类别个数
	
	public Kmeans(int cluster) {
		this.clusterNum = cluster;
		this.clusters = new ClusterEntry [this.clusterNum];
		for (int i = 0; i < this.clusterNum; i++) {
			this.clusters[i] = new ClusterEntry();
		}
	}
	
	/*
	 * 导入测试数据，格式：
	 * 每行三个元素，前两个为位置信息，最后一个为初始类别信息(各类有且仅有一个)
	 * 类别为0~C-1，其他不在类别中的为-1
	 */
	public void importData(String path) {
		try {
			Scanner input = new Scanner(new File(path));
			while (input.hasNextLine()) {
				String line = input.nextLine();
				String[] splits = line.split(" ");
				//System.out.println(splits.length);
				double X = Double.parseDouble(splits[0]);
				double Y = Double.parseDouble(splits[1]);
				int clusterInfo = Integer.parseInt(splits[2]);
				ClusterEntry myEntry = new ClusterEntry(X, Y);
				if (clusterInfo >= 0 && clusterInfo < this.clusterNum) {
					this.clusters[clusterInfo].setPosition(X, Y);
					myEntry.cluster = clusterInfo;
				}
				this.samples.add(myEntry);
			}
			System.out.println("sample size: " + this.samples.size());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 进行K-means操作,并将计算过程写入文件中
	 */
	public void doKmeans(String path) throws FileNotFoundException {
		PrintStream out = new PrintStream(new File(path));
		int count = 1; //进行的迭代次数
		System.out.println("center of process "+count);
		this.queryClusterInfo();
		this.outputProceed(out); 
		while (true) {
			boolean hasChanged = false; //记录该轮聚类中是否发生变化
			/*
			 * 重新判断分类
			 */
			for (ClusterEntry item: this.samples) {
				double minDist = Double.MAX_VALUE;
				int minIndex = 0;
				for (int i = 0; i < this.clusterNum; i++) {
					double myDist = item.getDistance(this.clusters[i]);
					if (myDist < minDist) {
						minDist = myDist;
						minIndex = i;
					}
				}
				if (item.cluster != minIndex) {
					hasChanged = true;
				}
				item.cluster = minIndex;
			}
			/*
			 * 重新计算各聚类中心
			 */
			for (int i = 0; i < this.clusterNum; i++) {
				this.clusters[i].X = 0;
				this.clusters[i].Y = 0;
				int countNum = 0; //记录有多少元素在此类
				for (ClusterEntry item: this.samples) {
					if (item.cluster == i) {
						this.clusters[i].X += item.X;
						this.clusters[i].Y += item.Y;
						countNum++;
					}
				}
				if (countNum == 0) {
					countNum++; //避免发生除0的情况
				}
				this.clusters[i].X /= (double)countNum;
				this.clusters[i].Y /= (double)countNum;
			}
			count++;
			System.out.println("center of process "+count);
			this.queryClusterInfo();
			this.outputProceed(out); //并将这次结果输出到文件中
			
			/*
			 * 观察是否满足判停条件
			 */
			if (!hasChanged) {
				break;
			}
			
		}
	}
	
	/*
	 * 获取现在各类别的信息 
	 */
	public void queryClusterInfo() {
		System.out.println("following are the center of each cluster:");
		for (int i = 0; i < this.clusterNum; i++) {
			String string = "cluster "+i+": ";
			string += "("+this.clusters[i].X+","+this.clusters[i].Y+")";
			System.out.println(string);
		}
	}
	
	/*
	 * 将聚类的过程输出到文件中
	 */
	public void outputProceed(PrintStream out) {
		for (ClusterEntry item: this.samples) {
			out.println(item.toSimpleString());
		}
		for (int i = 0; i < this.clusterNum; i++) {
			out.println(this.clusters[i].toSimpleString());
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Kmeans km = new Kmeans(3);
		km.importData("import/sample2.txt");
		km.doKmeans("output/forsample2.txt");
	}
}
