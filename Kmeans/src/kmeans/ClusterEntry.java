package kmeans;

public class ClusterEntry {
	public double X; //该实体的横坐标
	public double Y; //纵坐标
	int cluster; //所属类别信息
	
	public ClusterEntry(double X, double Y) {
		this.X = X;
		this.Y = Y;
		this.cluster = -1;
	}
	
	public ClusterEntry() {
		this.X = 0;
		this.Y = 0;
		this.cluster = -1;
	}
	
	public void setPosition(double X, double Y) {
		this.X = X;
		this.Y = Y;
	}
	
	/*
	 * 获取和另一个对象的距离
	 */
	public double getDistance(ClusterEntry other) {
		double distance = (X - other.X) * (X - other.X) + (Y - other.Y) * (Y - other.Y);
		distance = Math.sqrt(distance);
		return distance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 重写了toString方法
	 */
	public String toString() {
		String string = "X:"+X;
		string += " Y:"+Y;
		string += " cluster:"+this.cluster;
		return string;
	}
	
	/*
	 * 返回一个串，没有任何解释信息的，便于将结果输出到文件中
	 */
	public String toSimpleString() {
		String string = X+" "+Y+" "+this.cluster;
		return string;
	}
}
