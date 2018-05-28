from sklearn.tree import DecisionTreeRegressor

# method：0-decision tree
def regressionMethod(trainData, trainLabels, testData, method=0):
    if (method == 0):
        clf = DecisionTreeRegressor()
        clf.fit(trainData, trainLabels[:,2])
        return clf.predict(testData)
