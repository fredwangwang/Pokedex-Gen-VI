SELECT ps.id, ps.identifier, types 
FROM (pokemon_species AS ps, 
							SELECT ()
	  )
	  
SELECT identifier FROM pokemon_species AS ps WHERE ps.id IN 
	  (SELECT id FROM pokemon_species WHERE identifier like 'char%')
	  
	  //µÃµ½ ids
	 SELECT pkids.id, pspecies.identifier, types.identifier
	 FROM (SELECT id FROM pokemon_species WHERE identifier like 'char%') AS pkids,
					pokemon_species AS pspecies,
					pokemon_types AS ptype,
					types
	WHERE	pkids.id = pspecies.id AND pkids.id = ptype.pokemon_id AND ptype.type_id = types.id;
	 