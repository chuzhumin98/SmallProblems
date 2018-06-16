import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

# get hour data from time stamp
def getHour(stamp):
    time1 = stamp.split(' ')[-1]
    hour = int(time1.split(':')[0])
    return hour

# get for structured data and labels
def getStructureData(df1,trainData = True):
    size = len(df1['datetime'])
    data = np.zeros([size, 8]) #samples data
    for i in range(size):
        data[i,0] = getHour(df1['datetime'].loc[i])
    data[:,1] = df1['season']
    data[:,2] = df1['holiday']
    data[:,3] = df1['workingday']
    data[:,4] = df1['weather']
    outlierIndex = (df1['temp'] - df1['atemp'] > 10)
    data[:,5] = (df1['temp']+df1['atemp'])/2
    data[outlierIndex,5] = df1['temp'].loc[outlierIndex] # reset for outliers point
    data[:,6] = df1['humidity']
    data[:,7] = df1['windspeed']
    if (trainData):
        labels = np.zeros([size, 3])  # samples labels
        labels[:, 0] = df1['casual']
        labels[:, 1] = df1['registered']
        labels[:, 2] = df1['count']
        return [data, labels]
    else:
        return data

# get for one hot data which has been normalized
def getOneHotNormalizeData(data):
    size = len(data)
    newData = np.zeros([size, 34])
    for i in range(24):
        newData[data[:,0] == i, i] = 1 # the first 24 dim features shows time
    for i in range(24,28):
        newData[data[:,1] == i-23, i] = 1 # the 25-28 dim features shows season
    newData[:, 28:34] = data[:,2:8] # the next are the same for the raw data
    for i in range(30,34):
        minValue = min(newData[:,i])
        maxValue = max(newData[:,i])
        newData[:,i] = (newData[:,i]-minValue)/(maxValue-minValue)
    return newData

# get for normalized data
def getNormalizeData(data):
    size = len(data)
    feature = len(data[0])
    newData = np.zeros([size, feature])
    for i in range(feature):
        minValue = min(data[:, i])
        maxValue = max(data[:, i])
        newData[:, i] = (data[:, i] - minValue) / (maxValue - minValue)
    return newData

if __name__ == '__main__':
    trainFilePath = 'train.csv'
    df1 = pd.read_csv(trainFilePath, encoding='utf-8')

    # split to get hour
    size = len(df1['datetime'])
    hours = np.zeros(size)
    for i in range(size):
        hours[i] = getHour(df1['datetime'].loc[i])
    print(hours)

    data, labels = getStructureData(df1)
    oneHotData = getOneHotNormalizeData(data)

    # simply count for left area number
    leftCount = 0
    for i in range(size-1):
        if ((hours[i+1]-hours[i]+24) % 24 != 1):
            #print(hours[i],'-',hours[i+1])
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
    plt.legend(['weekend','workingday'], loc='upper left')
    plt.savefig('image/hour vs countv2.png',dpi=150)

    '''
    plt.figure(1)
    plt.scatter(df1['temp'], df1['count'],marker='.')
    plt.xlabel('temperature')
    plt.ylabel('count')
    plt.title('temp vs count')
    plt.xlim([0, 42])
    plt.ylim([0, 1000])
    plt.savefig('image/temp vs countv2.png',dpi=150)

    
    plt.figure(2)
    plt.scatter(df1['temp'],df1['atemp'], marker='x',c='b')
    plt.plot(range(42), range(42), c='r')
    plt.xlabel('temp')
    plt.ylabel('atemp')
    plt.title('temp vs atemp')
    plt.xlim([0, 42])
    plt.ylim([0, 48])
    plt.savefig('image/temp vs atempv2.png',dpi=150)
    '''