import pandas as pd
import numpy as np
import math
from DataPreprocess import getStructureData
from RegresionMethod import *

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

def RMSLEevaluate(testLabels, predictLabels):
    if (len(testLabels) != len(predictLabels)):
        print('error for two labels array not equal length')
        return math.inf
    else:
        delta = np.log(testLabels+1) - np.log(predictLabels+1)
        return math.sqrt(np.mean(np.square(delta)))

if __name__ == '__main__':
    trainFilePath = 'train.csv'
    df1 = pd.read_csv(trainFilePath, encoding='utf-8')
    data, labels = getStructureData(df1)
    k = 5
    generates = splitKfold(data, labels, k) #split for 5-fold
    genitor = generates.__iter__()
    RMSLEs = np.zeros(k)
    for i in range(k):
        trainData, trainLabels, validateData, validateLabels = genitor.__next__()
        predictLabels = regressionMethod(trainData, trainLabels, validateData)
        RMSLEs[i] = RMSLEevaluate(validateLabels[:,2], predictLabels)
    print('K-fold RMSLE = ',RMSLEs, ' average RMSLE = ',np.mean(RMSLEs))

