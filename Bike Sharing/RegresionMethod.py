from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.linear_model import BayesianRidge
from sklearn.svm import LinearSVR
from MLPv1 import MLP
import numpy as np

# method：0-decision tree,1-Random Forest,2-Bayesian Ridge Regression
def regressionMethod(trainData, trainLabels, testData, method=0, isSplit=False, max_depth=9, n_estimators=30):
    if (method == 0):
        clf = DecisionTreeRegressor(max_depth=max_depth)
    elif (method == 1):
        clf = RandomForestRegressor(n_estimators=n_estimators, max_depth=max_depth)
    elif (method == 2):
        clf = BayesianRidge()
    elif (method == 3):
        clf = LinearSVR()
    elif (method == 4):
        if (not isSplit):
            prediction = MLP(trainData, np.log(trainLabels[:,2:3]+1), testData)
            return np.exp(prediction[:,0]) - 1
        else:
            casualPredict = MLP(trainData, np.log(trainLabels[:,0:1]+1), testData)
            casualPredict = np.exp(casualPredict[:,0])-1
            registerPredict = MLP(trainData, np.log(trainLabels[:,1:2]+1), testData)
            registerPredict = np.exp(registerPredict[:,0])-1
            return casualPredict + registerPredict

    if (not isSplit):
        clf.fit(trainData, trainLabels[:, 2])
        return clf.predict(testData)
    else:
        clf.fit(trainData, trainLabels[:, 0])
        casualPredict = clf.predict(testData)
        clf.fit(trainData, trainLabels[:, 1])
        registerPredict = clf.predict(testData)
        return casualPredict+registerPredict
