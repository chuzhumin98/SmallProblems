import pandas as pd
import numpy as np
from DataPreprocess import getStructureData

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


if __name__ == '__main__':
    trainFilePath = 'train.csv'
    df1 = pd.read_csv(trainFilePath, encoding='utf-8')
    data, labels = getStructureData(df1)
    k = 5
    generates = splitKfold(data, labels, k) #split for 5-fold
    genitor = generates.__iter__()
    for i in range(k):
        trainData, trainLabels, validateData, validateLabels = genitor.__next__()
        print(len(trainLabels))
        print(len(validateLabels))

