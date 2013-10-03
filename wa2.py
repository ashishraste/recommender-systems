import pandas as pd
import numpy as np
from __future__ import division

location = r'/home/freax/Downloads/recsys_data_WA 1 Rating Matrix.csv'
df = pd.read_csv(location, header=none)
df.columns = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]

for i in range(1, 21):
    print '\n'
    na = df[i]         #gives us the an array of column i values
    items = 0 
    s = 0       
    pcent = 0
    for j in np.nditer(na):
        if not math.isnan(j):
            items = items + 1
            if j >= 4:
                s = s + 1
    try:
        pcent = (s / items) * 100
    except ZeroDivisionError:
        print "Zero div error!\n"
    nb[i-1] = pcent
    
nb_sorted = np.sort(nb)
