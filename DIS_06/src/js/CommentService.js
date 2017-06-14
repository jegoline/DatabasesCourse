import { db } from 'baqend/realtime'

class CommentService {

    /**
     * Returns a comment stream for a movie
     * @param {Object} [movie] The reference to the movie object
     */
    streamComments(movie) {
        let query = db.MovieComment.find()
            .equal('movie', movie.id)
			.sort({'id': -1 });
        return query.resultStream()
    }

    /**
     * Queries comments filtered by the query arguments
     * @param {Object} [args] The query arguments
     * @param {string} [args.type=prefix|keyword] The query type
     * @param {string} [args.parameter] The query parameter
     * @param {string} [args.limit=10] Max results
     */
    queryComments(args) {
        let query = ''

        switch (args.type) {
		case "prefix":
			var regexBuilder = new RegExp('^' + args.parameter.trim());
			query = db.MovieComment.find()
				.matches('username', regexBuilder)
				.sort({ 'id': -1 })
				.limit(new Number(args.limit));
			break;
        case "keyword":	
			var regexBuilder = new RegExp('^.*' + args.parameter.trim() + '.*');
		    query = db.MovieComment.find();
				
			var cond1 = query.matches('username', regexBuilder);
			var cond2 = query.matches('text', regexBuilder);
			
			query = query.or(cond1, cond2).sort({ 'id': -1 }).limit(new Number(args.limit));
			break;
        }

        return query.resultList({depth: 1}); // with depth: 1, the referenced movies will be loaded
    }

    /**
     * Adds a comment for a movie
     * @param {Object} [movie] The reference to the movie object
     * @param {Object} [comment] The comment
     * @param {string} [comment.username] The comment username
     * @param {string} [comment.text] The comment text
     */
    addComment(movie, comment) {
		var com = new db.MovieComment({username: comment.username, text: comment.text});
		com.movie = movie;
		com.save();
    }

    /**
     * Returns a comment stream for a movie
     * @param {Object} [comment] The reference to the old comment object
     * @param {String} [newText] The new text
     */
    editComment(comment, newText) {
        comment.text = newText;
		comment.update().then(function() {
			//the comment was updated
		}, function(e) {
			alert("We can't update your comment :(");
		});
    }

}

export default new CommentService()
