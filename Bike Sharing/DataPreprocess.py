import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# get hour data from time stamp
def getHour(stamp):
    time1 = stamp.split(' ')[-1]
    hour = int(time1.split(':')[0])
    return hour


if __name__ == '__main__':
    trainFilePath = 'train.csv'
    df1 = pd.read_csv(trainFilePath, encoding='utf-8')
    size = len(df1['datetime'])
    hours = np.zeros(size)
    for i in range(size):
        hours[i] = getHour(df1['datetime'].loc[i])
    print(hours)
    leftCount = 0
    for i in range(size-1):
        if ((hours[i+1]-hours[i]+24) % 24 != 1):
            print(hours[i],'-',hours[i+1])
            leftCount += 1
    print(leftCount)



    plt.figure(0)
    plt.scatter(hours[df1['workingday'] == 0], df1['count'].loc[df1['workingday'] == 0], marker='x',c='r')
    plt.scatter(hours[df1['workingday'] == 1], df1['count'].loc[df1['workingday'] == 1], marker='x', c='g')
    plt.xlim([0, 23])
    plt.ylim([0, 1000])
    plt.xlabel('hour')
    plt.ylabel('count')
    plt.title('hour vs count')
    plt.savefig('image/hour vs count.png',dpi=150)

    plt.figure(1)
    plt.scatter(df1['temp'], df1['count'],marker='.')
    plt.xlabel('temperature')
    plt.ylabel('count')
    plt.title('temp vs count')
    plt.xlim([0, 42])
    plt.ylim([0, 1000])
    plt.savefig('image/temp vs count.png',dpi=150)

    plt.figure(2)
    plt.scatter(df1['temp'],df1['atemp'], marker='x',c='b')
    plt.plot(range(42), range(42), c='r')
    plt.xlabel('temp')
    plt.ylabel('atemp')
    plt.title('temp vs atemp')
    plt.xlim([0, 42])
    plt.ylim([0, 48])
    plt.savefig('image/temp vs atemp.png',dpi=150)
