import pandas as pd
import numpy as np
import math
from DataPreprocess import getStructureData
from DataPreprocess import getOneHotNormalizeData
from RegresionMethod import *

if __name__ == '__main__':
    # Load and preprocess for train and test data
    trainFilePath = 'train.csv'
    df1 = pd.read_csv(trainFilePath, encoding='utf-8')
    trainData, trainLabels = getStructureData(df1)
    testFilePath = 'test.csv'
    df2 = pd.read_csv(testFilePath, encoding='utf-8')
    testData = getStructureData(df2, False)

    trainData = getOneHotNormalizeData(trainData)
    testData = getOneHotNormalizeData(testData)

    predictLabels = regressionMethod(trainData, trainLabels, testData, isSplit=False, method=4, max_depth=9, n_estimators=30)
    predictLabels = np.maximum(predictLabels, 0)
    predictFrame = pd.DataFrame(np.transpose([df2['datetime'], predictLabels]),
                                columns=['datetime', 'count'])
    predictFrame.to_csv('results/MLP_v1.csv', sep=',', index=None)
