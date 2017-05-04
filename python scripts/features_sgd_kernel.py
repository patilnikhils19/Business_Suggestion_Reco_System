# citation http://scikit-learn.org/stable/auto_examples/model_selection/plot_confusion_matrix.html

import matplotlib.pyplot as plt
import numpy as np
from sklearn import datasets, linear_model, svm
from sklearn import preprocessing, metrics
from sklearn.linear_model import SGDClassifier
import pickle
from sklearn.externals import joblib

from sklearn.kernel_approximation import RBFSampler
from sklearn.linear_model import SGDClassifier

np.set_printoptions(precision=2)

import itertools
import matplotlib.pyplot as plt
from sklearn.metrics import confusion_matrix

n_features = 31
split_index_train = 100000
split_index_test = 20000
class_names = [2,3,4,5,6,7,8,9,10]


def plot_confusion_matrix(cm, classes,
                          normalize=False,
                          title='Confusion matrix',
                          cmap=plt.cm.Blues):
    """
    This function prints and plots the confusion matrix.
    Normalization can be applied by setting `normalize=True`.
    """
    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()
    tick_marks = np.arange(len(classes))
    plt.xticks(tick_marks, classes, rotation=45)
    plt.yticks(tick_marks, classes)

    if normalize:
        cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]
        print("Normalized confusion matrix")
    else:
        print('Confusion matrix, without normalization')

    print(cm)

    thresh = cm.max() / 2.
    for i, j in itertools.product(range(cm.shape[0]), range(cm.shape[1])):
        plt.text(j, i, cm[i, j],
                 horizontalalignment="center",
                 color="white" if cm[i, j] > thresh else "black")

    plt.tight_layout()
    plt.ylabel('True label')
    plt.xlabel('Predicted label')


def round_of_array(arr):
    for i, val in enumerate(arr):
        arr[i] = round(val * 2) / 2
    return arr

# Load Data ------------------------------------------------------------------
X_total = []

# X_total = np.array(list(csv.reader(open('yelp_counts.tsv'), delimiter='\t')))
with open('attributeXy.csv', 'rb') as csvfile:
    for row in csvfile:
        row = row.strip().split(',')
        row_new = [row[0], float(row[1]), int(row[2]), int(row[3]), int(row[4]), int(row[5]), int(row[6]), int(row[7]), int(row[8]), int(row[9]), int(row[10]), int(row[11]), int(row[12]), int(row[13]), int(row[14]), int(row[15]), int(row[16]), int(row[17]), int(row[18]), int(row[19]), int(row[20]), int(row[21]), int(row[22]), int(row[23]), int(row[24]), int(row[25]), int(row[26]), int(row[27]), int(row[28]), int(row[29]), int(row[30]), int(row[31]), int(row[32])]
        # row_new = [row[0], float(row[1]), float(row[3])]
        # print row_new
        X_total.append(row_new)

# remove the Business_ID
X_total = np.array(X_total)
print "X_total all columns", X_total.shape, X_total[0]

y_total = X_total[:, 1]
# print "++++++++++++++++++++++++++++++++++++++++++++++++++", y_total

X_total = X_total[:, 2:33]  # 33-2=31 features
# X_total = X_total[:, 2]

y_total = np.array(y_total, dtype=float)
y_total = [int(2*x) for x in y_total]
# print "++++++++++++++++++++++++++++++++++++++++++++++++++", y_total
y_total = np.array(y_total, dtype=int)

# X_total = np.array(X_total, dtype=float)
X_total = np.array(X_total, dtype=int)
print "X_total, y_total", X_total.shape, y_total.shape
X_total = np.column_stack((X_total, y_total))
print "X_total + labels 1", X_total.shape
np.save("X_total_sgd_kernel_attr", X_total)

X_total = np.load('X_total_sgd_kernel_attr.npy')
print "X_total + labels 2", X_total.shape, X_total[0], X_total[-1]

#############################################################################
# X data
data_X = X_total[:, 0:n_features]
print("data X shape new", data_X.shape, data_X.dtype)

# labels y
data_y = X_total[:, np.newaxis, n_features]
print("data y shape new", data_y.shape, data_y.dtype)

# Split the data into training/testing sets
data_X_train = data_X[:split_index_train]
data_X_test = data_X[-split_index_test:]
print("data train test X data shape", data_X_train.shape, data_X_test.shape)

# Split the targets into training/testing sets
data_y_train = data_y[:split_index_train]
data_y_test = data_y[-split_index_test:]
print("data train test y data shape", data_y_train.shape, data_y_test.shape)

data_y_train = data_y_train.reshape((-1))
data_y_test = data_y_test.reshape((-1))

#############################################################################
# SGD kernel
rbf_feature = RBFSampler(gamma=1, random_state=1)
X_features = rbf_feature.fit_transform(data_X_train)
clf = SGDClassifier()
# clf = SGDClassifier(loss="hinge", penalty="l2")

clf.fit(X_features, data_y_train)

# print clf

X_features_test = rbf_feature.fit_transform(data_X_test)
predicted = clf.predict(X_features_test)
# predicted = clf.predict([data_X_test[-3]])
# print "clf.score", clf.score
# print "predicted, actual", predicted, data_y_test[-3]
joblib.dump(clf, 'sgd_kernel_attr.pkl')


# cmat1 = metrics.confusion_matrix(data_y_test, predicted)

cmat = confusion_matrix(data_y_test, predicted)

np.set_printoptions(precision=2)
print "score", clf.score(X_features, data_y_train)
print(metrics.classification_report(data_y_test, predicted))
print(metrics.confusion_matrix(data_y_test, predicted))
print "coeff", clf.coef_

report = metrics.classification_report(data_y_test, predicted)
np.save("rep_sgd_kernel.npy", report)
cmat = confusion_matrix(data_y_test, predicted)
np.save("cmat_sgd_kernel.npy", cmat)

# Plot non-normalized confusion matrix
plt.figure()
plot_confusion_matrix(cmat, classes=class_names,
                      title='Confusion matrix, without normalization')

# Plot normalized confusion matrix
plt.figure()
np.set_printoptions(precision=2)
plot_confusion_matrix(cmat, classes=class_names, normalize=True,
                      title='Normalized confusion matrix')

plt.show()

#   'precision', 'predicted', average, warn_for)
#              precision    recall  f1-score   support
#
#           2       0.00      0.00      0.00       321
#           3       0.00      0.00      0.00       445
#           4       0.00      0.00      0.00      1020
#           5       0.00      0.00      0.00      1787
#           6       0.14      0.04      0.06      2885
#           7       0.18      0.86      0.29      3626
#           8       0.29      0.08      0.13      4155
#           9       0.16      0.01      0.03      2828
#          10       0.48      0.01      0.02      2933
#
# avg / total       0.21      0.18      0.10     20000
