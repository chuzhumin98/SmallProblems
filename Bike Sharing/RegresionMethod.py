from sklearn.tree import DecisionTreeRegressor

# method：0-decision tree
def regressionMethod(trainData, trainLabels, testData, method=0, max_depth=None):
    if (method == 0):
        clf = DecisionTreeRegressor(max_depth=max_depth)
        clf.fit(trainData, trainLabels[:,2])
        return clf.predict(testData)
