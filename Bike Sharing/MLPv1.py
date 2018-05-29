import tensorflow as tf
import numpy as np
import pandas as pd
from DataPreprocess import getStructureData
from DataPreprocess import getOneHotNormalizeData
from methodEvaluate import splitKfold



# 增加一层
def addLayer(inputs, inSize, outSize, activateFunction=None):
    # 增加一层并返回该层的输出
    Weights = tf.Variable(tf.random_normal([inSize, outSize]))
    biases = tf.Variable(tf.zeros([1, outSize]))
    Wx_plus_b = tf.matmul(inputs, Weights) + biases
    if activateFunction is None:
        outputs = Wx_plus_b
    else:
        outputs = activateFunction(Wx_plus_b)
    return outputs

# 选取下一个batch，按照batchSize来选取,返回[trainData, trainLabels]大小为batchSize
def nextBatch(trainData, trainLabels, batchSize):
    size = len(trainLabels)
    slices = size // batchSize #一轮的多少
    while True:
        indexArray = np.array(range(size), dtype=int)  # 下标数组
        np.random.shuffle(indexArray) # shuffle一下index
        for i in range(slices):
            low = i * batchSize
            high = (i+1) * batchSize
            yield [trainData[indexArray[low:high]], trainLabels[indexArray[low:high]]] # 返回一个batch

# 多层感知机
def MLP(trainData, trainLabels, validateData, validateLabels=None):
    featureNum = len(trainData[0])
    # 输入的变量们
    x = tf.placeholder(tf.float32, [None, featureNum])  # 输入数据占位符
    y = tf.placeholder(tf.float32, [None, 1])  # 占位符，实际样本标签

    # 两个隐含层和输出层
    layer1 = addLayer(x, featureNum, 50, activateFunction=tf.nn.relu)
    layer2 = addLayer(layer1, 50, 20, activateFunction=tf.nn.relu)
    yhat = addLayer(layer2, 20, 1, activateFunction=None)

    # 计算最后一层是softmax层的cross entropy
    loss = tf.reduce_mean(tf.square(yhat-y))

    # 设置优化算法及学习率
    boundaries = [15000, 30000, 45000, 65000, 80000]
    learing_rates = [0.001, 0.0005, 0.0002, 0.0001, 0.00005, 0.00002]
    iterNum = tf.placeholder(tf.int32)
    learing_rate = tf.train.piecewise_constant(iterNum, boundaries=boundaries, values=learing_rates)  # 学习率阶梯状下降
    trainStep = tf.train.AdamOptimizer(learning_rate=0.001).minimize(loss)  # 采用Adam优化
    init = tf.global_variables_initializer()  # 变量的初始化

    batchGen = nextBatch(trainData, trainLabels, 50)  # 选取50的batch的生成器
    batchGenitor = batchGen.__iter__()

    with tf.Session() as sess:
        sess.run(init)
        for i in range(100000):
            batchData, batchLabels = batchGenitor.__next__()  # 生成一个batch
            if (i + 1) % 1000 == 0:
                lossNow = sess.run(loss, feed_dict={x: trainData, y: trainLabels})  # 观测不得影响模型
                print('step #', i + 1, ' train RMSLE = ', np.sqrt(lossNow))
                if (not validateLabels is None):
                    validateLoss = sess.run(loss, feed_dict={x:validateData, y:validateLabels})
                    print('step #', i + 1, ' validate RMSLE = ', np.sqrt(validateLoss))
            sess.run(trainStep, feed_dict={x: batchData, y: batchLabels, iterNum: i})
        if (validateLabels is None):
            yhats = sess.run(yhat, feed_dict={x: validateData})
            return yhats

if __name__ == '__main__':
    # Load and preprocess for train and test data
    trainFilePath = 'train.csv'
    df1 = pd.read_csv(trainFilePath, encoding='utf-8')
    trainData, trainLabels = getStructureData(df1)
    trainData = getOneHotNormalizeData(trainData)

    # split data for train and validate part: k-fold strategy
    k = 5
    generates = splitKfold(trainData, trainLabels, k)  # split for 5-fold
    genitor = generates.__iter__()
    RMSLEs = np.zeros(k)
    # k-fold evaluate model
    for i in range(1):
        trainData, trainLabels, validateData, validateLabels = genitor.__next__()
        MLP(trainData, np.log(trainLabels[:, 2:3] + 1), validateData, np.log(validateLabels[:, 2:3] + 1))

    #MLP(trainData, np.log(trainLabels[:,2:3]+1), trainData)