curl -X POST -H 'Content-Type: application/json' http://localhost:9000/auctions -d \
'{ "seller" : { "name" : "chris" }, "product" : { "name" : "car" } }'

curl http://localhost:9000/auctions/874e6141-0653-4e79-ac5c-17b33f9459b6

curl -X PUT -H 'Content-Type: application/json' http://localhost:9000/auctions/874e6141-0653-4e79-ac5c-17b33f9459b6 -d \
'{ "bidder" : { "name" : "tom" },	"amount" : 13 }'