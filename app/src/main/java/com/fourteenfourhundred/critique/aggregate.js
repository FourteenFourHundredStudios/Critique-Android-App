db.users.aggregate(

	// Pipeline
	[
		// Stage 1
		{
			$match: {
			    username:"marc"
			}
		},

		// Stage 2
		{
			$project: {
			
			    "_id":0,
			    "followed": "$following",
			   	"username":"$username"
			    
			}
		},

		// Stage 3
		{
			$lookup: {
			       from: "users",
			       localField: "followed",
			       foreignField: "username",
			       as: "value"
			     }
		},

		// Stage 4
		{
			$project: {
			 	followed:"$followed",
			 	username:"$username",
			 	following:"$value.following"
			  
			}
		},

		// Stage 5
		{
			$project: {
			  
			    followed:"$followed",
			    
			    isMutual:{
			     		$map:
			                 {
			                   input: "$following",
			                   as: "a",
			                   
			                   in: { $in: [ "$username","$$a" ] }
			                 }
			         }
			    }
		},

		// Stage 6
		{
			$project: {
			    mutuals:{
			    		"$map": {
			         "input": { "$zip": { "inputs": [ "$followed", "$isMutual" ] } },
			         "as": "el",
			         "in": { 
			           "username": { "$arrayElemAt": [ "$$el", 0 ] }, 
			           "isMutual": { "$arrayElemAt": [ "$$el", 1 ] }
			         }
			       }
			    }
			}
		},

	]

	// Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);
