import { db } from 'baqend/realtime'

class TweetService {

  /**
   * Returns a comment stream for a movie
   * @param {Object} [movie] The reference to the movie object
   */
  streamMovieTweets(movie) {
    var regexBuilder = new RegExp('^.*' + movie.title.toLowerCase() + '.*');
	let query = db.Tweet.find()
			.matches('text', regexBuilder)
			.sort({ 'id': -1 });
    return query.resultStream()
  }

  /**
   * Returns a comment stream for a movie
   * @param {Object} [args] The query arguments
   * @param {string} [args.type=prefix|keyword|followersOrFriends] The query type
   * @param {string} [args.parameter] The query parameter
   * @param {string} [args.limit=10] Max results
   */
  queryTweets(args) {
	let query = '';
	switch (args.type) {
		case "prefix":
			var regexBuilder = new RegExp('^' + args.parameter.trim().toLowerCase());
			query = db.Tweet.find()
				.matches('text', regexBuilder)
				.sort({ 'id': -1 })
				.limit(new Number(args.limit));
			break;
        case "keyword":
			var regexBuilder = new RegExp('^.*' + args.parameter.trim().toLowerCase() + '.*');
		    query = db.Tweet.find()
				.matches('text', regexBuilder)
				.sort({ 'id': -1 })
				.limit(new Number(args.limit));
			break;
		case "followersOrFriends":
			query = db.Tweet.find();
			var num = new Number(args.parameter.trim());
			if(isNaN(num)){
				num = 0;
			}
				
			var cond1 = query.gt('user.followers_count', num);
			var cond2 = query.gt('user.friends_count', num);
			
			query = query.or(cond1, cond2).sort({ 'id': -1 }).limit(new Number(args.limit));
			break;
    }

    return query.resultList();
  }
}

export default new TweetService()
