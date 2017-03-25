curl -X POST -H 'Content-Type: application/json' http://localhost:9000/auctions -d \
'{ "seller" : { "name" : "chris" }, "product" : { "name" : "car" } }'

curl http://localhost:9000/auctions/16b03ffd-b54e-47e8-9c54-88afc26e8890

curl -X PUT -H 'Content-Type: application/json' http://localhost:9000/auctions/16b03ffd-b54e-47e8-9c54-88afc26e8890 -d \
'{ "bidder" : { "name" : "tom" },	"amount" : 13 }'