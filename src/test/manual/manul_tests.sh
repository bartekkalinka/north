curl -X POST -H 'Content-Type: application/json' http://localhost:9000/auctions -d \
'{ "seller" : { "name" : "chris" }, "product" : { "name" : "car" } }'

curl http://localhost:9000/auctions/f5ec263a-671f-4f37-92ed-f393526b9502

curl -X PUT -H 'Content-Type: application/json' http://localhost:9000/auctions/77cc4653-f120-4ae5-9b46-07681fe87076 -d \
'{ "bidder" : { "name" : "tom" },	"amount" : 13 }'