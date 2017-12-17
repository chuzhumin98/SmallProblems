[info] = textread('forsample2.txt');
clusterNum = 3;
colors = ['k^';'r^';'g^';'b^']; % 分别是未聚类点，第1-3类的颜色 
clusterColor = ['rs';'gs';'bs']; % 各聚类类别中心的点
[row, column] = size(info);
Kmax = row / 11;
for (k = 1:Kmax) 
    for (i = k*11-10:k*11-3)
        type = info(i,3) + 2;
        if (k == 1) 
            type = 1;
        end
        if (type == 1) 
            scatter(info(i,1),info(i,2),80,colors(type,:));
        else
            scatter(info(i,1),info(i,2), 80,colors(type,:),'filled');
        end
        hold on
    end
    for (i = k*11-2:k*11)
        type = i-k*11+3;
        scatter(info(i,1),info(i,2),70,clusterColor(type,:),'filled');
        hold on
    end
    xlim([1.5,10.5])
    ylim([0.5,9.5])
    xlabel('X')
    ylabel('Y')
    titles = strcat('关于网络学堂上数据的聚类过程',num2str(k));
    title(titles)
    box on
    file = strcat('img/sample2Process=',num2str(k),'.png');
    saveas(gcf,file);
    figure
end
