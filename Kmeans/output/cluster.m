[info] = textread('forsample1.txt');
clusterNum = 3;
size = 9;
colors = ['k^';'r^';'g^';'b^']; % �ֱ���δ����㣬��1-3�����ɫ 
clusterColor = ['rs';'gs';'bs']; % ������������ĵĵ�
for (k = 1:1) 
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
        scatter(info(i,1),info(i,2),70,clusterColor(i - 9,:),'filled');
        hold on
    end
    xlim([0.5,5.5])
    ylim([0.5,5.5])
    xlabel('X')
    ylabel('Y')
    titles = strcat('����PPT�����ݵľ������',num2str(k));
    title(titles)
    box on
    file = strcat('img/sample1Process=',num2str(k),'.png');
    saveas(gcf,file);
end
