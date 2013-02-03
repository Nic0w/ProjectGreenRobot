package fr.esiea.sd.greenrobot.pdf_analysis.filtering;

import java.util.Arrays;
import java.util.List;

public interface IgnoredWords {
	
	List<String> list = Arrays.asList(
		("je me moi tu te toi nous vous il elle ils elles se ses son en y le la les lui soi leurs eux lui leur sa " +
		"celui celui-ci celui-là celle celle-ci celle-là ceux ceux-ci ceux-là celles celles-ci celles-là ce ces ceci cela ça " +
		"qui que quoi dont où lequel auquel duquel laquelle lesquels auxquels desquels lesquelles auxquelles desquelle " +
		"on tout un une uns unes autre autres autre aucun aucune aucuns aucunes certains certaine certains certaines " + 
		"tel telle tels telles tout toute tous toutes " +
		"même mêmes nul nulle nuls nulles " +
		"quelqu'un quelqu'une quelques uns quelques unes personne autrui quiconque d’aucuns afin façon avec egalement entre soit " +
		"mais donc or ni car sauf sinon parce que qui quand à dont pour les de des à du sur jusqu’au bref propos fois c " +
		"dans et plus au a été chaque eu ont par notre aux plupart assez cet cette pas nos sous fin ne ainsi n ou non auprès " +
		"suis es est sommes êtes sont être aussi " +
		"ai as a avons avez ont ma depuis grâce " +
		" devrait devraient" +
		" mis puisse doivent doit puissent fait " +
		" un deux trois quatre cinq six sept huit neuf dix "
		).split(" "));
	
	

}
