[info] = textread('forsample2.txt');
clusterNum = 3;
colors = ['k^';'r^';'g^';'b^']; % �ֱ���δ����㣬��1-3�����ɫ 
clusterColor = ['rs';'gs';'bs']; % ������������ĵĵ�
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
    titles = strcat('��������ѧ�������ݵľ������',num2str(k));
    title(titles)
    box on
    file = strcat('img/sample2Process=',num2str(k),'.png');
    saveas(gcf,file);
    figure
end
