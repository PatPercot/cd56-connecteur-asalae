package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Patrick Percot - Direction des systèmes d'information - Conseil départemental du Morbihan 
 * 
 * Classe d'utilitaires
 */
public class Utils {
	/**
	 * Calcule la difference en minutes entre maintenant et la date donnée
	 * 
	 * @param date la date à vérifier
	 * @return la différence en minutes entre les deux dates
	 */
	static public long getDiffToNow(Date date) {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
	    long diffInMillies = now.getTime() - date.getTime();
	    return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	/**
	 * Calcule la difference en minutes entre deux dates
	 * 
	 * @param date1 la date la plus ancienne
	 * @param date2 l date la plus récente
	 * @return la différence en minutes entre les deux dates
	 */
	static public long getDateDiff(Date date1, Date date2) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Crée une date à partir d'une chaîne de caractères
	 * 
	 * @param strdate la date à convertir
	 * @return 
	 */
	static public Date getDateTime(String strdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(strdate));
		} catch (ParseException e) {
			System.err.println("ERROR: (getDateTime) une exception de type ParseException s'est produite " + e.getMessage());
		}
		return calendar.getTime();
	}


}
