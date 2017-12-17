package kmeans;

import java.io.File;
import java.io.IOException;
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
	
	public static void main(String[] args) {
		Kmeans km = new Kmeans(3);
		km.importData("import/sample1.txt");
	}
}
