insert into status (name) values ('not confirmed');
insert into status (name) values ('confirmed');

INSERT INTO genre (name)
SELECT 'Комедия'
UNION ALL
SELECT 'Драма'
UNION ALL
SELECT'Мультфильм'
UNION ALL
SELECT 'Триллер'
UNION ALL
SELECT 'Документальный'
UNION ALL
SELECT 'Боевик'
WHERE NOT EXISTS (
  SELECT * FROM genre WHERE name IN ( 'Комедия', 'Драма', 'Мультфильм', 'Триллер',
'Документальный', 'Боевик'));

INSERT INTO MPA_RATING (name)
SELECT 'G'
UNION ALL
SELECT 'PG'
UNION ALL
SELECT'PG-13'
UNION ALL
SELECT 'R'
UNION ALL
SELECT 'NC-17'
WHERE NOT EXISTS (
  SELECT * FROM genre WHERE name IN ( 'G', 'PG', 'PG-13', 'R',
'NC-17'));

