[info] = textread('forsample1.txt');
clusterNum = 3;
colors = ['k^';'r^';'g^';'b^']; % 分别是未聚类点，第1-3类的颜色 
clusterColor = ['rs';'gs';'bs']; % 各聚类类别中心的点
[row, column] = size(info);
Kmax = row / 12;
for (k = 1:Kmax) 
    for (i = k*12-11:k*12-3)
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
    for (i = k*12-2:k*12)
        type = i-k*12+3;
        scatter(info(i,1),info(i,2),70,clusterColor(type,:),'filled');
        hold on
    end
    xlim([0.5,5.5])
    ylim([0.5,5.5])
    xlabel('X')
    ylabel('Y')
    titles = strcat('关于PPT上数据的聚类过程',num2str(k));
    title(titles)
    box on
    file = strcat('img/sample1Process=',num2str(k),'.png');
    saveas(gcf,file);
    figure
end
