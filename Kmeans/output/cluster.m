[info] = textread('forsample1.txt');
clusterNum = 3;
size = 9;
colors = ['k^';'r^';'g^';'b^']; % 分别是未聚类点，第1-3类的颜色 
clusterColor = ['rs';'gs';'bs']; % 各聚类类别中心的点
for (k = 1:1) 
    for (i = 1:9)
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
    for (i = 10:12)
        scatter(info(i,1),info(i,2),70,clusterColor(i - 9,:),'filled');
        hold on
    end
end
xlim([0.5,5.5])
ylim([0.5,5.5])
xlabel('X')
ylabel('Y')
title('关于PPT上数据的聚类过程图')