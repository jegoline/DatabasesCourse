import { db } from 'baqend/realtime'

class MovieService {

    /**
     * Loads movie suggestions for the typeahead input
     * @param {string} [title] The movie title
     */
    loadMovieSuggestions(title) {
        //TODO - DONE BUT NO OUTPUT
       /*  let query = db.Movie.find()
            .where({ 'id': { '$exists' : true } })
			.sort({ 'id': -1 })
            .limit(10); */
		var regexBuilder = new RegExp('^'+args.parameter);
		let query = db.Movie.find()
			.matches('title', regexBuilder)
			.sort({ 'id': -1 })
			.limit(10);
				
        return query.resultList((results) => results.map((result) => result.title));
    }

    /**
     * Loads a specific movie by title
     * @param {string} [title] The movie title
     */
    loadMovieByTitle(title) {
        //TODO - DONE 100%
        /* let query = db.Movie.find()
            .where({ 'id': { '$exists' : true } })
			.sort({ 'id': -1 }); */
		let query = db.Movie.find()
		.where({ 'title': title })
		.sort({ 'id': -1 })
        return query.singleResult();
    }
	
	

    /**
     * Queries movies filtered by the query arguments
     * @param {Object} [args] The query arguments
     * @param {string} [args.type=prefix|rating-greater|genre|genrePartialmatch|release|comments] The query type
     * @param {string} [args.parameter] The query parameter
     * @param {string} [args.limit=10] Max results
     */
    queryMovies(args) {
        let  query = '';

        switch (args.type) {
			//TODO - DONE 85%
			case "prefix":
				var regexBuilder = new RegExp('^'+args.parameter);
				query = db.Movie.find()
				.matches('title', regexBuilder)
				.sort({ 'id': -1 })
				.limit(new Number(args.limit));
				break;
            case "rating-greater":
				query = db.Movie.find()
				.gt('rating', new Number(args.parameter))
				.ascending('rating')
				.limit(new Number(args.limit));
				break;
			case "genre":
				var queryString = args.parameter.replace(/\s/g, '');
				var queryList = queryString.split(',').filter(function(el) {return el.length != 0});
				query = db.Movie.find()
				.containsAll('genre', queryList)
				.sort({ 'id': -1 })
				.limit(new Number(args.limit));
				break;
			case "genrePartialmatch":
				var regexBuilder = new RegExp('^'+args.parameter);
				query = db.Movie.find()
				.matches('genre', regexBuilder)
				.sort({ 'id': -1 })
				.limit(new Number(args.limit));
				break;
			case "release":
				var regexBuilder = new RegExp('^'+args.parameter);
				query = db.Movie.find() 
				.matches('releases.country', regexBuilder)
				.lt('year', '1950') 
				.sort({ 'id': -1 })
				.limit(new Number(args.limit));
				break;
			case "comments":
				let queryPromise = new Promise((resolve, reject) => {
					var regexBuilder = new RegExp('^' + args.parameter.trim());
					var userQuery = db.MovieComment.find().matches('username', regexBuilder);
					let movieList = [];
					
					userQuery.resultList((object) => {
						var movieList = [];
						object.forEach((itr) => {
							if(itr.movie){
								if(itr.movie.id){
									movieList.push(itr.movie.id.trim());
								}
							}
						});
						
						query = db.Movie.find()
							.containsAny('id', movieList) 
							.sort({ 'id': -1 })
							.limit(new Number(args.limit));
							
						query.resultList((object) => {
							resolve(object);
						});
					});
				});
				return queryPromise;

			default:
				query = db.Movie.find()
				.where({ 'id': { '$exists' : true } })
				.sort({ 'id': -1 })
				.limit(new Number(args.limit));
        }

        return query.resultList();
    }

}

export default new MovieService()
