/* %s is where the string insert will be */
/* ie in Java => "... WHERE id =" + %s */ 

/* Formalized query: output [id, identifier, type] for original queries which return id
/* Note: For those have two types, concate them into single string. e.g. 'Normal, Water'

/*	Query: 1	*/
SELECT identifier 
FROM pokemon_species
WHERE id = %s;

/*	Query: 2	*/
SELECT s.identifier, ps.base_stat
FROM pokemon_stats AS ps, stats AS s
WHERE ps.pokemon_id = %s AND s.id = ps.stat_id;

/*	Query: 3	*/
SELECT t.identifier
FROM pokemon_types AS pt, types AS t
WHERE pt.pokemon_id = %s AND t.id = pt.type_id;

/*	Query: 4 	*/
SELECT g.identifier
FROM generations AS g, pokemon_species AS ps
WHERE ps.id = %s AND g.id = ps.generation_id;

/*	Query: 5	*/
SELECT evolves_from_species_id
FROM pokemon_species
WHERE id = %s;

/*	Query: 6	*/
SELECT id
FROM  pokemon_species
WHERE id<>%s
AND evolution_chain_id = (SELECT evolution_chain_id
	FROM pokemon_species
	WHERE id=%s);

/*	Query: 7	*/
SELECT p.generation_id, r.identifier
FROM pokemon_species as p, regions as r
WHERE p.id=%s AND p.generation_id=r.id;

/*	Query: 8	*/
SELECT s.identifier as "To-get", t.identifier as "Method"
FROM evolution_triggers as t, pokemon_evolution as e, pokemon_species as s
WHERE t.id=e.evolution_trigger_id AND e.evolved_species_id=%s AND s.id=%s;

/*	Query: 9	*/
SELECT identifier
FROM pokemon_species
WHERE generation_id = %s;

/*	Query: 10	*/
SELECT distinct ps.pokemon_id 
FROM pokemon_stats AS ps, stats AS s
WHERE s.identifier = %s AND ps.base_stat >= %s 
ORDER by ps.pokemon_id;

/*	Query: 11	*/
SELECT p.pokemon_id 
FROM (select pokemon_id, sum(base_stat) 
	FROM pokemon_stats 
	GROUP BY pokemon_id 
	ORDER BY pokemon_id asc) AS p 
WHERE sum >= %s;

/*	Query: 12	*/
SELECT pt.pokemon_id 
FROM pokemon_types AS pt, types AS t 
WHERE t.identifier = %s AND t.id = pt.type_id;

