drop table if exists ficheRenseignement;
drop table if exists Etudiants;
drop table if exists Etablissements;
drop table if exists Adresses;
drop table if exists ServicesGestion;
drop table if exists Tuteurs;
drop table if exists infosStages;

create table Etudiants(
	id SERIAL primary key,
	nom varchar(30),
	prenom varchar(30),
	numEtudiant int,
	numPortable int,
	mail varchar(200),
	adresse varchar(200),
	typeAffiliation varchar(40),
	caisseAssurance varchar(40)
);

create table Adresses(
	id SERIAL primary key,
	adresse varchar(100),
	codePostal int,
	ville varchar(30),
	pays varchar(30)
);

create table ServicesGestion(
	id SERIAL primary key,
	nom varchar(30),
	prenom varchar(30),
	numeroTel int,
	mail varchar(50),
	adresse varchar(500)
);

create table Tuteurs(
	id SERIAL primary key,
	nom varchar(30),
	prenom varchar(30),
	fonction varchar(50),
	service varchar(100),
	numTelephone int,
	mail varchar(50),
	adresse varchar(400),
	disponibilite varchar(20)
);

create table Etablissements(
	id SERIAL primary key,
	raisonSociale varchar(300),
	representantLegal varchar(300),
	fonction varchar(300),
	numeroSiret int,
	codeApe varchar(50),
	domaineActivite varchar(300),
	effectif int,
	idAdresse int,
	serviceAccueil varchar(700),
	constraint idAdresse_fk foreign key(idAdresse) references Adresses(id)
);

create table infosStages(
	id serial primary key,
	dateDebutPartiel Date,
	dateFinPartiel Date,
	dateDebutPlein Date,
	dateFinPlein Date,
	dateDebutInterruption Date,
	dateFinInterruption Date,
	nbHeures real,
	gratification boolean,
	montantGratification real,
	versementGratification varchar(20),
	laboratoireUGA varchar(50),
	avantages varchar(50),
	confidentialite boolean,
	titre varchar(100),
	description varchar(5000),
	objectifs varchar(5000),
	taches varchar(5000),
	details varchar(5000)
);

create table ficheRenseignement(
	id serial primary key,
	idEtudiant int,
	idEtablissement int,
	idServiceGestion int,
	idTuteur int,
	idInfosStage int,
	dateDeCreation Date,
	ficheValidee int,
	constraint idEtudiant_fk foreign key(idEtudiant) references Etudiants(id),
	constraint idEtablissement_fk foreign key(idEtablissement) references Etablissements(id),
	constraint idServiceGestion_fk foreign key(idServiceGestion) references ServicesGestion(id),
	constraint idTuteur_fk foreign key(idTuteur) references Tuteurs(id),
	constraint idInfosStage_fk foreign key(idInfosStage) references infosStages(id)
);