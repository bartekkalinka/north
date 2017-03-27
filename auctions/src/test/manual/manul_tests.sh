curl -X POST -H 'Content-Type: application/json' http://localhost:9000/auctions -d \
'{ "seller" : { "name" : "chris" }, "product" : { "name" : "car" }, "expiresIn" : 5 }'

curl http://localhost:9000/auctions/aac0e88c-60a4-40a8-b0a6-6b7081cb8a9a

curl -X PUT -H 'Content-Type: application/json' http://localhost:9000/auctions/aac0e88c-60a4-40a8-b0a6-6b7081cb8a9a -d \
'{ "bidder" : { "name" : "tom" },	"amount" : 13 }'