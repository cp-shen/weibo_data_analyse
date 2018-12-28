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
result = sorted(result)
c = cl.Counter(list(result))

plt.bar(list(c.keys()), list(c.values()))
plt.show()
