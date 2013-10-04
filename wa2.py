from __future__ import division
import pandas as pd
import operator as op
import math as math
import numpy as np


location = r'/home/freax/recommender-systems/recsys_data_WA 1 Rating Matrix.csv'
df = pd.read_csv(location, header=None)
# df.columns = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]
ratingsMeanList = np.zeros(20)
highRatingsList = np.zeros(20)
popularMoviesList = np.zeros(20)
movieNamesList = df.iloc[0][1:21]
movieNamesList = movieNamesList.tolist()

for col in range(1, 21):   
    na = df[col]             # gives us the a series of every column in the dataframe 
    na = na[1:]             # we must ignore the header which will be in row 0
    na = [float(i) for i in na]    
    itemCount = 0
    ratingsSum = 0
    ratingsMean = 0
    highRatings = 0
    pcent = 0

    for item in range(len(na)):
        if ~np.isnan(na[item]):
            itemCount = itemCount + 1
            ratingsSum = ratingsSum + na[item]      
            if na[item] >= 4:
                highRatings = highRatings + 1

    try:
        ratingsMean = ratingsSum / itemCount
        pcentHighRatings = (highRatings / itemCount) * 100
    except ZeroDivisionError:
        print "Zero div error!\n"        
    ratingsMeanList[col-1] = ratingsMean
    highRatingsList[col-1] = pcentHighRatings
    popularMoviesList[col-1] = itemCount
    
ratingsMeanList = ratingsMeanList.tolist()
highRatingsList = highRatingsList.tolist()
popularMoviesList = popularMoviesList.tolist()

# storing the mean rating for each movie and listing the top 5
movieRatingsDict = dict(zip(movieNamesList, ratingsMeanList))
sortedTop5Movies = sorted(movieRatingsDict.iteritems(), key=op.itemgetter(1), reverse=True)
sortedTop5Movies = sortedTop5Movies[0:5]
#print sortedTop5Movies

# storing the percentage of ratings for each movie that are 4 or higher, in a dictionary and listing the top 5
movieHighRatingsDict = dict(zip(movieNamesList, highRatingsList))
sortedTop5HighlyRatedMovies = sorted(movieHighRatingsDict.iteritems(), key=op.itemgetter(1), reverse=True)
sortedTop5HighlyRatedMovies = sortedTop5HighlyRatedMovies[0:5]
#print sortedTop5HighlyRatedMovies

# storing the movies ordered with the most number of ratings first, and listing the top 5
popularMoviesDict = dict(zip(movieNamesList, popularMoviesList))
sortedTop5PopularMovies = sorted(popularMoviesDict.iteritems(), key=op.itemgetter(1), reverse=True)
sortedTop5PopularMovies = sortedTop5PopularMovies[0:5]
print sortedTop5PopularMovies


