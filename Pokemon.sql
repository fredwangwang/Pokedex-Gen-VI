/*	Query: 9	*/
select identifier
from pokemon_species
where generation_id = %s;

/*	Query: 10	*/
select distinct ps.pokemon_id 
from pokemon_stats as ps, stats as s
where s.identifier = 'hp' AND ps.base_stat >= 80 
order by ps.pokemon_id;

/*	Query: 11	*/

