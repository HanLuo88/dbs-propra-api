PRAGMA auto_vacuum = 1;
PRAGMA encoding = "UTF-8";
PRAGMA foreign_keys = 1;
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;

CREATE TABLE IF NOT EXISTS Pruefung
(
	PruefungsID					int			not null primary key check(PruefungsID > 0) ,
	Theorie						boolean		not null,
	Gebuehr						float 		not null check(Gebuehr > 0 AND round(Gebuehr, 2) = Gebuehr),
	BestANDen					boolean		not null,
	Email						varchar[50]	not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	
	foreign key(Email) references Schueler(Email) on update cAScade on delete cAScade
);



CREATE TABLE IF NOT EXISTS Adresse
(
	AdressID					int primary key not null check(AdressID > 0),
	Stadt						varchar[50] not null check(length(Stadt)>0 AND Stadt not glob '*[^ -~]*'),
	PLZ							varchar[50] not null check(PLZ glob '[0-9][0-9][0-9][0-9][0-9]'),
	StrASse						varchar[50] not null check(length(StrASse)>0 AND StrASse not glob '*[^ -~]*'),
	Hausnummer					varchar[50] not null check(Hausnummer glob '[0-9]' or Hausnummer glob '[0-9][0-9]' or Hausnummer glob '[0-9][0-9][0-9]' or Hausnummer glob '[0-9][0-9][0-9][0-9]' or Hausnummer glob '[0-9][0-9][0-9][0-9][a-z]' or Hausnummer glob '[0-9][0-9][0-9][a-z]' or Hausnummer glob '[0-9][0-9][a-z]' or Hausnummer glob '[0-9][a-z]')
);


CREATE TABLE IF NOT EXISTS Nutzer
(
	Email						varchar[50]	primary key not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	Vorname						varchar[50] not null collate nocASe check(length(Vorname)>0 AND Vorname not glob '*[^ -~]*'),
	Nachname					varchar[50] not null collate nocASe check(length(Nachname)>0 AND Nachname not glob '*[^ -~]*'),
	PASswort					varchar[50] not null check(length(PASswort)>=5 AND PASswort not glob '*[0-9][0-9]*'AND PASswort glob '*[A-Z]*' AND PASswort glob '*[0-9]*[0-9]*')
);


CREATE TABLE IF NOT EXISTS Fahrlehrer
(
	Email						varchar[50]	primary key not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	Erstlizenzdatum				datetime 	not null check(Erstlizenzdatum is strftime('%Y-%m-%d %H:%M:%S', Erstlizenzdatum)),
	foreign key(Email) references Nutzer(Email) on update cAScade on delete cAScade
);


CREATE TABLE IF NOT EXISTS Schueler
(
	Email						varchar[50] primary key not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	Geschlecht					varchar[50] not null collate nocASe check(length(Geschlecht)>0 AND Geschlecht in('m', 'w', 'd')),
	AdressID					int not null check(AdressID > 0),
	
	foreign key(Email) references Nutzer(Email) on update cAScade on delete cAScade,
	foreign key(AdressID) references Adresse(AdressID) on update cAScade on delete cAScade
);


CREATE TABLE IF NOT EXISTS Fuehrerschein
(
	FuehrerscheinID				int not null primary key check(FuehrerscheinID > 0),
	Ausstelldatum				datetime not null check(Ausstelldatum is strftime('%Y-%m-%d %H:%M:%S', Ausstelldatum)),
	Gueltig_bis					datetime not null  check(Gueltig_bis is strftime('%Y-%m-%d %H:%M:%S', Gueltig_bis)),
	Email						varchar[50] not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	
	foreign key(Email) references Schueler(Email) on update cAScade on delete cAScade
);


CREATE TABLE IF NOT EXISTS Verwalter
(
	Email						varchar[50] primary key not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	Telefonnummer				int not null unique check(Telefonnummer > 0),
	
	foreign key(Email) references Nutzer(Email) on update cAScade on delete cAScade
);


CREATE TABLE IF NOT EXISTS Oeffnungszeit
(
	OeffnungszeitID				int not null primary key check(OeffnungszeitID > 0),
	Werktag						varchar[50] not null collate nocASe check(length(Werktag)>0 AND Werktag not glob '*[^ -~]*'),
	Von							time not null check(Von is strftime('%H:%M:%S', Von) AND Von < Bis),
	Bis							time not null check(Bis is strftime('%H:%M:%S', Bis))
);


CREATE TABLE IF NOT EXISTS Fahrzeugklasse
(
	FahrzeugklASse_Bezeichnung	varchar[50] primary key not null collate nocASe check(length(FahrzeugklASse_Bezeichnung)>0 AND FahrzeugklASse_Bezeichnung not glob '*[^ -~]*')
);


CREATE TABLE IF NOT EXISTS Fahrschule
(
	Email						varchar[50] primary key not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	Website						varchar[50] not null unique collate nocASe check(length(Website)>0 AND Website not glob '*[^ -~]*' AND Website like 'https://%'),
	Fahrschul_Bezeichnung		varchar[50] not null collate nocASe check(length(Fahrschul_Bezeichnung)>0 AND Fahrschul_Bezeichnung not glob '*[^ -~]*'),
	AdressID					int not null check(AdressID > 0),
	Adminemail					varchar[50] not null collate nocASe check(length(Adminemail)>0 AND substr(Adminemail, 0, instr(Adminemail, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Adminemail, instr(Adminemail,'@')+1, instr(Adminemail, '.')-1 - instr(Adminemail, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Adminemail, instr(Adminemail, '.')+1,length(Adminemail)-instr(Adminemail,'.')) not glob '*[^a-zA-Z]*'),
	
	foreign key(Adminemail) references Verwalter(Email) on update cAScade on delete cAScade,
	foreign key(AdressID) references Adresse(AdressID) on update cAScade on delete cAScade
);


CREATE TABLE IF NOT EXISTS oeffnet_an
(
	OeffnungszeitID				int not null check(OeffnungszeitID > 0),
	Email						varchar[50] not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	
	primary key(OeffnungszeitID, Email),
	foreign key(OeffnungszeitID) references Oeffnungszeit(OeffnungszeitID) on update cAScade on delete cAScade,
	foreign key(Email) references Fahrschule(Email) on update cAScade on delete cAScade
);



CREATE TABLE IF NOT EXISTS Fahrstunde
(
	FahrstundeID				int primary key not null check(FahrstundeID > 0),
	Typ							varchar[50] not null collate nocASe check(length(Typ)>0 AND Typ not glob '*[^ -~]*'),
	Dauer						int not null check(Dauer > 0 AND Dauer % 45 = 0),
	Preis						float not null check(Preis > 0 AND round(Preis, 2) = Preis),
	Schueleremail				varchar[50] not null collate nocASe check(length(Schueleremail)>0 AND substr(Schueleremail, 0, instr(Schueleremail, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Schueleremail, instr(Schueleremail,'@')+1, instr(Schueleremail, '.')-1 - instr(Schueleremail, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Schueleremail, instr(Schueleremail, '.')+1,length(Schueleremail)-instr(Schueleremail,'.')) not glob '*[^a-zA-Z]*'),
	Fahrlehreremail				varchar[50]	not null collate nocASe check(length(Fahrlehreremail)>0 AND substr(Fahrlehreremail, 0, instr(Fahrlehreremail, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Fahrlehreremail, instr(Fahrlehreremail,'@')+1, instr(Fahrlehreremail, '.')-1 - instr(Fahrlehreremail, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Fahrlehreremail, instr(Fahrlehreremail, '.')+1,length(Fahrlehreremail)-instr(Fahrlehreremail,'.')) not glob '*[^a-zA-Z]*'),
	Fahrschulemail				varchar[50] not null collate nocASe check(length(Fahrschulemail)>0 AND substr(Fahrschulemail, 0, instr(Fahrschulemail, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Fahrschulemail, instr(Fahrschulemail,'@')+1, instr(Fahrschulemail, '.')-1 - instr(Fahrschulemail, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Fahrschulemail, instr(Fahrschulemail, '.')+1,length(Fahrschulemail)-instr(Fahrschulemail,'.')) not glob '*[^a-zA-Z]*'),
	

	
	foreign key(Schueleremail) references Schueler(Email) on update cAScade on delete cAScade,
	foreign key(Fahrlehreremail) references Fahrlehrer(Email) on update cAScade on delete cAScade,
	foreign key(Fahrschulemail) references Fahrschule(Email) on update cAScade on delete cAScade
);


CREATE TABLE IF NOT EXISTS Theoriestunde
(
	TheoriestundeID				int not null primary key check(TheoriestundeID > 0),
	Thema						varchar[50] not null collate nocASe check(length(Thema)>0 AND Thema not glob '*[^a-zA-Z]*'),
	Dauer						int not null check(Dauer > 0 AND Dauer % 45 = 0),
	Verpflichtend				boolean not null,
	Email						varchar[50] not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	
	foreign key(Email) references Fahrschule(Email) on update cAScade on delete cAScade
);


CREATE TABLE IF NOT EXISTS Fahrzeug
(
	Kennzeichen					varchar[50] primary key not null collate nocASe check(length(Kennzeichen)>0 AND Kennzeichen not glob '*[^ -~]*'),
	HUDatum						datetime not null check(HUDatum is strftime('%Y-%m-%d', HUDatum)),
	ErstzulASsungsdatum			datetime not null check(ErstzulASsungsdatum is strftime('%Y-%m-%d %H:%M:%S', ErstzulASsungsdatum)),
	Email						varchar[50] not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	FahrzeugklASse_Bezeichnung	varchar[50] not null collate nocASe check(length(FahrzeugklASse_Bezeichnung)>0 AND FahrzeugklASse_Bezeichnung not glob '*[^ -~]*'),
	
	foreign key(Email) references Fahrschule(Email) on update cAScade on delete cAScade,
	foreign key(FahrzeugklASse_Bezeichnung) references FahrzeugklASse(FahrzeugklASse_Bezeichnung) on update cAScade on delete cAScade
);



CREATE TABLE IF NOT EXISTS nehmen_teil
(
	Email						varchar[50] not null collate nocASe check(length(Email)>0 AND substr(Email, 0, instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email,'@')+1, instr(Email, '.')-1 - instr(Email, '@')) not glob '*[^a-zA-Z0-9]*' AND substr(Email, instr(Email, '.')+1,length(Email)-instr(Email,'.')) not glob '*[^a-zA-Z]*'),
	TheoriestundeID				int not null check(TheoriestundeID > 0),
	
	primary key(Email, TheoriestundeID),
	foreign key(Email) references Schueler(Email) on update cAScade on delete cAScade,
	foreign key(TheoriestundeID) references Theoriestunde(TheoriestundeID) on update cAScade on delete cAScade
);



CREATE TABLE IF NOT EXISTS zugelASsen_fuer
(
	FuehrerscheinID				int not null check(FuehrerscheinID > 0),
	FahrzeugklASse_Bezeichnung	varchar[50] not null collate nocASe check(length(FahrzeugklASse_Bezeichnung)>0 AND FahrzeugklASse_Bezeichnung not glob '*[^ -~]*'),
	
	primary key(FuehrerscheinID, FahrzeugklASse_Bezeichnung),
	foreign key(FuehrerscheinID) references Fuehrerschein(FuehrerscheinID) on update cAScade on delete cAScade,
	foreign key(FahrzeugklASse_Bezeichnung) references FahrzeugklASse(FahrzeugklASse_Bezeichnung) on update cAScade on delete cAScade
);



--Ein Fahrschüler soll nur eine praktische Prüfung ablegen können, wenn er in Summe 180 Fahrstundenminuten absolviert hat.
--CREATE TRIGGER 'InsertfehlerPruefung' BEFORE INSERT ON Pruefung
--BEGIN
--SELECT CASE 
--WHEN (NEW.Theorie = 0 AND (SELECT SUM(fahrstunde.dauer) FROM fahrstunde WHERE schueleremail = NEW.email) < 180) 
--THEN RAISE(ABORT, 'Nicht genug Fahrstundenminuten') 
--END; 
--END;
CREATE TRIGGER 'InsertfehlerPruefung' BEFORE INSERT ON Pruefung
BEGIN
SELECT CASE
WHEN (NEW.Theorie = 0 AND (SELECT SUM(fahrstunde.dauer) AS M
                           FROM fahrstunde
                           WHERE schueleremail = NEW.email
                           GROUP BY Schueleremail
                           HAVING M < 180))
THEN RAISE(ABORT, 'Nicht genug Fahrstundenminuten')
END;
END;


--Ein Fahrschüler soll nur eine theoretische Prüfung ablegen können, wenn er 3 verschiedene verpflichtende Theoriethemen absolviert hat.
CREATE TRIGGER 'InsertfehlerPruefung1' BEFORE INSERT ON Pruefung
BEGIN
SELECT CASE 
WHEN (NEW.Theorie = 1 AND (SELECT COUNT(anzahlT) AS t 
						   FROM 
								(SELECT distinct thema AS anzahlT 
								 FROM theoriestunde WHERE theoriestunde.verpflichtend = 1 AND theoriestunde.theoriestundeid in 
									(SELECT nehmen_teil.theoriestundeid 
									 FROM nehmen_teil WHERE nehmen_teil.email = NEW.email
									)
								)
						  ) < 3) 
THEN RAISE(ABORT, 'Nicht genug verschiedene verpflichtende Theoriethemen!') 
END; 
END;

