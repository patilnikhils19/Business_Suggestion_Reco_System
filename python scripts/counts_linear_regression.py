import matplotlib.pyplot as plt
import numpy as np
from sklearn import datasets, linear_model
import csv


def round_of_array(arr):
    for i, val in enumerate(arr):
        arr[i] = round(val * 2) / 2
    return arr

# Load Data ------------------------------------------------------------------
X_total = []

# X_total = np.array(list(csv.reader(open('yelp_counts.tsv'), delimiter='\t')))
with open('yelp_counts.tsv', 'rb') as tsvfile:
    for row in tsvfile:
        row = row.strip().split('\t')
        # row_new = [row[0], float(row[1]), float(row[2]), float(row[3]), float(row[4]), float(row[5])]
        row_new = [row[0], float(row[1]), float(row[3])]
        # print row_new
        X_total.append(row_new)

# remove the Business_ID
X_total = np.array(X_total)
print "X_total all columns", X_total.shape, X_total[0]

y_total = X_total[:, 1]
# X_total = X_total[:, 2:6]
X_total = X_total[:, 2]
y_total = np.array(y_total, dtype=float)
X_total = np.array(X_total, dtype=float)
print "X_total, y_total", X_total.shape, y_total.shape
X_total = np.column_stack((X_total, y_total))
print "X_total + labels", X_total.shape
np.save("X_total", X_total)

X_total = np.load('X_total.npy')
print "X_total + labels", X_total.shape, X_total[0], X_total[-1]


# Linear Regression Train Test Split ---------------------------------------------------------
print "#----------------------------------------------------------------------"
# Use only one feature or all
n_features = 1
split_index_train = 100000
split_index_test = 20000
diabetes_X = X_total[:, 0:n_features]
print("diabetes data X shape new", diabetes_X.shape, diabetes_X.dtype, diabetes_X[-1])

# labels y
diabetes_y = X_total[:, np.newaxis, 1]
print("diabetes data y shape new", diabetes_y.shape, diabetes_y.dtype, diabetes_y[-1])

# Split the data into training/testing sets
diabetes_X_train = diabetes_X[:split_index_train]
diabetes_X_test = diabetes_X[-split_index_test:]
print("diabetes train test X data shape", diabetes_X_train.shape, diabetes_X_test.shape)

# Split the targets into training/testing sets
diabetes_y_train = diabetes_y[:split_index_train]
diabetes_y_test = diabetes_y[-split_index_test:]
print("diabetes train test y data shape", diabetes_y_train.shape, diabetes_y_test.shape)

#############################################################################
# Create linear regression object
regr = linear_model.LinearRegression()

# Train the model using the training sets
regr.fit(diabetes_X_train, diabetes_y_train)

# The coefficients
print('Coefficients: \n', regr.coef_)
# The mean squared error
print "test output labels", round_of_array(regr.predict(diabetes_X_test))
print "test actual labels", diabetes_y_test
print("Mean squared error: %.5f" % np.mean((round_of_array(regr.predict(diabetes_X_test)) - diabetes_y_test) ** 2))
# Explained variance score: 1 is perfect prediction
print('Variance score: %.5f' % regr.score(diabetes_X_test, diabetes_y_test))

#############################################################################
# # Plot outputs
plt.scatter(diabetes_X_test[:, 0], diabetes_y_test,  color='black')
plt.plot(diabetes_X_test[:, 0], regr.predict(diabetes_X_test), color='blue', linewidth=3)
#
# # plt.xticks(())
# # plt.yticks(())
#
plt.show()