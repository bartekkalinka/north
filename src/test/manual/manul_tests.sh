curl -X POST -H 'Content-Type: application/json' http://localhost:9000/auctions -d \
'{ "seller" : { "name" : "chris" }, "product" : { "name" : "car" } }'

curl http://localhost:9000/auctions/fca2d80e-8872-4ada-87b9-c9b960a8314e

curl -X PUT -H 'Content-Type: application/json' http://localhost:9000/auctions/fca2d80e-8872-4ada-87b9-c9b960a8314e -d \
'{ "bidder" : { "name" : "tom" },	"amount" : 13 }'