from __future__ import division
import pandas as pd
import itertools
import operator as op
import math as math
import numpy as np


location = r'/home/freax/recommender-systems/recsys_data_WA 1 Rating Matrix.csv'
df = pd.read_csv(location, header=None)
df.columns = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
nb = np.zeros(20)

# calculating the mean rating for each movie and ordering the top 5 in descending order
for col in range(1, 21):
    print '\n'    
    na = df[col]             # gives us the a series of every column in the dataframe 
    na = na[1:]             # we must ignore the header which will be in row 0
    na = [float(i) for i in na]    
    item_count = 0
    ratings_sum = 0
    pcent = 0

    for item in range(len(na)):
        if ~np.isnan(na[item]):
            item_count = item_count + 1
            ratings_sum = na[item]
    try:
        pcent = (ratings_sum / item_count) * 100
    except ZeroDivisionError:
        print "Zero div error!\n"        
    nb[col-1] = pcent
nb = nb.tolist()
nb = sorted(nb, reverse=True)
top5_movies_avg_ratings = nb[0:5]
print top5_movies_avg_ratings



#    nb[col-1] = pcent
#    
#nb_sorted = np.sort(nb)
#print nb_sorted
