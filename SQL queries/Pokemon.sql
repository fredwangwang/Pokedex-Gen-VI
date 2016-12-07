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
SELECT * FROM
	(SELECT t.identifier||', '||tt.identifier
	FROM pokemon_types AS pt, types AS t,
		(SELECT t.identifier, t.id, pt.slot 
		FROM pokemon_types AS pt, types AS t 
		WHERE pt.pokemon_id = %s AND t.id = pt.type_id) AS tt 
	WHERE pt.pokemon_id = %s AND t.id = type_id AND (t.id <> tt.id               OR tt.slot = 1)
	ORDER BY t.identifier) AS p 
LIMIT 1;

/*	4. Given national pokedex id, find the generation of that pokemon	*/
SELECT g.identifier
FROM generations AS g, pokemon_species AS ps
WHERE ps.id = %s AND g.id = ps.generation_id;

/*	5. Given national pokedex id, find what is the id of the pokemon it evolves from	*/
SELECT ps.evolves_from_species_id, p.identifier, t.identifier
FROM (SELECT evolves_from_species_id
      FROM pokemon_species 
      WHERE id = %s) AS ps,
      pokemon_species AS p,
      pokemon_types AS pt,
      types AS t
WHERE ps.evolves_from_species_id = p.id AND p.id = pt.pokemon_id AND pt.type_id = t.id;

/*	6. Given national pokedex id, find the id of all ancestor and descendant of that pokemon	*/
SELECT ps.id, ps.identifier, t.identifier
FROM  pokemon_species AS ps, pokemon_types AS pt, types AS t
WHERE ps.id<>%s
AND evolution_chain_id = (SELECT evolution_chain_id
	FROM pokemon_species
	WHERE id=%s)
AND ps.id = pt.pokemon_id AND pt.type_id = t.id;

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
SELECT distinct ps.pokemon_id, p.identifier, t.identifier 
FROM pokemon_stats AS ps, stats AS s, pokemon_species AS p, pokemon_types AS pt, types AS t
WHERE s.identifier = %s AND ps.base_stat >= %s AND ps.pokemon_id = p.id AND pt.pokemon_id = p.id AND pt.type_id = t.id 
ORDER by ps.pokemon_id;

/*	11. Given stats sum, find all ids of qualified pokemon	*/
SELECT p.pokemon_id, ps.identifier, t.identifier 
FROM (select pokemon_id, sum(base_stat) 
	FROM pokemon_stats 
	GROUP BY pokemon_id 
	ORDER BY pokemon_id asc) AS p,
	pokemon_species AS ps,
	types AS t,
	pokemon_types AS pt
WHERE sum >= %s AND ps.id = p.pokemon_id AND pt.pokemon_id = p.pokemon_id AND pt.type_id = t.id;

/*	12. Given certain type, find ids of all pokemon of that type	*/
SELECT pt.pokemon_id, p.identifier, t.identifier 
FROM pokemon_types AS pt, types AS t, pokemon_species AS p 
WHERE t.identifier = %s AND t.id = pt.type_id AND pt.pokemon_id = p.id;

/*	13. Give the id, identifier and type of the given pokemon	*/
SELECT p.id, p.identifier, T
FROM pokemon_species as p,
(SELECT * FROM
        (SELECT t.identifier||', '||tt.identifier
        FROM pokemon_types AS pt, types AS t,
                (SELECT t.identifier, t.id, pt.slot
                FROM pokemon_types AS pt, types AS t
                WHERE pt.pokemon_id = %s AND t.id = pt.type_id) AS tt
        WHERE pt.pokemon_id = %s AND t.id = type_id AND (t.id <> tt.id         OR tt.slot = 1)
        ORDER BY t.identifier) AS p
LIMIT 1) as T
WHERE p.id = %s;
