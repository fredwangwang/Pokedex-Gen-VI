/* %s is where the string insert will be */
/* ie in Java => "... WHERE id =" + %s */ 

/* Formalized query: output [id, identifier, type] for original queries which return id
/* Note: For those have two types, concate them into single string. e.g. 'Normal, Water'

/*	1. Given national pokedex id, find the name of the pokemon	*/
SELECT identifier 
FROM pokemon_species
WHERE id = %s;

/*	2. Given national pokedex id, find the stats of the pokemon	*/
SELECT s.identifier, ps.base_stat
FROM pokemon_stats AS ps, stats AS s
WHERE ps.pokemon_id = %s AND s.id = ps.stat_id;

/*	3. Given national pokedex id, find the type of pokemon	*/
SELECT t.identifier
FROM pokemon_types AS pt, types AS t
WHERE pt.pokemon_id = %s AND t.id = pt.type_id;

/*	4. Given national pokedex id, find the generation of that pokemon	*/
SELECT g.identifier
FROM generations AS g, pokemon_species AS ps
WHERE ps.id = %s AND g.id = ps.generation_id;

/*	5. Given national pokedex id, find what is the id of the pokemon it evolves from	*/
SELECT evolves_from_species_id
FROM pokemon_species
WHERE id = %s;

/*	6. Given national pokedex id, find the id of all ancestor and descendant of that pokemon	*/
SELECT id
FROM  pokemon_species
WHERE id<>%s
AND evolution_chain_id = (SELECT evolution_chain_id
	FROM pokemon_species
	WHERE id=%s);

/*	7. Given generation id, find the region	*/
SELECT p.generation_id, r.identifier
FROM pokemon_species as p, regions as r
WHERE p.id=%s AND p.generation_id=r.id;

/*	8. Given ancestor id and pokemon id, find all evolve conditions	*/
SELECT s.identifier as "To-get", t.identifier as "Method"
FROM evolution_triggers as t, pokemon_evolution as e, pokemon_species as s
WHERE t.id=e.evolution_trigger_id AND e.evolved_species_id=%s AND s.id=%s;

/*	9. Given generation id, find all pokemon of that generation	*/
SELECT identifier
FROM pokemon_species
WHERE generation_id = %s;

/*	10. Given condition stats, find all ids of qualified pokemon	*/
SELECT distinct ps.pokemon_id 
FROM pokemon_stats AS ps, stats AS s
WHERE s.identifier = %s AND ps.base_stat >= %s 
ORDER by ps.pokemon_id;

/*	11. Given stats sum, find all ids of qualified pokemon	*/
SELECT p.pokemon_id 
FROM (select pokemon_id, sum(base_stat) 
	FROM pokemon_stats 
	GROUP BY pokemon_id 
	ORDER BY pokemon_id asc) AS p 
WHERE sum >= %s;

/*	12. Given certain type, find ids of all pokemon of that type	*/
SELECT pt.pokemon_id 
FROM pokemon_types AS pt, types AS t 
WHERE t.identifier = %s AND t.id = pt.type_id;

