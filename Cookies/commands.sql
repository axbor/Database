#Skapa pall kommandon:

#create batch



#list cookies
select cookieName from Recipe;


#gui selects cookiename


insert into ProductionBatch values(?,NOW(),NOW(), null, untested);


select ingredientName, amount  from CookieContains where cookieName = ?


# gör en for-loop som uppdaterar varje ingrediens

select amount from RawMaterial where ingredientName = ?
	rawAmount = rawAmount - pallets*54*cookieAmount;
	#pallets - antalet pallar som ska göras, cookieAmount kommer från cookiecontains
	update RawMaterial set amount = ? where ingredientName = ?



#skapa pallar i for loop

insert into Pallets values(null, 'in production', 


