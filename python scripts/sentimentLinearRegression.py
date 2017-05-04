print(__doc__)

##!/usr/bin/python
##!/usr/bin/env python


# Code source: Jaques Grobler
# License: BSD 3 clause

import matplotlib.pyplot as plt
import numpy as np
from sklearn import datasets, linear_model


n_features = 31
split_index_train = 110000
# split_index_test = 30000
split_index_test = 5000

def round_of_array(arr):
    for i, val in enumerate(arr):
        arr[i] = round(val * 2) / 2
    return arr

# Load Data ------------------------------------------------------------------
X_total = []

# X_total = np.array(list(csv.reader(open('yelp_counts.tsv'), delimiter='\t')))
with open('senti.tsv', 'rb') as tsvfile:
    for row in tsvfile:
        row = row.strip().split('\t')
        row_new = [row[0], float(row[1]), float(row[2])]
        # row_new = [row[0], float(row[1]), float(row[3])]
        # print row_new
        X_total.append(row_new)

# remove the Business_ID
X_total = np.array(X_total)
print "X_total all columns", X_total.shape, X_total[0]

y_total = X_total[:, 1]
# print "++++++++++++++++++++++++++++++++++++++++++++++++++", y_total

X_total = X_total[:, 2]  # 1 feature
# X_total = X_total[:, 2]

y_total = np.array(y_total, dtype=float)
# y_total = [int(2*x) for x in y_total]
# print "++++++++++++++++++++++++++++++++++++++++++++++++++", y_total
# y_total = np.array(y_total, dtype=int)

# X_total = np.array(X_total, dtype=float)
X_total = np.array(X_total, dtype=float)
print "X_total, y_total", X_total.shape, y_total.shape
X_total = np.column_stack((X_total, y_total))
print "X_total + labels 1", X_total.shape
np.save("X_total_senti", X_total)

X_total = np.load('X_total_senti.npy')
print "X_total + labels 2", X_total.shape, X_total[0], X_total[-1]

# # Load the diabetes dataset
# diabetes = datasets.load_diabetes()
# print("diabetes data shape", diabetes.data.shape)

# Use only one feature
diabetes_X = X_total[:, np.newaxis, 0]  # diabetes.data[:, np.newaxis, 2]
print("diabetes X data shape new", diabetes_X.shape, diabetes_X.dtype, diabetes_X[0], diabetes_X[-3])
print("diabetes y data shape new", y_total[0], y_total[-3])

# Split the data into training/testing sets
diabetes_X_train = diabetes_X[:-split_index_test]
diabetes_X_test = diabetes_X[-split_index_test:]
print("data train test X data shape", diabetes_X_train.shape, diabetes_X_test.shape)

# Split the targets into training/testing sets
diabetes_y_train = y_total[:-split_index_test] # diabetes.target[:-split_index_test]
diabetes_y_test =  y_total[-split_index_test:] # diabetes.target[-split_index_test:]
print("data train test y data shape", diabetes_y_train.shape, diabetes_y_test.shape)



# Create linear regression object
regr = linear_model.LinearRegression()

# Train the model using the training sets
regr.fit(diabetes_X_train, diabetes_y_train)

# The coefficients
print('Coefficients: \n', regr.coef_)
# The mean squared error
print("Mean squared error: %.2f" % np.mean(((regr.predict(diabetes_X_test)) - diabetes_y_test) ** 2))
# Explained variance score: 1 is perfect prediction
print('Variance score: %.2f' % regr.score(diabetes_X_test, diabetes_y_test))

# Plot outputs
plt.scatter(diabetes_X_test, diabetes_y_test, alpha=0.8, marker='.', color='black')
plt.plot(diabetes_X_test, regr.predict(diabetes_X_test), color='blue',
         linewidth=3)

plt.xlabel('average sentiment value per business', fontsize=20)
plt.ylabel('star ratings of the business', fontsize=20)

# plt.xticks(())
# plt.yticks(())

# plt.rc('xtick', labelsize=30)
# plt.rc('ytick', labelsize=30)

plt.show()