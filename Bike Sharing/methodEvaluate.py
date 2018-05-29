import pandas as pd
import numpy as np
import math
from DataPreprocess import getStructureData
from DataPreprocess import getNormalizeData
from DataPreprocess import getOneHotNormalizeData
from RegresionMethod import *
import matplotlib.pyplot as plt

# split the dataset and labels to k-fold
def splitKfold(data, labels, k):
    size = len(data) # the total data set
    indexArray = np.array(range(size), dtype=int)  # 下标数组
    np.random.shuffle(indexArray)
    for i in range(k):
        low = size * i // k
        high = size * (i+1) // k
        trainData = np.row_stack((data[:low, :], data[high:,:]))
        trainLabels = np.row_stack((labels[:low,:], labels[high:,:]))
        validateData = data[low:high,:]
        validateLabels = labels[low:high,:]
        yield [trainData, trainLabels, validateData, validateLabels]

# evaluate predict model using RMSLE
def RMSLEevaluate(testLabels, predictLabels):
    if (len(testLabels) != len(predictLabels)):
        print('error for two labels array not equal length')
        return math.inf
    else:
        delta = np.log(testLabels+1) - np.log(predictLabels+1)
        return math.sqrt(np.mean(np.square(delta)))

# change the parameter max_depth of the decision tree
def selectDecisionTreeDepth(data, labels, k):
    depths = range(1,21)
    meanRMSLEs = np.zeros(len(depths))
    meanTrainRMSLEs = np.zeros(len(depths))
    stdRMSLEs = np.zeros(len(depths))
    for max_depth in depths:
        # split data for train and validate part: k-fold strategy
        generates = splitKfold(data, labels, k)  # split for 5-fold
        genitor = generates.__iter__()
        RMSLEs = np.zeros(k)
        trainRMSLEs = np.zeros(k)
        # k-fold evaluate model
        for i in range(k):
            trainData, trainLabels, validateData, validateLabels = genitor.__next__()
            predictLabels = regressionMethod(trainData, trainLabels, validateData, max_depth=max_depth)
            RMSLEs[i] = RMSLEevaluate(validateLabels[:, 2], predictLabels)
            predictTrainLabels = regressionMethod(trainData, trainLabels, trainData, max_depth=max_depth)
            trainRMSLEs[i] = RMSLEevaluate(trainLabels[:, 2], predictTrainLabels)
        print('max_depth # ', max_depth, 'average RMSLE = ', np.mean(RMSLEs),' variance = ',np.std(RMSLEs))
        meanRMSLEs[max_depth-1] = np.mean(RMSLEs)
        meanTrainRMSLEs[max_depth-1] = np.mean(trainRMSLEs)
        stdRMSLEs[max_depth-1] = np.std(RMSLEs)
    plt.figure(0)
    plt.plot(depths, meanRMSLEs, marker='.', c='b',linewidth=1.5, mfc='r',ms=10)
    plt.plot(depths, meanTrainRMSLEs, marker='.', c='g', ls='--', lw=1.5, mfc='r', ms=10)
    plt.title('max_depth vs RMSLE')
    plt.xlabel('max_depth')
    plt.ylabel('mean of RMSLE')
    plt.legend(['validate set', 'train set'])
    plt.grid()
    plt.savefig('image/DT_depth vs RMSLEv2.png', dpi=150)

    plt.figure(1)
    plt.plot(depths, stdRMSLEs, marker='.', c='b',linewidth=1.5, mfc='r',ms=10)
    plt.title('max_depth vs std(RMSLE)')
    plt.xlabel('max_depth')
    plt.ylabel('std of RMSLE')
    plt.savefig('image/DT_depth vs std_RMSLEv2.png', dpi=150)


def selectRandomForestN(data, labels, k, max_depth=None):
    Ns = range(1,51)
    meanRMSLEs = np.zeros(len(Ns))
    stdRMSLEs = np.zeros(len(Ns))
    for n in Ns:
        # split data for train and validate part: k-fold strategy
        generates = splitKfold(data, labels, k)  # split for 5-fold
        genitor = generates.__iter__()
        RMSLEs = np.zeros(k)
        # k-fold evaluate model
        for i in range(k):
            trainData, trainLabels, validateData, validateLabels = genitor.__next__()
            predictLabels = regressionMethod(trainData, trainLabels, validateData, method=1, max_depth=max_depth, n_estimators=n)
            RMSLEs[i] = RMSLEevaluate(validateLabels[:, 2], predictLabels)
        print('n # ', n, 'average RMSLE = ', np.mean(RMSLEs),' variance = ',np.std(RMSLEs))
        meanRMSLEs[n - 1] = np.mean(RMSLEs)
        stdRMSLEs[n - 1] = np.std(RMSLEs)
    plt.figure(2)
    plt.plot(Ns, meanRMSLEs, marker='.', c='b',linewidth=1.5, mfc='r',ms=10)
    plt.title('tree number vs RMSLE')
    plt.xlabel('tree number')
    plt.ylabel('mean of RMSLE')
    plt.grid()
    plt.savefig('image/RF_number vs RMSLE.png', dpi=150)

    plt.figure(3)
    plt.plot(Ns, stdRMSLEs, marker='.', c='b',linewidth=1.5, mfc='r',ms=10)
    plt.title('tree number vs std(RMSLE)')
    plt.xlabel('tree number')
    plt.ylabel('std of RMSLE')
    plt.savefig('image/RF_number vs std_RMSLE.png', dpi=150)



if __name__ == '__main__':
    # Load and preprocess for train data
    trainFilePath = 'train.csv'
    df1 = pd.read_csv(trainFilePath, encoding='utf-8')
    data, labels = getStructureData(df1)
    data = getOneHotNormalizeData(data)

    #selectDecisionTreeDepth(data, labels, 5)
    #selectRandomForestN(data, labels, 5, 9)

    #split data for train and validate part: k-fold strategy
    k = 5
    generates = splitKfold(data, labels, k) #split for 5-fold
    genitor = generates.__iter__()
    RMSLEs = np.zeros(k)
    # k-fold evaluate model
    for i in range(k):
        trainData, trainLabels, validateData, validateLabels = genitor.__next__()
        predictLabels = regressionMethod(trainData, trainLabels, validateData, isSplit=True, method=3) # max_depth = 9 is best
        predictLabels = np.maximum(predictLabels, 0)
        RMSLEs[i] = RMSLEevaluate(validateLabels[:,2], predictLabels)
    print('K-fold RMSLE = ',RMSLEs, ' average RMSLE = ',np.mean(RMSLEs),' std = ',np.std(RMSLEs))
