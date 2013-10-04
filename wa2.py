from __future__ import division
import pandas as pd
import operator as op
import math as math
import numpy as np


location = r'/home/freax/recommender-systems/recsys_data_WA 1 Rating Matrix.csv'
df = pd.read_csv(location, header=None)
# df.columns = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
nb = np.zeros(20)
movie_names = df.iloc[0][1:21]
movie_names = movie_names.tolist()

# calculating the mean rating for each movie and ordering the top 5 in descending order
for col in range(1, 21):   
    na = df[col]             # gives us the a series of every column in the dataframe 
    na = na[1:]             # we must ignore the header which will be in row 0
    na = [float(i) for i in na]    
    item_count = 0
    ratings_sum = 0
    ratings_mean = 0
    pcent = 0

    for item in range(len(na)):
        if ~np.isnan(na[item]):
            item_count = item_count + 1
            ratings_sum = na[item]      
    try:
        ratings_mean = ratings_sum / item_count
        #pcent = (ratings_sum / item_count) * 100
    except ZeroDivisionError:
        print "Zero div error!\n"        
    nb[col-1] = ratings_mean
nb = nb.tolist()
movie_ratings_dict = dict(zip(movie_names, nb))
sorted_top5_movies = sorted(movie_ratings_dict.iteritems(), key=op.itemgetter(1), reverse=True)
sorted_top5_movies = sorted_top5_movies[0:5]
print sorted_top5_movies



