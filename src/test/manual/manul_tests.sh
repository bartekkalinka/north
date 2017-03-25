curl -X POST -H 'Content-Type: application/json' http://localhost:9000/auctions -d \
'{ "seller" : { "name" : "chris" }, "product" : { "name" : "car" } }'

curl http://localhost:9000/auctions/8a9bcb05-07b9-4450-ab40-e3f80d386739

curl -X PUT -H 'Content-Type: application/json' http://localhost:9000/auctions/8a9bcb05-07b9-4450-ab40-e3f80d386739 -d \
'{ "bidder" : { "name" : "tom" },	"amount" : 13 }'