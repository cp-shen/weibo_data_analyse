import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt
import pandas as pd
import collections as cl

f = open('./data/weibo.txt', 'r')
data = {}
for line in f:
    content = line.split()
    uid = content[1]
    date = content[4]
    if (uid, date) in data:
        data[(uid, date)] += 1
    else:
        data[(uid, date)] = 1

f.close()
result = np.array(list(data.values()))

sns.kdeplot(result, bw = 1, label = "bw:1")
sns.kdeplot(result, bw = 3, label = "bw:3")
sns.kdeplot(result, bw = 6, label = "bw:6")
sns.kdeplot(result, bw = 9, label = "bw:9")

plt.legend()
plt.show()