SELECT * FROM scambio_figurine.album;
SELECT * FROM scambio_figurine.user;

SELECT count(*) as album_count FROM scambio_figurine.album;
SELECT * FROM scambio_figurine.album_doppie order by qta desc;
SELECT * FROM scambio_figurine.album_mancanti;

SELECT COUNT(*) as user_count FROM scambio_figurine.user;
SELECT * FROM scambio_figurine.user;
SELECT * FROM scambio_figurine.user WHERE user_name = ' Francesco69';

#rare
SELECT
	m.mancanti,
	count(*) as count
FROM
	scambio_figurine.album_mancanti m
group by
	m.mancanti
order by 
	count desc
;

#comuni
SELECT
	d.numero,
	sum(qta) as count
FROM
	scambio_figurine.album_doppie d
group by
	d.numero
order by 
	count desc
;

#mancanti
SELECT 
	m.mancanti
FROM 
	scambio_figurine.user u,
    scambio_figurine.album_mancanti m
WHERE
	m.album_user_id = u.user_id
    and u.user_id = 0
    #and u.user_name = 'FrancescoP92'
;



#chi ha quelle che mancano a me
SELECT 
	album_user_id,
    count(*) count
FROM 
    scambio_figurine.album_doppie
WHERE
	numero IN (SELECT 
					mancanti
				FROM 
					scambio_figurine.album_mancanti
				WHERE
					album_user_id = 0)
group by album_user_id
order by count desc
;
    
#doppie
SELECT 
	*
FROM 
	scambio_figurine.user u,
    scambio_figurine.album_doppie d
WHERE
    d.album_user_id = u.user_id
    and u.comune = 'Napoli'
    #and u.provincia = 'NA - Napoli'
    #and d.numero = '83'
    and u.user_name = ' FrancescoP92'
;