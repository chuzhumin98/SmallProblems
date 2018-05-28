from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor

# method：0-decision tree
def regressionMethod(trainData, trainLabels, testData, method=0, isSplit=False, max_depth=9, n_estimators=30):
    if (method == 0):
        clf = DecisionTreeRegressor(max_depth=max_depth)
    elif (method == 1):
        clf = RandomForestRegressor(n_estimators=n_estimators, max_depth=max_depth)
    if (not isSplit):
        clf.fit(trainData, trainLabels[:, 2])
        return clf.predict(testData)
    else:
        clf.fit(trainData, trainLabels[:, 0])
        casualPredict = clf.predict(testData)
        clf.fit(trainData, trainLabels[:, 1])
        registerPredict = clf.predict(testData)
        return casualPredict+registerPredict
