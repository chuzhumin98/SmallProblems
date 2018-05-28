from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor

# method：0-decision tree
def regressionMethod(trainData, trainLabels, testData, method=0, max_depth=None,n_estimators=10):
    if (method == 0):
        clf = DecisionTreeRegressor(max_depth=max_depth)
    elif (method == 1):
        clf = RandomForestRegressor(n_estimators=n_estimators, max_depth=max_depth)
    clf.fit(trainData, trainLabels[:, 2])
    return clf.predict(testData)
