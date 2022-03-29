# Joke of the day 

A Joke of the day REST service. Returns JSON.

Supports CRUD actions to the /jokes endpoint on port 8080

## Notes:
* Built on JDK 11
* date format id yyyy-MM-dd (ISO-8601)
* loads 3 jokes (ids 1-3) on startup
* description field is optional and is only returned if populated
* fixed joke per day - lets you do things like put Pie jokes on 3/14; tradeoff pretty rigid
* uses Apache Lang StringBuilder and Lombok to be a little fancy
* there's no services/view complexity because this isn't that complicated
* not strictly REST (HATEOAS) due to lack of self/parent link generation (TODO?)
* Could use a fancy Swagger (Open API) doc

# Build and run

Uses the built-in gradle wrapper. Just:

`./gradlew clean bootRun`

# Convenient curls 

## Get the joke of the day
`curl -v 'localhost:8080/api/jokes/today'`

## Get a given day's joke
`curl -v 'localhost:8080/api/jokes/dates?date=yyyy-MM-dd'`

## POST
`curl -X POST 'localhost:8080/api/jokes' -H 'Content-type:application/json' -d '{"joke": "I gave up my seat to an elderly person on the bus. And that’s how I lost my job as a bus driver.", "date": "2023-01-01", "description": "bus driver joke"}'`

## GET (all)
`curl -v 'localhost:8080/api/jokes'`

## GET one (above POST)
`curl -v localhost:8080/api/jokes/4`

## PUT (changes the period to an exclamation mark)
`curl -X PUT localhost:8080/api/jokes/4 -H 'Content-Type:application/json' -d '{"joke": "I gave up my seat to an elderly person on the bus. And that’s how I lost my job as a bus driver!", "date": "2023-01-01", "description": "bus driver joke with exclamation mark"}'`
### Re-get modified joke
`curl -v localhost:8080/api/jokes/4`

## DELETE bus driver joke
`curl -v -X DELETE localhost:8080/api/jokes4`

### Verify deleted 404 / "Could not find joke 4"
`curl -v localhost:8080/api/jokes/4`